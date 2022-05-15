package hr.fer.myspellbuddy.view.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.databinding.FragmentQRCodeScannerBinding
import hr.fer.myspellbuddy.util.extensions.viewBinding
import java.io.IOException


@AndroidEntryPoint
class QRCodeScannerFragment : BaseFragment(R.layout.fragment_q_r_code_scanner) {

    override fun getToolbar(): Toolbar? = null

    private val binding by viewBinding(FragmentQRCodeScannerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnPlay.setOnClickListener {
            playAudio()
            Toast.makeText(requireContext(), "PRESSED!", Toast.LENGTH_LONG).show()
        }
    }

    private fun playAudio() {
        val mediaPlayer = MediaPlayer()
        val storage = FirebaseStorage.getInstance().reference
        storage.child("test.mp4").downloadUrl
            .addOnSuccessListener(
                OnSuccessListener<Any> { uri ->
                    Toast.makeText(requireContext(), "Success!", Toast.LENGTH_LONG).show()
                    try {
                        mediaPlayer.setDataSource(uri.toString())
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }).addOnFailureListener(OnFailureListener {
                Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_LONG).show()
            })
    }


}