package hr.fer.myspellbuddy.barcodeScanner

import android.annotation.SuppressLint
import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class QrCodeAnalyzer(private val context: Context) : ImageAnalysis.Analyzer {

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
                        /*
                        Toast.makeText(
                            context,
                            "Value: " + barcode.rawValue,
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

    fun getRawValue(): String? = rawValue
}