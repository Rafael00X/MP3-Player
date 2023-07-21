package com.example.musicplayer.player

import androidx.media3.common.Player
import javax.inject.Inject

class PlayerController @Inject constructor(
    private val player: Player
) : Player.Listener {

}