package hr.fer.myspellbuddy.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import hr.fer.myspellbuddy.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class MySpellBuddyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}