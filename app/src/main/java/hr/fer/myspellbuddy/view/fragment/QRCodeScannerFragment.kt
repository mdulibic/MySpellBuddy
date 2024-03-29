package hr.fer.myspellbuddy.view.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.barcodeScanner.OnInputListener
import hr.fer.myspellbuddy.barcodeScanner.QrCodeAnalyzer
import hr.fer.myspellbuddy.databinding.FragmentQRCodeScannerBinding
import hr.fer.myspellbuddy.model.ExerciseEntry
import hr.fer.myspellbuddy.util.extensions.viewBinding
import hr.fer.myspellbuddy.viewModel.SettingsViewModel
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class QRCodeScannerFragment : BaseFragment(R.layout.fragment_q_r_code_scanner) {

    override fun getToolbar(): Toolbar = binding.toolbar.toolbar

    private val binding by viewBinding(FragmentQRCodeScannerBinding::bind)

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var qrCodeAnalyzer: QrCodeAnalyzer

    private val vm: SettingsViewModel by viewModels()

    private val qrInputListener = object : OnInputListener {
        override fun onInput(value: String) {
            try {
                val entry = parseEntrySet(value)
                vm.setupPause(entry.pause)
                vm.setupPlayback(entry.repeat)
                vm.setupSpeed(entry.speed)
                when (vm.getWritingMethod()) {
                    "digital_ink" -> {
                        svm.navigate(
                            QRCodeScannerFragmentDirections.actionQRCodeScannerFragmentToDigitalInkFragment(
                                entry.audioId
                            )
                        )
                    }
                    "text_recognition" -> {
                        svm.navigate(
                            QRCodeScannerFragmentDirections.actionQRCodeScannerFragmentToExamFragment(
                                entry.audioId
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                Timber.d("onInput $ex")
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        qrCodeAnalyzer = QrCodeAnalyzer(requireContext(), qrInputListener)
        checkIfCameraPermissionIsGranted()
    }

    private fun parseEntrySet(entry: String): ExerciseEntry {
        val entryList = entry.split(",")
        val textId = entryList[0].split(":")[1]
        val audioId = entryList[1].split(":")[1]
        val pause = entryList[2].split(":")[1].toBoolean()
        val repeat = entryList[3].split(":")[1].toBoolean()
        val speed = entryList[4].split(":")[1].toBoolean()
        return ExerciseEntry(
            textId = textId,
            audioId = audioId,
            pause = pause,
            repeat = repeat,
            speed = speed
        )
    }

    /**
     * This function is responsible for the setup of the camera preview and the image analyzer.
     */
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // Image analyzer
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, qrCodeAnalyzer)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    /**
     * 1. This function is responsible to request the required CAMERA permission
     */
    private fun checkCameraPermission() {
        try {
            val requiredPermissions = arrayOf(android.Manifest.permission.CAMERA)
            requireActivity().let { ActivityCompat.requestPermissions(it, requiredPermissions, 0) }
        } catch (e: IllegalArgumentException) {
            checkIfCameraPermissionIsGranted()
        }
    }

    /**
     * 2. This function will check if the CAMERA permission has been granted.
     * If so, it will call the function responsible to initialize the camera preview.
     * Otherwise, it will raise an alert.
     */
    private fun checkIfCameraPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted: start the preview
            startCamera()
        } else {
            // Permission denied
            MaterialAlertDialogBuilder(requireContext())
                .setBackground(requireContext().getDrawable(R.drawable.bg_dialog))
                .setTitle(requireContext().getString(R.string.permission_required))
                .setMessage(requireContext().getString(R.string.application_camera_access))
                .setPositiveButton(requireContext().getString(R.string.ok)) { _, _ ->
                    // Keep asking for permission until granted
                    checkCameraPermission()
                }
                .setCancelable(false)
                .create()
                .apply {
                    setCanceledOnTouchOutside(false)
                    show()
                }
        }
    }

    /**
     * 3. This function is executed once the user has granted or denied the missing permission
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkIfCameraPermissionIsGranted()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}