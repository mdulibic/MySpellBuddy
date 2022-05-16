package hr.fer.myspellbuddy.barcodeScanner

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

object TextAnalyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun processImage(context: Context, bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val result = recognizer.process(image)
            .addOnSuccessListener { result ->
                val resultText = result.text
                Toast.makeText(context, "Result: $resultText", Toast.LENGTH_LONG).show()
                for (block in result.textBlocks) {
                    val blockText = block.text
                    val blockCornerPoints = block.cornerPoints
                    val blockFrame = block.boundingBox
                    for (line in block.lines) {
                        val lineText = line.text
                        val lineCornerPoints = line.cornerPoints
                        val lineFrame = line.boundingBox
                        for (element in line.elements) {
                            val elementText = element.text
                            val elementCornerPoints = element.cornerPoints
                            val elementFrame = element.boundingBox
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
    }

}