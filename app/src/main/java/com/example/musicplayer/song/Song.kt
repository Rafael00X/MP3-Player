package com.example.musicplayer.song

import android.net.Uri

data class Song(
    val songId: Long,
    val title: String,
    val path: Uri,
    val album: String? = null,
    val artist: String? = null,
)