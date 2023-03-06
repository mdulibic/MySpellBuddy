package hr.fer.myspellbuddy.util

import android.media.MediaPlayer
import android.net.Uri

object PlayerWrapper {

    private val mediaPlayer = MediaPlayer()

    fun setupPlayer(uri: Uri) {
        mediaPlayer.setDataSource(uri.toString())
        mediaPlayer.prepare()
    }

    fun startPlayer() {
        mediaPlayer.start()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
    }

    fun restartPlayer() {
        mediaPlayer.pause()
        mediaPlayer.seekTo(0)
        mediaPlayer.start()
    }

    fun resetPlayer() {
        mediaPlayer.reset()
    }

    private fun onCompleteListener() {
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.reset()
        }
    }
}