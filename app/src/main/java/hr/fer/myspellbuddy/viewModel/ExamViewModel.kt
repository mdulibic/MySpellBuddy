package hr.fer.myspellbuddy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.fer.myspellbuddy.util.SessionManager
import hr.fer.myspellbuddy.util.other.Event
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _pauseSetup = MutableLiveData<Event<Boolean>>()
    val pauseSetup: LiveData<Event<Boolean>>
        get() = _pauseSetup

    private val _playbackSetup = MutableLiveData<Event<Boolean>>()
    val playbackSetup: LiveData<Event<Boolean>>
        get() = _playbackSetup

    fun getPlayerSetup() {
        _pauseSetup.value = Event(sessionManager.pauseAudio)
        _playbackSetup.value = Event(sessionManager.playback)
    }
}