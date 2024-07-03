package net.pettip.app.navi.screen.login

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import net.pettip.app.navi.screen.Screen
import net.pettip.app.navi.utils.singleton.MySharedPreference

/**
 * @Project     : PetTip-Android
 * @FileName    : SplashScreen
 * @Date        : 2024-06-21
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.login
 * @see net.pettip.app.navi.screen.login.SplashScreen
 */
@Composable
fun SplashScreen(navController: NavHostController) {

    val pagerState = rememberPagerState (initialPage = 0,pageCount = {3})

    LaunchedEffect(Unit) {
        while (pagerState.currentPage != 2){
            delay(2000)
            pagerState.animateScrollToPage(
                page = pagerState.currentPage+1,
                animationSpec = tween(700)
            )
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .safeContentPadding(),
        contentAlignment = Alignment.Center
    ){
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = true,
            pageSize = PageSize.Fill
        ) {page ->
            when(page){
                0 -> FirstSplash()
                1 -> SecondSplash()
                2 -> ThirdSplash(navController = navController)
            }
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
    }
}

@Composable
fun FirstSplash(){
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "First Splash")
    }
}

@Composable
fun SecondSplash(){
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Second Splash")
    }
}

@Composable
fun ThirdSplash(navController: NavHostController){
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Button(
            onClick = {
                MySharedPreference.setIsInit(false)
                navController.navigate(Screen.MainScreen.route){ popUpTo(0)}
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(
                text = "메인으로",
                color = Color.White
            )
        }
    }
}