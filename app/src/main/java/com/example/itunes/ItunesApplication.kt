package com.example.itunes

import android.app.Application
import com.example.itunes.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ItunesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ItunesApplication)
            androidLogger()
            modules(appModule)
        }
    }
}