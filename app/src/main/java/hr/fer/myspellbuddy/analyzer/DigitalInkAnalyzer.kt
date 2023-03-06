package hr.fer.myspellbuddy.analyzer

import android.content.Context
import android.view.MotionEvent
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.*
import timber.log.Timber

object DigitalInkAnalyzer {
    private var inkBuilder = Ink.builder()
    private var strokeBuilder = Ink.Stroke.builder()
    private lateinit var model: DigitalInkRecognitionModel

    fun addNewTouchEvent(event: MotionEvent) {
        val action = event.actionMasked
        val x = event.x
        val y = event.y
        val t = System.currentTimeMillis()

        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> strokeBuilder.addPoint(
                Ink.Point.create(
                    x,
                    y,
                    t
                )
            )
            MotionEvent.ACTION_UP -> {
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                inkBuilder.addStroke(strokeBuilder.build())
                strokeBuilder = Ink.Stroke.builder()

            }
            else -> {
                // Action not relevant for ink construction
            }
        }
    }

    fun download() {
        var modelIdentifier: DigitalInkRecognitionModelIdentifier? = null
        try {
            modelIdentifier =
                DigitalInkRecognitionModelIdentifier.fromLanguageTag("HR")
        } catch (e: MlKitException) {
            Timber.d("DigitalInkRecognitionModelIdentifier.fromLanguageTag error: $e")
        }

        model =
            DigitalInkRecognitionModel.builder(modelIdentifier!!).build()

        val remoteModelManager = RemoteModelManager.getInstance()
        remoteModelManager.download(model, DownloadConditions.Builder().build())
            .addOnSuccessListener {
                Timber.d("DigitalInkAnalyzer : Model downloaded")
            }
            .addOnFailureListener { e: Exception ->
                Timber.d("DigitalInkAnalyzer : Error while downloading a model: $e")
            }
    }

    fun recognize(context: Context, onSuccess: (List<String>) -> Unit) {
        val recognizer: DigitalInkRecognizer =
            DigitalInkRecognition.getClient(
                DigitalInkRecognizerOptions.builder(model).build()
            )

        val ink = inkBuilder.build()

        recognizer.recognize(ink)
            .addOnSuccessListener { result: RecognitionResult ->
                val list = result.candidates[0].text.replace("\n", "").replace("\r", "").split(" ")
                list.forEach { it.trim() }
                onSuccess(list)
                Timber.d("DigitalInkAnalyzer recognized text: ${result.candidates[0].text}")
                clear()
            }
            .addOnFailureListener { e: Exception ->
                Timber.d("DigitalInkAnalyzer : Error during recognition: $e")
            }
    }

    fun clear() {
        inkBuilder = Ink.builder()
    }
}