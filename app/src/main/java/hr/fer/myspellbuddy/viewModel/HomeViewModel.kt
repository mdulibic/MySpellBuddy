package hr.fer.myspellbuddy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.fer.myspellbuddy.util.SessionManager
import hr.fer.myspellbuddy.util.other.Event
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _languageInit = MutableLiveData<Event<String>>()
    val languageInit: LiveData<Event<String>>
        get() = _languageInit

    fun setupLanguage() {
        sessionManager.language?.let {
            _languageInit.value = Event(it)
        }
    }

}