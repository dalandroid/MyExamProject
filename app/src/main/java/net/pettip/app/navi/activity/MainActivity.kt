package net.pettip.app.navi.activity

import android.app.PictureInPictureParams
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import net.pettip.app.navi.componet.GlobalLoadingScreen
import net.pettip.app.navi.screen.MyApp
import net.pettip.app.navi.screen.Screen
import net.pettip.app.navi.ui.theme.NaviTheme
import net.pettip.app.navi.utils.service.LocationService
import net.pettip.app.navi.viewmodel.login.LoginViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition{false}

        Log.d("INTENT","onCreate")

        enableEdgeToEdge()

        setContent {

            navController = rememberNavController()

            NaviTheme {
                Surface {
                    MyApp(navController = navController)
                    GlobalLoadingScreen()
                }
            }
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        Log.d("INTENT","newIntent")

        handleIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        /** 앱을 끄면 service 도 같이 종료되게끔 **/
        val intent = Intent(this, LocationService::class.java)
        this.stopService(intent)
    }

    private fun handleIntent(intent: Intent) {
        // 인텐트의 액션 또는 데이터를 기반으로 원하는 컴포저블로 이동
        val action = intent.action
        if (action == "GPX_OPEN" && navController.currentDestination?.route != Screen.TestWalkScreen.route) {
            navController.navigate(Screen.TestWalkScreen.route)
        }
        // 추가 액션 처리가 필요한 경우 여기에 추가
    }

}

