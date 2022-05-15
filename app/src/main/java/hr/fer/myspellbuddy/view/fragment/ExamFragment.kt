package hr.fer.myspellbuddy.view.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.databinding.FragmentExamBinding
import hr.fer.myspellbuddy.util.extensions.viewBinding
import java.io.IOException

@AndroidEntryPoint
class ExamFragment : BaseFragment(R.layout.fragment_exam) {

    override fun getToolbar(): Toolbar? = null

    private val binding by viewBinding(FragmentExamBinding::bind)

    private val mediaPlayer = MediaPlayer()

    private val barcodeValue by lazy { ExamFragmentArgs.fromBundle(requireArguments()).barcodeValue }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        //prepareAudio()
    }

    private fun setOnClickListener() {
        binding.btnPlay.setOnClickListener {
            mediaPlayer.start()
        }
    }

    private fun prepareAudio() {
        val storage = FirebaseStorage.getInstance().reference
        storage.child("$barcodeValue.mp4").downloadUrl
            .addOnSuccessListener { uri ->
                Toast.makeText(requireContext(), "Success!", Toast.LENGTH_LONG).show()
                try {
                    mediaPlayer.setDataSource(uri.toString())
                    mediaPlayer.prepare()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_LONG).show()
            }
    }


}