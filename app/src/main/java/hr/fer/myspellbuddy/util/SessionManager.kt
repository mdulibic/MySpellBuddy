package hr.fer.myspellbuddy.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManager @Inject constructor(@ApplicationContext private val appContext: Context) {

    companion object {
        private const val PREFERENCES_APP = "prefs_app"
        private const val PLAYBACK = "playback"
        private const val WRITING_METHOD = "writing_method"
        private const val LANGUAGE = "language"
        private const val PAUSE_AUDIO = "pause_audio"
    }

    private val appPreferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(PREFERENCES_APP, Context.MODE_PRIVATE)
    }

    var playback: Boolean
        get() = appPreferences.getBoolean(PLAYBACK, false)
        set(value) {
            appPreferences.edit().putBoolean(PLAYBACK, value).apply()
        }

    var pauseAudio: Boolean
        get() = appPreferences.getBoolean(PAUSE_AUDIO, false)
        set(value) {
            appPreferences.edit().putBoolean(PAUSE_AUDIO, value).apply()
        }

    var writingMethod: String?
        get() = appPreferences.getString(WRITING_METHOD, "Hand writing")
        set(value) {
            appPreferences.edit().putString(WRITING_METHOD, value).apply()
        }

    var language: String?
        get() = appPreferences.getString(LANGUAGE, "hr")
        set(value) {
            appPreferences.edit().putString(LANGUAGE, value).apply()
        }
}