package hr.fer.myspellbuddy.barcodeScanner

import android.annotation.SuppressLint
import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

interface OnInputListener {
    fun onInput(value: String)
}

class QrCodeAnalyzer(private val context: Context, private val inputListener: OnInputListener) :
    ImageAnalysis.Analyzer {

    private var rawValue: String? = null

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val img = image.image
        if (img != null) {
            val inputImage = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)

            // Process image searching for barcodes
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            val scanner = BarcodeScanning.getClient(options)

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        rawValue = barcode.rawValue
                        rawValue?.let { inputListener.onInput(it) }
                        /*
                        Timber.d("Raw value: $rawValue")
                        Toast.makeText(
                            context,
                            "Value: $rawValue",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                         */
                    }
                }
                .addOnFailureListener { }
                .addOnCompleteListener {
                    image.close()
                }
        }
        image.close()
    }
}