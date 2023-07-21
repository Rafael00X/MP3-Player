package com.example.musicplayer.di

import android.app.Application
import com.example.musicplayer.mediastore.MediaStoreFetcher
import com.example.musicplayer.mediastore.MediaStoreFetcherImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMediaStoreFetcher() : MediaStoreFetcher {
        return MediaStoreFetcherImpl(app = Application())
    }
}