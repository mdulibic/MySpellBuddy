package hr.fer.myspellbuddy.util.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import hr.fer.myspellbuddy.util.other.Event

fun <T> LiveData<Event<T>>.observeEvent(viewLifecycleOwner: LifecycleOwner, onEvent: (T) -> Unit) {
    this.observe(viewLifecycleOwner) {
        it.getContentIfNotHandled()?.let(onEvent)
    }
}