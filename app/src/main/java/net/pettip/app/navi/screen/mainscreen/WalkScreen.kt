package net.pettip.app.navi.screen.mainscreen

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.pettip.app.navi.R
import net.pettip.app.navi.componet.AnimatedCircleLoading
import net.pettip.app.navi.componet.ButtonSingleTap
import net.pettip.app.navi.componet.WaterIcon
import net.pettip.app.navi.utils.function.customShape

/**
 * @Project     : PetTip-Android
 * @FileName    : WalkScreen
 * @Date        : 2024-04-30
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.mainscreen
 * @see net.pettip.app.navi.screen.mainscreen.WalkScreen
 */
@Composable
fun WalkScreen(){
    var targetPercentage by remember { mutableFloatStateOf(0f) }

    // 애니메이션 적용된 퍼센트 값
    val percentage by animateFloatAsState(
        targetValue = targetPercentage,
        animationSpec = tween(durationMillis = 1500),
        label = ""
    )

    val lazyListState = rememberLazyListState()
    val gradientColor = Brush.verticalGradient(listOf(Color(0xFFFFA726), Color(0xFFFFD1B8 )))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        state = lazyListState
    ){
        item {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(gradientColor, customShape(400f))
                    .clip(customShape(400f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text ="${percentage.toInt()}%",
                    fontSize = 20.sp,
                    color = Color.White
                )

                WaterIcon(
                    onClick = { Log.d("MAIN","main icon click") },
                    icon = R.drawable.main_icon,
                    backgroundIcon = R.drawable.main_icon_outline,
                    iconSize = 250.dp,
                    backgroundIconColor = Color(0xFFFFA726).copy(alpha = 0.9f),
                    wiggleColor = Color.White,
                    percentage = percentage
                )

                Row (modifier = Modifier.padding(bottom = 30.dp)){
                    ButtonSingleTap(
                        onClick = { targetPercentage -= 10f }
                    ) {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                    }
                    ButtonSingleTap(
                        onClick = { targetPercentage += 10f }
                    ) {
                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "")
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ){
                AnimatedCircleLoading(
                    duration = 100,
                    circleRadius = 60.dp
                )
            }
        }
    }

}

