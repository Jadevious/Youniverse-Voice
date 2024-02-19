package uk.youniverse.youniverse_voice.presentation

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
    private val _voiceState = MutableStateFlow(VoiceState.INACTIVE)
    val voiceState = _voiceState.asStateFlow()

    fun controlAction() {
        when(voiceState.value) {
            VoiceState.INACTIVE,
            VoiceState.PAUSED -> _voiceState.update { VoiceState.RECORDING }
            VoiceState.RECORDING -> _voiceState.update { VoiceState.PAUSED }
            VoiceState.STORED -> {
                _voiceState.update { VoiceState.INACTIVE }
                // TODO: Delete recording, reset
            }
            else -> {}
        }
    }

    fun mediaAction() {
        when(voiceState.value) {
            VoiceState.PAUSED,
            VoiceState.RECORDING -> _voiceState.update { VoiceState.STORED }
            VoiceState.STORED -> {
                _voiceState.update { VoiceState.SYNCING }
                // TODO: Delete recording, reset
            }
            else -> {}
        }
    }
}