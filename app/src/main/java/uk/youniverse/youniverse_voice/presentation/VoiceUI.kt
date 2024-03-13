package uk.youniverse.youniverse_voice.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults

@Composable
fun VoiceUI(
    state: VoiceState,
    toggleActionState: () -> Unit,
    sync: () -> Unit,
    modifier: Modifier = Modifier
) {
    MaterialTheme {
        Scaffold(
            timeText = {
                TimeText(
                    timeTextStyle = TimeTextDefaults.timeTextStyle(
                        fontSize = 10.sp
                    )
                )
            }
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when (state) {
                        VoiceState.INACTIVE -> "Ready."
                        VoiceState.PAUSED -> "Paused."
                        VoiceState.RECORDING -> "Listening..."
                        VoiceState.STORED -> "Saved. \nUpload?"
                        VoiceState.SYNCING -> "Syncing..."
                        VoiceState.DENIED -> "Permission \nRequired"
                        else -> ""
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = toggleActionState,
                        enabled = (state != VoiceState.SYNCING)) {
                        Icon(
                            imageVector = when (state) {
                                VoiceState.INACTIVE,
                                VoiceState.DENIED,
                                VoiceState.PAUSED -> Icons.Default.Mic
                                VoiceState.RECORDING -> Icons.Default.Pause
                                VoiceState.STORED -> Icons.Default.DeleteForever
                                else -> Icons.Default.Mic
                            },
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = sync,
                        enabled = !(
                                state == VoiceState.INACTIVE
                                || state == VoiceState.DENIED
                                || state == VoiceState.SYNCING),
                    ) {
                        Icon(
                            imageVector = when (state) {
                                VoiceState.INACTIVE,
                                VoiceState.DENIED -> Icons.Default.CloudUpload
                                VoiceState.PAUSED,
                                VoiceState.RECORDING -> Icons.Default.StopCircle
                                else -> Icons.Default.CloudUpload
                            },
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }

}