package net.pettip.app.navi.screen.login

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import net.pettip.app.navi.utils.singleton.MySharedPreference

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
    init:()->Unit,
    noInit:()->Unit
){
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = Color.White)

    var firstStep by remember{ mutableStateOf(false) }
    var secondStep by remember{ mutableStateOf(false) }
    var thirdStep by remember{ mutableStateOf(false) }

    LaunchedEffect(Unit){
        delay(300) // 여기에 실제 통신 코드를 추가
        firstStep = true

        // 두 번째 비동기 작업
        delay(300) // 여기에 실제 통신 코드를 추가
        secondStep = true

        // 세 번째 비동기 작업
        delay(300) // 여기에 실제 통신 코드를 추가
        thirdStep = true

        if (!MySharedPreference.getIsInit()){
            init()
        }else{
            noInit()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        val animatedFloat by animateFloatAsState(
            targetValue = when {
                thirdStep -> 1f
                secondStep -> 0.66f
                firstStep -> 0.33f
                else -> 0f
            },
            label = "LoadingState",
            animationSpec = tween(250)
        )

        LinearProgressIndicator(
            progress = { animatedFloat },
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth(),
            color = Color.Blue,
            trackColor = Color.LightGray,
            strokeCap = StrokeCap.Square
        )
    }
}