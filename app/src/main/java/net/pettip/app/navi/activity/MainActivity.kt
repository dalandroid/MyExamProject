package net.pettip.app.navi.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import net.pettip.app.navi.componet.GlobalLoadingScreen
import net.pettip.app.navi.screen.MyApp
import net.pettip.app.navi.ui.theme.NaviTheme

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

