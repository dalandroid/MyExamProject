package net.pettip.app.navi.activity

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.Rational
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import net.pettip.app.navi.screen.map.camera.CameraScreen
import net.pettip.app.navi.ui.theme.NaviTheme
import net.pettip.app.navi.utils.service.RecordingService
import net.pettip.app.navi.utils.singleton.MySharedPreference

/**
 * @Project     : PetTip-Android
 * @FileName    : CameraActivity
 * @Date        : 2024-06-04
 * @author      : CareBiz
 * @description : net.pettip.app.navi.activity
 * @see net.pettip.app.navi.activity.CameraActivity
 */
@AndroidEntryPoint
class CameraActivity : ComponentActivity() {

    companion object {
        const val REQUEST_CODE_OVERLAY_PERMISSION = 1
        const val REQUEST_CODE_RECORD_AUDIO_PERMISSION = 2
    }

    private var isInPiPMode = mutableStateOf(false)
    private lateinit var overlayPermissionLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, RecordingService::class.java)
        stopService(intent)

        overlayPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "권한을 허용되었습니다", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "권한을 허용해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(Color.Black.toArgb(), Color.Black.toArgb())
        )

        setContent {
            NaviTheme {
                Surface {
                    CameraScreen(
                        enterPIPMode = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val aspectRatio = Rational(3, 4)
                                val pipParams = PictureInPictureParams.Builder()
                                    .setAspectRatio(aspectRatio)
                                    .build()
                                this.enterPictureInPictureMode(pipParams)
                            }
                        },
                        exitActivity = {
                            this.finish()
                        },
                        isInPiPMode = isInPiPMode.value,
                        requestPermission = {
                            checkAndRequestPermissions()
                        }
                    )
                }
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (MySharedPreference.getServiceRunning()) {
            val aspectRatio = Rational(3, 4)

            enterPictureInPictureMode(
                PictureInPictureParams
                    .Builder()
                    .setAspectRatio(aspectRatio)
                    .build()
            )
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        isInPiPMode.value = isInPictureInPictureMode
    }

    override fun onDestroy() {
        super.onDestroy()
        MySharedPreference.setServiceRunning(false)
    }

    override fun onStop() {
        super.onStop()
        Log.d("LIFE", "onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LIFE", "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LIFE", "onResume")
    }

    private fun checkAndRequestPermissions() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        overlayPermissionLauncher.launch(intent)
    }

    private fun startRecordingService() {
        val intent = Intent(this, RecordingService::class.java)
        startForegroundService(intent)
    }
}