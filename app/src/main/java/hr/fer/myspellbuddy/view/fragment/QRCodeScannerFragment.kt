package hr.fer.myspellbuddy.view.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.barcodeScanner.QrCodeAnalyzer
import hr.fer.myspellbuddy.databinding.FragmentQRCodeScannerBinding
import hr.fer.myspellbuddy.util.extensions.viewBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class QRCodeScannerFragment : BaseFragment(R.layout.fragment_q_r_code_scanner) {

    override fun getToolbar(): Toolbar = binding.toolbar.toolbar

    private val binding by viewBinding(FragmentQRCodeScannerBinding::bind)

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var qrCodeAnalyzer: QrCodeAnalyzer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        qrCodeAnalyzer = QrCodeAnalyzer(requireContext())
        checkIfCameraPermissionIsGranted()
        onClickListener()
    }

    private fun onClickListener() {
        binding.btnCheck.setOnClickListener {
            qrCodeAnalyzer.getRawValue()?.let {
                svm.navigate(
                    QRCodeScannerFragmentDirections.actionQRCodeScannerFragmentToExamFragment(
                        it
                    )
                )
            } ?: run {
                Toast.makeText(
                    context,
                    "No valid qr code detected! Try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
                .setTitle("Permission required")
                .setMessage("This application needs to access the camera to process barcodes")
                .setPositiveButton("Ok") { _, _ ->
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