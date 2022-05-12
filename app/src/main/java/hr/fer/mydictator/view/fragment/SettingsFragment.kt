package hr.fer.mydictator.view.fragment

import androidx.appcompat.widget.Toolbar
import hr.fer.mydictator.R
import hr.fer.mydictator.databinding.FragmentSettingsBinding
import hr.fer.mydictator.util.extensions.viewBinding

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun getToolbar(): Toolbar? = null

    private val binding by viewBinding(FragmentSettingsBinding::bind)


}