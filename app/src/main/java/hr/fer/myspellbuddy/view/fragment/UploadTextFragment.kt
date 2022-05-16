package hr.fer.myspellbuddy.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
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
            openGalleryForImage()
        }
        binding.btnCheck.setOnClickListener {
            Timber.d("Check image!")
        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imgUri = result.data
                imgUri?.data.let {
                    if (it != null) {
                        binding.btnCheck.isEnabled = true
                        //handle pic
                    }
                }
                binding.ivPreview.setImageURI(imgUri?.data)
            }
        }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

}