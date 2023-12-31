package com.example.musicplayer.player

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import com.example.musicplayer.song.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerController: PlayerController,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var duration by savedStateHandle.saveable { mutableStateOf(0L) }
    var progressPercentage by savedStateHandle.saveable { mutableStateOf(0f) }
    var progressString by savedStateHandle.saveable { mutableStateOf("00:00") }
    var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }

    private val _uiState = MutableStateFlow<UIState>(UIState.Initial)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {

            playerController.playerState.collect { playerState ->
                when (playerState) {
                    PlayerState.Initial -> _uiState.value = UIState.Initial
                    is PlayerState.Buffering -> calculateProgressValues(playerState.progress)
                    is PlayerState.Playing -> {
                        isPlaying = true
                        calculateProgressValues(playerState.progress)
                    }

                    is PlayerState.Paused -> {
                        isPlaying = false
                        calculateProgressValues(playerState.progress)
                    }

                    is PlayerState.Ready -> {
                        duration = playerState.duration
                        _uiState.value = UIState.Ready
                    }
                }
            }
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            playerController.onPlayerEvent(PlayerEvent.Stop)
        }
    }

    fun onUIEvent(uiEvent: UIEvent) = viewModelScope.launch {
        when (uiEvent) {
            UIEvent.Next -> playerController.onPlayerEvent(PlayerEvent.Next)
            UIEvent.Previous -> playerController.onPlayerEvent(PlayerEvent.Previous)
            UIEvent.PlayPause -> playerController.onPlayerEvent(PlayerEvent.PlayPause)
            is UIEvent.UpdateProgress -> {
                progressPercentage = uiEvent.newProgress
                playerController.onPlayerEvent(PlayerEvent.UpdateProgress(uiEvent.newProgress))
            }
        }
    }

    fun setSongsList(songs: List<Song>, index: Int) {
        viewModelScope.launch {
            playerController.onPlayerEvent(PlayerEvent.Pause)
            val mediaItems = songs.map { MediaItem.Builder().setUri(it.path).build() }
            playerController.onPlayerEvent(PlayerEvent.LoadMediaItems(mediaItems, index))
        }
    }

    private fun formatDuration(duration: Long): String {
        val minutes: Long = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds: Long = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)
                - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun calculateProgressValues(currentProgress: Long) {
        progressPercentage = if (currentProgress > 0) (currentProgress.toFloat() / duration) else 0f
        progressString = formatDuration(currentProgress)
    }

}

sealed class UIEvent {
    object PlayPause : UIEvent()
    object Next : UIEvent()
    object Previous : UIEvent()
    data class UpdateProgress(val newProgress: Float) : UIEvent()
}

sealed class UIState {
    object Initial : UIState()
    object Ready : UIState()
}