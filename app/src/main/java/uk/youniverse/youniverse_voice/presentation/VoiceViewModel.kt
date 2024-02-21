package uk.youniverse.youniverse_voice.presentation

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VoiceViewModel : ViewModel() {


    private val _voiceState = MutableStateFlow(VoiceState.DENIED)
    val voiceState = _voiceState.asStateFlow()

    fun controlAction() {
        when(voiceState.value) {
            VoiceState.INACTIVE,
            VoiceState.PAUSED -> {
                _voiceState.update { VoiceState.RECORDING }
                // TODO: Re-enable recording
            }
            VoiceState.RECORDING -> {
                _voiceState.update { VoiceState.PAUSED }
                // TODO: Pause recording
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
                _voiceState.update { VoiceState.STORED }
                // TODO: End/save recording
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
}