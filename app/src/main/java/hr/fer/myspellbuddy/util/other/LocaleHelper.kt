package hr.fer.myspellbuddy.util.other

import android.app.Activity
import android.content.Context
import java.util.*


fun setAppLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    context.createConfigurationContext(config)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}

fun setLocale(activity: Activity, languageCode: String?) {
    val locale = languageCode?.let { Locale(it) }
    locale?.let {
        Locale.setDefault(it)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(it)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}


