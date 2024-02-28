package uk.youniverse.youniverse_voice.presentation

import android.Manifest
import android.content.Context
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File

class VoiceRecorder(
    private val context : Context,
    private val fileName : String
) {
    private var mediaRecorder: MediaRecorder? = null
    private val file = File(context.filesDir, fileName)

    // Derived from WearSpeakerSample (https://github.com/android/wear-os-samples/tree/main)
    private enum class State {
        IDLE, RECORDING, PAUSED
    }

    private var recordingState = State.IDLE

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    suspend fun startRecording() {
        if (recordingState != State.IDLE) {
            // TODO: Exception case
            return
        }

        suspendCancellableCoroutine<Unit> { cont ->
            @Suppress("DEPRECATION")
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.OGG)
                setAudioEncoder(MediaRecorder.AudioEncoder.OPUS)
                setOutputFile(file.path)
                setOnInfoListener { mr, what, extra ->
                    println("info: $mr $what $extra")
                }
                setOnErrorListener { mr, what, extra ->
                    println("error: $mr $what $extra")
                }
            }

            cont.invokeOnCancellation {
                mediaRecorder?.apply {
                    stop()
                    release()
                }
                mediaRecorder = null
                recordingState = State.IDLE
            }

            mediaRecorder?.prepare()
            mediaRecorder?.start()

            recordingState = State.RECORDING
        }
    }

    fun pauseRecording() {
        if (recordingState == State.RECORDING) {
            mediaRecorder?.pause()
            recordingState = State.PAUSED
        }
    }

    fun resumeRecording() {
        if (recordingState == State.PAUSED) {
            mediaRecorder?.resume()
            recordingState = State.RECORDING
        }
    }
}