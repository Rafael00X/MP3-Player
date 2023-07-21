package com.example.musicplayer.player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerController @Inject constructor(
    private val player: Player
) : Player.Listener {

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Initial)
    val playerState = _playerState.asStateFlow()

    private var progressJob: Job? = null

    init {
        player.addListener(this)
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.prepare()
        progressJob = Job()
    }

    fun addMediaItem(mediaItem: MediaItem) {
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    fun addMediaItemList(mediaItemList: List<MediaItem>) {
        player.setMediaItems(mediaItemList)
        player.prepare()
    }

    fun onPlayerEvent(playerEvent: PlayerEvent) {
        when (playerEvent) {
            PlayerEvent.Play -> play()
            PlayerEvent.Pause -> pause()
            PlayerEvent.PlayPause -> {
                if (player.isPlaying) pause() else play()
            }

            PlayerEvent.Next -> player.seekToNextMediaItem()
            PlayerEvent.Previous -> player.seekToPreviousMediaItem()
            PlayerEvent.Stop -> stop()
            PlayerEvent.Repeat -> {
                if (player.repeatMode == Player.REPEAT_MODE_ALL) {
                    player.repeatMode = Player.REPEAT_MODE_ONE
                } else {
                    player.repeatMode = Player.REPEAT_MODE_ALL
                }
            }

            is PlayerEvent.UpdateProgress -> player.seekTo((player.duration * playerEvent.newProgress).toLong())
            is PlayerEvent.LoadMediaItems -> loadMediaItems(
                playerEvent.mediaItems,
                playerEvent.index
            )
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _playerState.value =
                PlayerState.Buffering(player.currentPosition)

            ExoPlayer.STATE_READY -> _playerState.value =
                PlayerState.Ready(player.duration)

            Player.STATE_ENDED -> {
                // TODO()
            }

            Player.STATE_IDLE -> {
                // TODO()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying) {
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = progressJob.run {
        while (true) {
            delay(500)
            _playerState.value = PlayerState.Playing(player.currentPosition)
        }
    }

    private fun stopProgressUpdate() {
        progressJob?.cancel()
        _playerState.value = PlayerState.Paused(player.currentPosition)
    }

    private fun play() {
        if (player.currentMediaItem != null && !player.isPlaying)
            player.play()
    }

    private fun pause() {
        if (player.isPlaying)
            player.pause()
    }

    private fun stop() {
        player.stop()
        stopProgressUpdate()
    }

    private fun loadMediaItems(mediaItems: List<MediaItem>, index: Int) {
        player.stop()
        player.setMediaItems(mediaItems, index, 0L)
        player.prepare()
        player.play()
    }

}

sealed class PlayerEvent {
    object PlayPause : PlayerEvent()
    object Play : PlayerEvent()
    object Pause : PlayerEvent()
    object Next : PlayerEvent()
    object Previous : PlayerEvent()
    object Stop : PlayerEvent()
    object Repeat : PlayerEvent()
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()
    data class LoadMediaItems(val mediaItems: List<MediaItem>, val index: Int = 0) : PlayerEvent()
}

sealed class PlayerState {
    object Initial : PlayerState()
    data class Ready(val duration: Long) : PlayerState()
    data class Buffering(val progress: Long) : PlayerState()
    data class Playing(val progress: Long) : PlayerState()
    data class Paused(val progress: Long) : PlayerState()
}
