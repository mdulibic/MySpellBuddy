package hr.fer.myspellbuddy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.fer.myspellbuddy.util.navigation.NavCommand
import hr.fer.myspellbuddy.util.other.Event
import javax.inject.Inject

/**
 * used for navigation
 */
@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _navCommand = MutableLiveData<Event<NavCommand>>()
    val navCommand: LiveData<Event<NavCommand>> = _navCommand


    fun navigate(directions: NavDirections) {
        _navCommand.postValue(Event(NavCommand.To(directions)))
    }

    fun navigateUp() {
        _navCommand.postValue(Event(NavCommand.Up))
    }

}