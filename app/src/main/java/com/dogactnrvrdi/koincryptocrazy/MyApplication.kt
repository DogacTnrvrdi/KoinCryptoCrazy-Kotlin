package com.dogactnrvrdi.koincryptocrazy

import android.app.Application
import com.dogactnrvrdi.koincryptocrazy.di.appModule
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }
}