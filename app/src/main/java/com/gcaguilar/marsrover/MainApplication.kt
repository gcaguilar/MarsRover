package com.gcaguilar.marsrover

import android.app.Application
import com.gcaguilar.marsrover.data.dataModule
import com.gcaguilar.marsrover.domain.domainModule
import com.gcaguilar.marsrover.presentation.presentationModule
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin() {
            androidLogger()
            modules(dataModule, domainModule, presentationModule)
        }
    }
}