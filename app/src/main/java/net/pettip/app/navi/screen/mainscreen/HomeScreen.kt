package net.pettip.app.navi.screen.mainscreen

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import net.pettip.app.navi.activity.CameraActivity
import net.pettip.app.navi.activity.MainActivity
import net.pettip.app.navi.componet.CustomPagerWithOffset
import net.pettip.app.navi.componet.Linear
import net.pettip.app.navi.screen.Screen
import net.pettip.app.navi.utils.function.shimmerLoadingAnimation

/**
 * @Project     : PetTip-Android
 * @FileName    : WalkScreen
 * @Date        : 2024-04-30
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.mainscreen
 * @see net.pettip.app.navi.screen.mainscreen.WalkScreen
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    val pagerState1 = rememberPagerState(pageCount = {10})
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ){

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ){
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawPath(
                path = Path().apply {
                    moveTo(0f, canvasHeight)
                    cubicTo(
                        x1 = canvasWidth * 0.6f, y1 = canvasHeight,
                        x2 = canvasWidth * 0.8f, y2 = canvasHeight * 0.8f,
                        x3 = canvasWidth, y3 = 0f
                    )
                },
                style = Stroke(),
                color = Color(0xFFE97BBC),
            )
        }

        /**
         * pageSize : 화면 전체의 50%
         * pageSpacing : 화면 전체의 10% 만큼 겹치기. 세번째 페이지가 20%가량 보이기
         */

        Column (
            modifier =Modifier.padding(innerPadding)
        ){
            CustomPagerWithOffset(
                pagerState = pagerState1,
                pagingEffect = Linear,
                itemGap = 0.22f
            ){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(16.dp)
                ){
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(54.dp))
                        .border(width = 1.dp, color = Color(0xFFF77D40), shape = RoundedCornerShape(54.dp))
                        .background(Color.White, RoundedCornerShape(54.dp))
                    ){

                    }
                }
            }
        }

        Column {
            Button(
                onClick = {
                    //navController.navigate(Screen.CameraScreen.route)
                    val intent = Intent(context,CameraActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Text(text = "카메라")
            }
        }

    }
}


