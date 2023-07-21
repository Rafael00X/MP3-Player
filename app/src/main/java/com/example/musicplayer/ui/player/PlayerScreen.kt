package com.example.musicplayer.ui.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicplayer.player.PlayerViewModel
import com.example.musicplayer.player.UIEvent


@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = viewModel(),
) {
    val handlePlay = { viewModel.onUIEvent(UIEvent.PlayPause) }
    val handlePause = { viewModel.onUIEvent(UIEvent.PlayPause) }
    val handleNext = { viewModel.onUIEvent(UIEvent.Next) }
    val handlePrev = { viewModel.onUIEvent(UIEvent.Previous) }

    Column {
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Button(onClick = { handlePlay() }) {
                Text("Play")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = { handlePause() }) {
                Text("Pause")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = { handleNext() }) {
                Text("Next")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = { handlePrev() }) {
                Text("Prev")
            }
        }
    }

}