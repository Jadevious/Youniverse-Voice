package uk.youniverse.youniverse_voice.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
fun VoiceApp(
    voiceViewModel :VoiceViewModel,
    enforcePermissionStatus: (String) -> Unit
) {
    val voiceState by voiceViewModel.voiceState.collectAsStateWithLifecycle()



    VoiceUI(
        voiceState,
        voiceViewModel::controlAction,
        voiceViewModel::mediaAction,
        Modifier.fillMaxSize()
    )
}

// Taken from WearSpeakerSample (https://github.com/android/wear-os-samples/tree/main)
private tailrec fun Context.findActivity(): Activity =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> throw IllegalStateException(
            "findActivity should be called in the context of an Activity"
        )
    }
//


// UI Preview
private class VoicePreviewProvider : CollectionPreviewParameterProvider<VoiceState>(
    listOf(
        VoiceState.DENIED,
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
    @PreviewParameter(VoicePreviewProvider::class) voiceState: VoiceState
) {
    VoiceUI(
        state = voiceState,
        toggleActionState = {},
        sync = {},
        Modifier.fillMaxSize()
    )
}