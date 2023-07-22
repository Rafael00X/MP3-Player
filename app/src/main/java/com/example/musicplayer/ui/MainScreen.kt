package com.example.musicplayer.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.musicplayer.ui.navigation.drawer.NavigationDrawer
import com.example.musicplayer.ui.song.SongScreen

@Composable
fun MainScreen() {
    NavigationDrawer(modifier = Modifier) {
        SongScreen()
    }

}
