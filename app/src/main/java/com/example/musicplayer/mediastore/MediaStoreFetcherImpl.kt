package com.example.musicplayer.mediastore

import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.example.musicplayer.song.Song
import java.io.File
import javax.inject.Inject

class MediaStoreFetcherImpl(private val app: Application) : MediaStoreFetcher {

    override fun getAllSongs(): List<Song> {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC"

        val cursor = app.contentResolver.query(uri, null, selection, null, sortOrder)

        println(cursor?.count)
        if (cursor == null || !cursor.moveToFirst())
            return emptyList()

        val songs = getSongsFromCursor(cursor)
        cursor.close()
        return songs
    }

    private fun getSongsFromCursor(cursor: Cursor): List<Song> {
        val songIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
        val artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

        val songs: ArrayList<Song> = ArrayList()
        do {
            val songId = cursor.getLong(songIdIndex)
            val title = cursor.getString(titleIndex)
            val album = cursor.getString(albumIndex)
            val artist = cursor.getString(artistIndex)
            val path =
                Uri.parse(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString() + File.separator + songId)

            songs.add(
                Song(
                    songId = songId,
                    title = title,
                    path = path,
                    album = album,
                    artist = artist
                )
            )
        } while (cursor.moveToNext())

        return songs
    }
}