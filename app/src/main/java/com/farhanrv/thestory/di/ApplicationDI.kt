package com.farhanrv.thestory.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ApplicationDI : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@ApplicationDI)
            modules(
                listOf(
                    viewModelModule,
                    apiModule,
                    repositoryModule,
                    databaseModule
                )
            )
        }
    }
}