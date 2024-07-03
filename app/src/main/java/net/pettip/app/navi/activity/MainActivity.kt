package net.pettip.app.navi.activity

import android.app.PictureInPictureParams
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.pettip.app.navi.componet.GlobalLoadingScreen
import net.pettip.app.navi.screen.MyApp
import net.pettip.app.navi.ui.theme.NaviTheme
import net.pettip.app.navi.viewmodel.login.LoginViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition{false}

        enableEdgeToEdge()

        setContent {
            NaviTheme {
                Surface {
                    MyApp()
                    GlobalLoadingScreen()
                }
            }
        }
    }
}

