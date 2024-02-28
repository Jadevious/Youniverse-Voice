/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package uk.youniverse.youniverse_voice.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    private val voiceViewModel: VoiceViewModel by viewModels()

    // Configuring permission launcher w/ callback
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            voiceViewModel.handlePermissionUpdate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        checkPermissionStatus(Manifest.permission.RECORD_AUDIO)

        setContent {
            VoiceApp(
                voiceViewModel,
                enforcePermissionStatus = {permission -> checkPermissionStatus(permission)}
            )
        }
    }

    private fun checkPermissionStatus(permission: String) {
        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
                voiceViewModel.handlePermissionUpdate()
            }
            else -> {
                // Otherwise request the permission
                requestPermissionLauncher.launch(permission)
            }
        }
    }
}
