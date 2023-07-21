package com.example.musicplayer.mediastore

import com.example.musicplayer.song.Song

interface MediaStoreFetcher {
    fun getAllSongs() : List<Song>
}