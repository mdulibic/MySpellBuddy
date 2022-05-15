package hr.fer.myspellbuddy.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.databinding.FragmentHomeBinding
import hr.fer.myspellbuddy.util.extensions.observeEvent
import hr.fer.myspellbuddy.util.extensions.viewBinding
import hr.fer.myspellbuddy.util.other.setLocale
import hr.fer.myspellbuddy.viewModel.HomeViewModel

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val vm: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        observeLiveData()
        vm.setupLanguage()
    }

    private fun observeLiveData() {
        vm.languageInit.observeEvent(viewLifecycleOwner) {
            //setAppLocale(requireContext(), it)
            setLocale(requireActivity(), it)
        }
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