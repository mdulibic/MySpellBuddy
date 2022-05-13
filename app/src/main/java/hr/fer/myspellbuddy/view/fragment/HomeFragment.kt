package hr.fer.myspellbuddy.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.databinding.FragmentHomeBinding
import hr.fer.myspellbuddy.util.extensions.viewBinding

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    override fun getToolbar(): Toolbar? = null

    private fun setOnClickListener() {
        binding.btnStart.setOnClickListener {
            svm.navigate(HomeFragmentDirections.actionHomeFragmentToQRCodeScannerFragment())
        }

        binding.btnSettings.setOnClickListener {
            svm.navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
        }
    }

}