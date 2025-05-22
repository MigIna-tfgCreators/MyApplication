package com.example.myapplication

import android.app.Application
import com.example.myapplication.classes.providers.ContextProviderInterface
import com.example.myapplication.configurations.appModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import kotlin.getValue

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }

        val contextProviderInterface: ContextProviderInterface by inject()
        contextProviderInterface.currentActivity
    }
}