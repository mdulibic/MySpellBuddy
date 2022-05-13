package hr.fer.myspellbuddy.view.fragment

import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.databinding.FragmentSettingsBinding
import hr.fer.myspellbuddy.util.extensions.viewBinding

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun getToolbar(): Toolbar = binding.toolbar

    private val binding by viewBinding(FragmentSettingsBinding::bind)


}