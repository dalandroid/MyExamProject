package net.pettip.app.navi.screen.test

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import net.pettip.app.navi.componet.CustomPagerWithOffset
import net.pettip.app.navi.componet.Linear

/**
 * @Project     : PetTip-Android
 * @FileName    : TestPagerScreen
 * @Date        : 2024-06-14
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.test
 * @see net.pettip.app.navi.screen.test.TestPagerScreen
 */
@Composable
fun TestPagerScreen(){

    val pagerState = rememberPagerState(pageCount = {10})
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding(),
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


        CustomPagerWithOffset(
            pagerState = pagerState,
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
}