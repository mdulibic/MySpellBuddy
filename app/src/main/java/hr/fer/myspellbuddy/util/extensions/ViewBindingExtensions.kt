package hr.fer.myspellbuddy.util.extensions

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import hr.fer.myspellbuddy.view.binding.ViewBindingFragmentDelegate

inline fun <VB : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> VB
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

fun <VB : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> VB) =
    ViewBindingFragmentDelegate(this, viewBindingFactory)