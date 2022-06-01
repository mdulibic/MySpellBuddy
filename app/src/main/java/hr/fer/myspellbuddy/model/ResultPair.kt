package hr.fer.myspellbuddy.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultPair(
    val userInput: String,
    val solution: String
) : Parcelable