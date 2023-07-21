package com.example.musicplayer.ui.song

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicplayer.song.Song

@Composable
fun SongCard(song: Song, index: Int, handleClick: (Song, Int) -> Unit) {
    val titleFontSize = 15.sp
    val textFontSize = 8.sp
    println("Inside SongCard")
    Box(Modifier.clickable { handleClick(song, index) }) {
        Column {
            Text(song.title, fontSize = titleFontSize)
            Row {
                Text(song.artist ?: "No Artist", fontSize = textFontSize)
                Spacer(modifier = Modifier.width(10.dp))
                Text(song.album ?: "No Album", fontSize = textFontSize)
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun SongCardPreview() {
    val song = Song(1L, "First Song", Uri.EMPTY, "Album", "Artist")
    SongCard(song = song, index = 0, handleClick = {a, b ->})
}