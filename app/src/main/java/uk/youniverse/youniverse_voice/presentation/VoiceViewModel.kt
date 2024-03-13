package uk.youniverse.youniverse_voice.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_AUTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File



class VoiceViewModel(application: Application) : AndroidViewModel(application) {

    private val appDirectory = application.applicationContext.filesDir

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
                uploadRecording()
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
        // Logging file output for debugging purposes
        println(appDirectory)
    }

    private fun uploadRecording() {
        val file = File(appDirectory, FILE_NAME)
        val requestBody = RequestBody.create("audio/opus".toMediaTypeOrNull(), file)
        val filePart = MultipartBody.Part.createFormData("AudioFile", file.name, requestBody)

        // Make the network request
        val call = ApiClient.apiService.UploadEntry(filePart)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _voiceState.update { VoiceState.INACTIVE }
                } else {
                    _voiceState.update { VoiceState.STORED }
                    // Failure
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _voiceState.update { VoiceState.STORED }
                // Error
            }
        })
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private suspend fun record(voiceRecorder: VoiceRecorder) {
        coroutineScope {
            launch { voiceRecorder.startRecording() }
        }
    }
}