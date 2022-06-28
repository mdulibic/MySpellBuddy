package hr.fer.myspellbuddy.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.databinding.FragmentHomeBinding
import hr.fer.myspellbuddy.util.extensions.observeEvent
import hr.fer.myspellbuddy.util.extensions.viewBinding
import hr.fer.myspellbuddy.view.activity.HomeActivity
import hr.fer.myspellbuddy.viewModel.HomeViewModel
import hr.fer.myspellbuddy.viewModel.SettingsViewModel

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val homeVm: HomeViewModel by viewModels()

    private val settingsVm: SettingsViewModel by viewModels()

    private val languages = arrayListOf("en", "hr")

    private val writingMethods = arrayListOf("digital_ink", "text_recognition")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        observeLiveData()
        initSpinners()
        settingsVm.initSettings()
        homeVm.setupLanguage()
    }

    private fun observeLiveData() {
        homeVm.languageInit.observeEvent(viewLifecycleOwner) {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(it)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

        settingsVm.languageInit.observeEvent(viewLifecycleOwner) {
            binding.spinnerLanguage.apply {
                setSelection(languages.indexOf(it), false)
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        val language = languages[position]
                        settingsVm.setupLanguage(language)
                    }
                }
            }
        }
        settingsVm.languageChange.observeEvent(viewLifecycleOwner) {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(it)
            AppCompatDelegate.setApplicationLocales(appLocale)
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
        }

        settingsVm.writingMethodChange.observeEvent(viewLifecycleOwner) {
            binding.spinnerWritingMethod.setSelection(writingMethods.indexOf(it), false)
        }
    }

    override fun getToolbar(): Toolbar? = null

    private fun setOnClickListener() {
        binding.btnStart.setOnClickListener {
            svm.navigate(HomeFragmentDirections.actionHomeFragmentToQRCodeScannerFragment())
        }
    }

    private fun initSpinners() {
        binding.apply {
            spinnerLanguage.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_spinner,
                resources.getStringArray(R.array.Languages)
            )
            spinnerWritingMethod.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_spinner,
                resources.getStringArray(R.array.WritingMethods)
            )
        }
    }

}