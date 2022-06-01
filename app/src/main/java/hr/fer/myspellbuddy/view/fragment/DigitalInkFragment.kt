package hr.fer.myspellbuddy.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.google.firebase.storage.FirebaseStorage
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.analyzer.DigitalInkAnalyzer
import hr.fer.myspellbuddy.databinding.FragmentDigitalInkBinding
import hr.fer.myspellbuddy.util.PlayerWrapper
import hr.fer.myspellbuddy.util.extensions.observeEvent
import hr.fer.myspellbuddy.util.extensions.viewBinding
import hr.fer.myspellbuddy.viewModel.ExamViewModel
import java.io.IOException

class DigitalInkFragment : BaseFragment(R.layout.fragment_digital_ink) {

    override fun getToolbar(): Toolbar = binding.toolbar.toolbar

    private val binding by viewBinding(FragmentDigitalInkBinding::bind)

    private val barcodeValue by lazy { ExamFragmentArgs.fromBundle(requireArguments()).barcodeValue }

    private val vm: ExamViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DigitalInkAnalyzer.download()
        onClickListener()
        prepareAudio()
        observeLiveData()
        vm.getPlayerSetup()
    }

    private fun onClickListener() {
        binding.apply {
            btnClear.setOnClickListener {
                canvas.clear()
                DigitalInkAnalyzer.clear()
            }

            btnContinue.setOnClickListener {
                DigitalInkAnalyzer.recognize(requireContext()) {
                    svm.navigate(
                        DigitalInkFragmentDirections.actionDigitalInkFragmentToResultFragment(
                            it.toTypedArray(),
                            barcodeValue
                        )
                    )
                }
            }

            binding.btnPlay.setOnClickListener {
                PlayerWrapper.startPlayer()
            }
            binding.btnPause.setOnClickListener {
                PlayerWrapper.pausePlayer()
            }
            binding.btnPlayback.setOnClickListener {
                PlayerWrapper.restartPlayer()
            }
        }
    }

    private fun observeLiveData() {
        vm.pauseSetup.observeEvent(viewLifecycleOwner) {
            binding.btnPause.visibility = if (it) View.VISIBLE else View.GONE
        }
        vm.playbackSetup.observeEvent(viewLifecycleOwner) {
            binding.btnPlayback.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun prepareAudio() {
        val storage = FirebaseStorage.getInstance().reference
        storage.child("$barcodeValue.mp3").downloadUrl
            .addOnSuccessListener { uri ->
                Toast.makeText(
                    requireContext(),
                    requireContext().getText(R.string.audio_ready),
                    Toast.LENGTH_LONG
                ).show()
                try {
                    PlayerWrapper.setupPlayer(uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    requireContext().getText(R.string.audio_fail),
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    override fun onPause() {
        super.onPause()
        PlayerWrapper.resetPlayer()
    }
}