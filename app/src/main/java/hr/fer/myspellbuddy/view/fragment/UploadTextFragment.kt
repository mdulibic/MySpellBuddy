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
import hr.fer.myspellbuddy.analyzer.TextAnalyzer
import hr.fer.myspellbuddy.databinding.FragmentUploadTextBinding
import hr.fer.myspellbuddy.util.extensions.viewBinding
import timber.log.Timber

@AndroidEntryPoint
class UploadTextFragment : BaseFragment(R.layout.fragment_upload_text) {

    override fun getToolbar(): Toolbar = binding.toolbar.toolbar

    private val binding by viewBinding(FragmentUploadTextBinding::bind)

    private var extractedBitmap: Bitmap? = null

    private val barcodeValue by lazy { UploadTextFragmentArgs.fromBundle(requireArguments()).barcodeValue }

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
            extractedBitmap?.let { b ->
                TextAnalyzer.processImage(requireContext(), b) {
                    svm.navigate(
                        UploadTextFragmentDirections.actionUploadTextFragmentToResultFragment(
                            barcodeValue,
                            it.toTypedArray()
                        )
                    )
                }
            }
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                extractedBitmap = result.data?.extras?.get("data") as Bitmap
                binding.ivPreview.setImageBitmap(extractedBitmap)
                binding.btnCheck.isEnabled = true
            }
        }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)
    }

}