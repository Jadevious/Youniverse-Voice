package uk.youniverse.youniverse_voice.presentation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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