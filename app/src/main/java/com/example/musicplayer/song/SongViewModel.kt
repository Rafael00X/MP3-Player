package com.example.musicplayer.song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.mediastore.MediaStoreFetcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val mediaStoreHelper: MediaStoreFetcher
) : ViewModel() {
    private val _state = MutableStateFlow(State(emptyList()))
    val state = _state.asStateFlow()

    fun getAllSongs() {
        viewModelScope.launch {
            val songs = mediaStoreHelper.getAllSongs()

            println("Number of songs = ${songs.size}")
            _state.update {
                it.copy(
                    songs = songs
                )
            }
        }
    }

    data class State(val songs: List<Song>)
}