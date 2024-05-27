package net.pettip.app.navi.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

/**
 * @Project     : PetTip-Android
 * @FileName    : IntroScreen
 * @Date        : 2024-04-30
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen
 * @see net.pettip.app.navi.screen.IntroScreen
 */
private const val SplashWaitTime:Long = 2000

@Composable
fun IntroScreen(
    modifier:Modifier = Modifier,
    onLogin:()->Unit,
    unLogin:()->Unit
){
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = Color.White)

    var loginState = false

    LaunchedEffect(Unit){
        delay(SplashWaitTime) // 2초 대기

        if (loginState){
            onLogin()
        }else{
            unLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "INTRO Screen"
        )
    }
}