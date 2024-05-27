package net.pettip.app.navi.componet

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * @Project     : PetTip-Android
 * @FileName    : AnimatedCircleLoading
 * @Date        : 2024-05-23
 * @author      : CareBiz
 * @description : net.pettip.app.navi.componet
 * @see net.pettip.app.navi.componet.AnimatedCircleLoading
 */
@Composable
fun AnimatedCircleLoading(
    duration:Int = 50,
    numberOfDots : Int = 9,
    startColor:Color = Color.Gray,
    targetColor:Color = Color.LightGray,
    circleRadius: Dp = 80.dp
){
    val angleStep = 360f / numberOfDots

    // Remember individual dot animations
    val dotColorAnimations = remember {
        List(numberOfDots) {
            Animatable(startColor)
        }
    }
    val dotSizeAnimations = remember {
        List(numberOfDots) {
            androidx.compose.animation.core.Animatable(1.2f)
        }
    }

    // Launch animations
    LaunchedEffect(Unit) {
        dotColorAnimations.forEachIndexed { index, animatable ->
            launch {
                delay(index * duration.toLong())
                animatable.animateTo(
                    targetValue = targetColor,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = numberOfDots * duration, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
        }
        dotSizeAnimations.forEachIndexed { index, animatable ->
            launch {
                delay(index * duration.toLong())
                dotSizeAnimations[index].animateTo(
                    targetValue = 1f, // 1.5 times the original size (6.dp * 1.5)
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = numberOfDots * duration, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
        }

    }

    Box(
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .size(circleRadius)
        ) {
            val radius = size.minDimension / 2

            for (i in 0 until numberOfDots) {
                val angle = angleStep * i
                val x = radius * cos(Math.toRadians(angle.toDouble())).toFloat()
                val y = radius * sin(Math.toRadians(angle.toDouble())).toFloat()
                drawCircle(
                    color = dotColorAnimations[i].value,
                    radius = 5.dp.toPx() * dotSizeAnimations[i].value,
                    center = center + Offset(x, y)
                )
            }
        }
    }
}