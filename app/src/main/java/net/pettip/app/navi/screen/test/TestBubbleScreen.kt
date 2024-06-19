package net.pettip.app.navi.screen.test

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.exyte.animatednavbar.utils.toPxf
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * @Project     : PetTip-Android
 * @FileName    : TestBubbleScreen
 * @Date        : 2024-06-17
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.test
 * @see net.pettip.app.navi.screen.test.TestBubbleScreen
 */
@Composable
fun TestBubbleScreen(){

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    var sliderPosition by remember { mutableFloatStateOf(0.15f) }

    val bubbles = listOf(
        BubbleData("1", "1,234,567", (screenWidth*0.3f).dp),
        BubbleData("2", "500,000", (screenWidth*0.20f).dp),
        BubbleData("3", "234", (screenWidth*0.23f).dp),
        BubbleData("4", "300,000", (screenWidth*0.27f).dp),
        BubbleData("5", "100,000", (screenWidth*0.25f).dp),
        BubbleData("6", "200,000", (screenWidth*sliderPosition).dp),
        BubbleData("7", "134,333", (screenWidth*0.20).dp)
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)){
        BubbleScreen(bubbles = bubbles)

        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0.15f..0.3f,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }
}

data class BubbleData(val title: String, val count: String, val size: Dp)

