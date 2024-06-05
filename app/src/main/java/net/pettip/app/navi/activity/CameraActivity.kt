package net.pettip.app.navi.activity

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
class CameraActivity :ComponentActivity(){
    private var isInPiPMode = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this,RecordingService::class.java)
        stopService(intent)

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(Color.Black.toArgb(), Color.Black.toArgb())
        )

        setContent {
            NaviTheme {
                Surface {
                    CameraScreen(
                        enterPIPMode = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val aspectRatio = Rational(16, 9)
                                val pipParams = PictureInPictureParams.Builder()
                                    .setAspectRatio(aspectRatio)
                                    .build()
                                this.enterPictureInPictureMode(pipParams)
                            }
                        },
                        exitActivity = {
                            this.finish()
                        },
                        isInPiPMode = isInPiPMode.value
                    )
                }
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (MySharedPreference.getServiceRunning()){
            enterPictureInPictureMode(PictureInPictureParams.Builder().build())
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
}