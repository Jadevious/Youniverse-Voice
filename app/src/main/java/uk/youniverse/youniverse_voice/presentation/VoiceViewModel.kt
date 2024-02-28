package uk.youniverse.youniverse_voice.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val FILE_NAME = "CR.opus"

class VoiceViewModel(application: Application) : AndroidViewModel(application) {

    private val temp = application.applicationContext.filesDir

    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private var recordingJob: Job? = null

    private val voiceRecorder = VoiceRecorder(application.applicationContext, FILE_NAME)

    private val _voiceState = MutableStateFlow(VoiceState.DENIED)
    val voiceState = _voiceState.asStateFlow()

    @SuppressLint("MissingPermission")
    fun controlAction() {
        when(voiceState.value) {
            VoiceState.INACTIVE -> {
                startRecording()
                _voiceState.update { VoiceState.RECORDING }
            }
            VoiceState.RECORDING -> {
                pauseRecording()
                _voiceState.update { VoiceState.PAUSED }
            }
            VoiceState.PAUSED -> {
                resumeRecording()
                _voiceState.update { VoiceState.RECORDING }
            }
            VoiceState.STORED -> {
                _voiceState.update { VoiceState.INACTIVE }
                // TODO: Delete recording, reset
            }
            VoiceState.DENIED -> {
                // TODO: Re-launch permissions
            }
            else -> {}
        }
    }

    fun mediaAction() {
        when(voiceState.value) {
            VoiceState.PAUSED,
            VoiceState.RECORDING -> {
                finishRecording()
                _voiceState.update { VoiceState.STORED }
            }
            VoiceState.STORED -> {
                _voiceState.update { VoiceState.SYNCING }
                // TODO: Upload and delete the recording
            }
            else -> {}
        }
    }

    fun handlePermissionUpdate() {
        _voiceState.update { VoiceState.INACTIVE }
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun startRecording() {
        recordingJob = viewModelScope.launch {
            record(voiceRecorder)
        }
    }

    private fun pauseRecording() {
        voiceRecorder.pauseRecording()
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun resumeRecording() {
        voiceRecorder.resumeRecording()
    }

    private fun finishRecording() {
        recordingJob?.cancel()
        println(temp)
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private suspend fun record(voiceRecorder: VoiceRecorder) {
        coroutineScope {
            launch { voiceRecorder.startRecording() }
        }
    }
}