@Composable
fun BubbleScreen(bubbles: List<BubbleData>) {
    
    val angleInDegrees1 = 10
    val angleInRadians1 = Math.toRadians(angleInDegrees1.toDouble())
    val cosValue1 = cos(angleInRadians1).toFloat()
    val sinValue1 = sin(angleInRadians1).toFloat()

    val angleInDegrees2 = -15
    val angleInRadians2 = Math.toRadians(angleInDegrees2.toDouble())
    val cosValue2 = cos(angleInRadians2).toFloat()
    val sinValue2 = sin(angleInRadians2).toFloat()

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        /** 1번원 offset */
        val firstBubbleOffsetX = (-LocalConfiguration.current.screenWidthDp / 2).dp + bubbles[0].size / 2
        val firstBubbleOffsetY = 0.dp

        /** 2번원 offset */
        val secondBubbleRadiusSum = (bubbles[0].size + bubbles[1].size) / 2
        val secondBubbleOffsetX = firstBubbleOffsetX + secondBubbleRadiusSum * cosValue1
        val secondBubbleOffsetY = firstBubbleOffsetY + secondBubbleRadiusSum * sinValue1

        /** 3번원 offset */
        val thirdBubbleRadiusSum = (bubbles[1].size + bubbles[2].size) / 2
        val thirdBubbleOffsetX = secondBubbleOffsetX + thirdBubbleRadiusSum
        val thirdBubbleOffsetY = secondBubbleOffsetY

        /** 4번원 offset */
        val fourthBubbleRadiusSum = (bubbles[2].size + bubbles[3].size) / 2
        val fourthBubbleOffsetX = thirdBubbleOffsetX + fourthBubbleRadiusSum * cosValue2
        val fourthBubbleOffsetY = thirdBubbleOffsetY + fourthBubbleRadiusSum * sinValue2

        /** 5번원 offset */
        val a = (bubbles[0].size + bubbles[4].size).value / 2
        val b = (bubbles[1].size + bubbles[4].size).value / 2
        val dx = (secondBubbleOffsetX - firstBubbleOffsetX).value
        val dy = (secondBubbleOffsetY - firstBubbleOffsetY).value
        val c = sqrt(dx * dx + dy * dy)

        val x = (a * a - b * b + c * c) / (2 * c)
        val h = sqrt(a * a - x * x)

        val angle = atan2(dy, dx)

        val fifthBubbleOffsetX = firstBubbleOffsetX + x.dp * cos(angle) + h.dp * sin(angle)
        val fifthBubbleOffsetY = firstBubbleOffsetY + x.dp * sin(angle) - h.dp * cos(angle)

        /** 6번원 offset */
        val a6 = (bubbles[4].size + bubbles[5].size).value / 2
        val b6 = (bubbles[2].size + bubbles[5].size).value / 2
        val dx6 = (thirdBubbleOffsetX - fifthBubbleOffsetX).value
        val dy6 = (thirdBubbleOffsetY - fifthBubbleOffsetY).value
        val c6 = sqrt(dx6 * dx6 + dy6 * dy6)
        val x6 = (a6.pow(2) - b6.pow(2) + c6.pow(2)) / (2 * c6)
        val h6 = sqrt(a6.pow(2) - x6.pow(2))
        val angle6 = atan2(dy6, dx6)

        val sixthBubbleOffsetX = fifthBubbleOffsetX + (x6 * cos(angle6) + h6 * sin(angle6)).dp
        val sixthBubbleOffsetY = fifthBubbleOffsetY + (x6 * sin(angle6) - h6 * cos(angle6)).dp

        /** 7번원 offset */
        val a7 = (bubbles[5].size + bubbles[6].size).value / 2
        val b7 = (bubbles[3].size + bubbles[6].size).value / 2
        val dx7 = (fourthBubbleOffsetX - sixthBubbleOffsetX).value
        val dy7 = (fourthBubbleOffsetY - sixthBubbleOffsetY).value
        val c7 = sqrt(dx7 * dx7 + dy7 * dy7)
        val x7 = (a7.pow(2) - b7.pow(2) + c7.pow(2)) / (2 * c7)
        val h7 = sqrt(a7.pow(2) - x7.pow(2))
        val angle7 = atan2(dy7, dx7)

        val seventhBubbleOffsetX = sixthBubbleOffsetX + (x7 * cos(angle7) + h7 * sin(angle7)).dp
        val seventhBubbleOffsetY = sixthBubbleOffsetY + (x7 * sin(angle7) - h7 * cos(angle7)).dp

        BubbleItem(
            offsetX = firstBubbleOffsetX,
            offsetY = firstBubbleOffsetY,
            bubbleData = bubbles[0]
        )

        BubbleItem(
            offsetX = secondBubbleOffsetX,
            offsetY = secondBubbleOffsetY,
            bubbleData = bubbles[1]
        )

        BubbleItem(
            offsetX = thirdBubbleOffsetX,
            offsetY = thirdBubbleOffsetY,
            bubbleData = bubbles[2]
        )

        BubbleItem(
            offsetX = fourthBubbleOffsetX,
            offsetY = fourthBubbleOffsetY,
            bubbleData = bubbles[3]
        )

        BubbleItem(
            offsetX = fifthBubbleOffsetX,
            offsetY = fifthBubbleOffsetY,
            bubbleData = bubbles[4]
        )

        BubbleItem(
            offsetX = sixthBubbleOffsetX,
            offsetY = sixthBubbleOffsetY,
            bubbleData = bubbles[5]
        )

        BubbleItem(
            offsetX = seventhBubbleOffsetX,
            offsetY = seventhBubbleOffsetY,
            bubbleData = bubbles[6]
        )
    }
}

@Composable
fun BubbleItem(
    offsetX: Dp,
    offsetY: Dp,
    bubbleData: BubbleData
){
    val mutableInteractionSource = remember {
        MutableInteractionSource()
    }
    val pressed by mutableInteractionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed){
            1.2f
        }else{
            1.0f
        }, label = ""
    )

    Box(modifier = Modifier
        .offset(x = offsetX, y = offsetY)
        .size(bubbleData.size)
        .graphicsLayer {
            this.scaleX = scale
            this.scaleY = scale
        }
        .clip(CircleShape)
        .zIndex(scale)
        .background(Color.Blue)
        .border(1.dp, Color.Black, CircleShape)
        .clickable(
            interactionSource = mutableInteractionSource,
            indication = rememberRipple(bounded = false),
            onClick = {}
        ),
        contentAlignment = Alignment.Center
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = bubbleData.title,
                fontSize = 14.sp,
                color = Color.White
            )

            Text(
                text = bubbleData.count,
                fontSize = 10.sp,
                color = Color.White
            )
        }
    }
}