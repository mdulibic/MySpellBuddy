package hr.fer.myspellbuddy.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseEntry(
    val soundId: String,
    val textId: String,
    val pause: Boolean,
    val repeat: Boolean,
    val speed: Boolean
) : Parcelable