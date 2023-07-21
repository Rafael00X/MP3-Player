package com.example.musicplayer.ui.song

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicplayer.player.PlayerViewModel
import com.example.musicplayer.song.Song
import com.example.musicplayer.song.SongViewModel
import com.example.musicplayer.ui.player.PlayerScreen

@Composable
fun SongScreen(
    songViewModel: SongViewModel = viewModel(),
    playerViewModel: PlayerViewModel = viewModel()
) {
    val state by songViewModel.state.collectAsState()
    val songs: List<Song> = state.songs

    println("Inside SongScreen")

    val handleClick = { song: Song, index: Int ->
        println("From SongScreen handleClick")
        println(song)
        println(index)
        playerViewModel.setSongsList(songs, index)
        println(song.toString())
    }

    Column {
        PlayerScreen()
        Spacer(modifier = Modifier.height(10.dp))
        Text("SongScreen")
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            println("Button clicked")
            songViewModel.getAllSongs()
        }) {
            Text("Fetch Songs")
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            itemsIndexed(songs) { index, song -> SongCard(song = song, index = index, handleClick = handleClick) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SongScreenPreview() {
    SongScreen()
}