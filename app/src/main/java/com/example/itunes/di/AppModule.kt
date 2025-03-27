package com.example.itunes.di

import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.itunes.repository.SearchRepository
import com.example.itunes.ui.player.PlayerViewModel
import com.example.itunes.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { provideHttpClient() }
    single { provideConverterFactory() }
    single { provideRetrofit(get(), get()) }
    single { provideService(get()) }
    single { SearchRepository(get()) }
    viewModel { SearchViewModel(get()) }

    single {
        ExoPlayer.Builder(androidContext()).setAudioAttributes(
            androidx.media3.common.AudioAttributes.DEFAULT, true
        ).setHandleAudioBecomingNoisy(true).build().apply {
            repeatMode = Player.REPEAT_MODE_OFF
        }
    }
    viewModel { params ->
        PlayerViewModel(
            exoPlayer = get()
        )
    }
}
