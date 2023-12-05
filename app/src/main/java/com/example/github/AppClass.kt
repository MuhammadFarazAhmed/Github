package com.example.github

import android.app.Application
import com.example.github.di.appModule
import com.example.github.di.featureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AppClass)
            modules(appModule() + featureModule())
        }

    }

}