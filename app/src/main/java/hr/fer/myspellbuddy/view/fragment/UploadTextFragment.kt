package hr.fer.myspellbuddy.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.barcodeScanner.TextAnalyzer
import hr.fer.myspellbuddy.databinding.FragmentUploadTextBinding
import hr.fer.myspellbuddy.util.extensions.viewBinding
import timber.log.Timber


@AndroidEntryPoint
class UploadTextFragment : BaseFragment(R.layout.fragment_upload_text) {

    override fun getToolbar(): Toolbar = binding.toolbar.toolbar

    private val binding by viewBinding(FragmentUploadTextBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListener()
    }

    private fun onClickListener() {
        binding.btnUpload.setOnClickListener {
            openCamera()
        }
        binding.btnCheck.setOnClickListener {
            Timber.d("Check image!")
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                binding.ivPreview.setImageBitmap(bitmap)
                binding.btnCheck.isEnabled = true
                TextAnalyzer.processImage(requireContext(), bitmap)
            }
        }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)
    }

}