package uk.youniverse.youniverse_voice.presentation

import android.content.res.Configuration
import android.speech.tts.Voice
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition

@Composable
fun VoiceApp() {
    val viewModel = viewModel<VoiceViewModel>()
    val voiceState by viewModel.voiceState.collectAsStateWithLifecycle()

    lateinit var permissionLauncher: ManagedActivityResultLauncher<String, Boolean>

    VoiceUI(
        voiceState,
        viewModel::controlAction,
        viewModel::mediaAction,
        Modifier.fillMaxSize()
    )
}

private class PlaybackStatePreviewProvider : CollectionPreviewParameterProvider<VoiceState>(
    listOf(
        VoiceState.INACTIVE,
        VoiceState.RECORDING,
        VoiceState.PAUSED,
        VoiceState.STORED
    )
)

@Preview(
    device = Devices.WEAR_OS_SMALL_ROUND,
    showSystemUi = true,
    widthDp = 200,
    heightDp = 200,
    uiMode = Configuration.UI_MODE_TYPE_WATCH
)
@Composable
fun VoiceUIPreview(
    @PreviewParameter(PlaybackStatePreviewProvider::class) voiceState: VoiceState
) {
    VoiceUI(
        state = voiceState,
        toggleActionState = {},
        sync = {},
        Modifier.fillMaxSize()
    )
}