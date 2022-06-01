package hr.fer.myspellbuddy.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.databinding.FragmentExamBinding
import hr.fer.myspellbuddy.util.PlayerWrapper
import hr.fer.myspellbuddy.util.extensions.observeEvent
import hr.fer.myspellbuddy.util.extensions.viewBinding
import hr.fer.myspellbuddy.viewModel.ExamViewModel
import java.io.IOException

@AndroidEntryPoint
class ExamFragment : BaseFragment(R.layout.fragment_exam) {

    override fun getToolbar(): Toolbar = binding.toolbar.toolbar

    private val binding by viewBinding(FragmentExamBinding::bind)

    private val barcodeValue by lazy { ExamFragmentArgs.fromBundle(requireArguments()).barcodeValue }

    private val vm: ExamViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        prepareAudio()
        observeLiveData()
        vm.getPlayerSetup()
    }

    private fun observeLiveData() {
        vm.pauseSetup.observeEvent(viewLifecycleOwner) {
            binding.btnPause.visibility = if (it) View.VISIBLE else View.GONE
        }
        vm.playbackSetup.observeEvent(viewLifecycleOwner) {
            binding.btnPlayback.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setOnClickListener() {
        binding.btnPlay.setOnClickListener {
            PlayerWrapper.startPlayer()
        }
        binding.btnPause.setOnClickListener {
            PlayerWrapper.pausePlayer()
        }
        binding.btnPlayback.setOnClickListener {
            PlayerWrapper.restartPlayer()
        }
        binding.btnContinue.setOnClickListener {
            svm.navigate(ExamFragmentDirections.actionExamFragmentToUploadTextFragment(barcodeValue))
        }
    }

    private fun prepareAudio() {
        val storage = FirebaseStorage.getInstance().reference
        storage.child("$barcodeValue.mp3").downloadUrl
            .addOnSuccessListener { uri ->
                Toast.makeText(requireContext(), "Success!", Toast.LENGTH_LONG).show()
                try {
                    PlayerWrapper.setupPlayer(uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_LONG).show()
            }
    }

    override fun onPause() {
        super.onPause()
        PlayerWrapper.resetPlayer()
    }


}