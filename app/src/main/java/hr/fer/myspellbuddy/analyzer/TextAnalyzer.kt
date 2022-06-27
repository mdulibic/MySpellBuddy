package hr.fer.myspellbuddy.analyzer

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import timber.log.Timber

object TextAnalyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun processImage(context: Context, bitmap: Bitmap, onSuccess: (List<String>) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val wordsList = arrayListOf<String>()
        val result = recognizer.process(image)
            .addOnSuccessListener { result ->
                val resultText = result.text
                for (block in result.textBlocks) {
                    for (line in block.lines) {
                        for (element in line.elements) {
                            wordsList.add(element.text.trim())
                        }
                    }
                }
                Timber.d("Words list: $wordsList")
                onSuccess(wordsList)
                Toast.makeText(context, "Result: $resultText", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "An exception occurred: $e!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }

}