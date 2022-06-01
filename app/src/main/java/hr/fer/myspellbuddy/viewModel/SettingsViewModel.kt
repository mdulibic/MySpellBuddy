package hr.fer.myspellbuddy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.fer.myspellbuddy.util.SessionManager
import hr.fer.myspellbuddy.util.other.Event
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _languageInit = MutableLiveData<Event<String>>()
    val languageInit: LiveData<Event<String>>
        get() = _languageInit

    private val _languageChange = MutableLiveData<Event<String>>()
    val languageChange: LiveData<Event<String>>
        get() = _languageChange

    private val _pauseSetupChange = MutableLiveData<Event<Boolean>>()
    val pauseSetupChange: LiveData<Event<Boolean>>
        get() = _pauseSetupChange

    private val _playbackSetupChange = MutableLiveData<Event<Boolean>>()
    val playbackSetupChange: LiveData<Event<Boolean>>
        get() = _playbackSetupChange

    private val _writingMethodChange = MutableLiveData<Event<String>>()
    val writingMethodChange: LiveData<Event<String>>
        get() = _writingMethodChange

    fun initSettings() {
        sessionManager.language?.let {
            _languageInit.value = Event(it)
        }

        sessionManager.writingMethod?.let {
            _writingMethodChange.value = Event(it)
        }

        _playbackSetupChange.value = Event(sessionManager.playback)
        _pauseSetupChange.value = Event(sessionManager.pauseAudio)
    }

    fun setupPause(isEnabled: Boolean) {
        Timber.d("Pause $isEnabled")
        sessionManager.pauseAudio = isEnabled
        _pauseSetupChange.value = Event(isEnabled)
    }

    fun setupPlayback(isEnabled: Boolean) {
        sessionManager.playback = isEnabled
        _playbackSetupChange.value = Event(isEnabled)
    }

    fun setupWritingMethod(method: String) {
        sessionManager.writingMethod = method
        _writingMethodChange.value = Event(method)
    }

    fun setupLanguage(language: String) {
        sessionManager.language = language
        _languageChange.value = Event(language)
    }

    fun getWritingMethod(): String? {
        return sessionManager.writingMethod
    }
}