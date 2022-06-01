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
import hr.fer.myspellbuddy.databinding.FragmentSettingsBinding
import hr.fer.myspellbuddy.util.extensions.observeEvent
import hr.fer.myspellbuddy.util.extensions.viewBinding
import hr.fer.myspellbuddy.view.activity.HomeActivity
import hr.fer.myspellbuddy.viewModel.SettingsViewModel

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun getToolbar(): Toolbar = binding.toolbar.toolbar

    private val vm: SettingsViewModel by viewModels()

    private val languages = arrayListOf("en", "hr")

    private val writingMethods = arrayListOf("digital", "handwritten")

    private val binding by viewBinding(FragmentSettingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinners()
        vm.initSettings()
        initListeners()
        observeLiveData()
    }

    private fun observeLiveData() {
        vm.languageInit.observeEvent(viewLifecycleOwner) {
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
                        vm.setupLanguage(language)
                    }
                }
            }
        }
        vm.languageChange.observeEvent(viewLifecycleOwner) {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(it)
            AppCompatDelegate.setApplicationLocales(appLocale)
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
        }

        vm.pauseSetupChange.observeEvent(viewLifecycleOwner) {
            binding.checkboxPause.isChecked = it
        }

        vm.playbackSetupChange.observeEvent(viewLifecycleOwner) {
            binding.checkboxPlayback.isChecked = it
        }

        vm.writingMethodChange.observeEvent(viewLifecycleOwner) {
            binding.spinnerWritingMethod.setSelection(writingMethods.indexOf(it), false)
        }
    }

    private fun initListeners() {
        binding.apply {

            checkboxPause.setOnCheckedChangeListener { _, isChecked ->
                vm.setupPause(isChecked)
            }

            checkboxPlayback.setOnCheckedChangeListener { _, isChecked ->
                vm.setupPlayback(isChecked)
            }

            spinnerWritingMethod.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        val method = writingMethods[position]
                        vm.setupWritingMethod(method)
                    }
                }
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