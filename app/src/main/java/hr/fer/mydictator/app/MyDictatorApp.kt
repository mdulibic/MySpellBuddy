package hr.fer.mydictator.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import hr.fer.mydictator.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class MyDictatorApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}