package net.pettip.app.navi.componet

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.exyte.animatednavbar.utils.noRippleClickable
import com.exyte.animatednavbar.utils.toPxf
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random

/**
 * @Project     : PetTip-Android
 * @FileName    : WaterIcon
 * @Date        : 2024-05-22
 * @author      : CareBiz
 * @description : net.pettip.app.navi.componet
 * @see net.pettip.app.navi.componet.WaterIcon
 */
@Composable
fun WaterIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes movedIcon: Int? = null,
    @DrawableRes icon: Int,
    @DrawableRes backgroundIcon: Int, // 물결 모양이 보일 영역
    contentDescription: String? = null,
    backgroundIconColor: Color = Color.White,
    wiggleColor: Color = Color.Blue,
    outlineColor: Color = Color.White,
    iconSize: Dp = 25.dp,
    percentage: Float
){
    var numberOfClick by remember{ mutableIntStateOf(0) }
    var targetValue by remember{ mutableStateOf(1f) }
    val animatedToWave = remember { Animatable(1f) } // 터치시 물결 효과를 위한 float 값
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .noRippleClickable {
                onClick()
                scope.launch {
                    if (targetValue == 1f){
                        val randomValue = Random.nextFloat()*(1.8f- 1.3f) + 1.3f
                        targetValue = randomValue
                    }else{
                        targetValue = 1f
                    }
                    animatedToWave.animateTo(
                        targetValue = targetValue,
                        animationSpec = tween(durationMillis = 1000)
                    )
                    animatedToWave.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 2000)
                    )
                }
            }
    ) {

        CustomDrawWithBlendMode(
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.Center),
            movedIcon = movedIcon,
            icon = icon,
            backgroundIcon = backgroundIcon,
            wiggleColor = wiggleColor,
            backgroundIconColor = backgroundIconColor,
            outlineColor = outlineColor,
            contentDescription = contentDescription,
            size = iconSize,
            percentage = percentage,
            animatedValue = animatedToWave.value
        )

    }
}

@Composable
fun CustomDrawWithBlendMode(
    modifier: Modifier,
    @DrawableRes movedIcon: Int? = null,
    @DrawableRes icon: Int,
    @DrawableRes backgroundIcon: Int,
    contentDescription: String? = null,
    wiggleColor: Color,
    size: Dp,
    backgroundIconColor: Color,
    outlineColor: Color,
    percentage: Float,
    animatedValue: Float,
) {
    val movedVector = movedIcon?.let { ImageVector.vectorResource(id = it) }
    val movedPainter = movedVector?.let { rememberVectorPainter(image = it) }

    val vector = ImageVector.vectorResource(id = icon)
    val painter = rememberVectorPainter(image = vector)

    val backgroundVector = ImageVector.vectorResource(id = backgroundIcon)
    val backgroundPainter = rememberVectorPainter(image = backgroundVector)

    var canvasSize by remember { mutableStateOf(Size.Zero) }

    val density = LocalDensity.current
    val sizePx = remember(size) { size.toPxf(density) }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Canvas(
        modifier = modifier
            .graphicsLayer(
                alpha = 0.99f,
                scaleX = 1f,
                scaleY = 0.8f
            )
            .fillMaxSize()
            .onGloballyPositioned { canvasSize = it.size.toSize() },
        contentDescription = contentDescription ?: ""
    ) {

        with(backgroundPainter) {
            draw(
                size = Size(sizePx, sizePx),
                colorFilter = ColorFilter.tint(color = backgroundIconColor)
            )
        }

        val path = Path()
        val amplitude = 50f*animatedValue // 진폭
        val frequency = 2f*animatedValue // 주파수

        // 시작점을 설정합니다.
        path.moveTo(0f, canvasSize.height*(100f-percentage)/100f)

        // sin 함수를 사용하여 물결 모양을 그립니다.
        for (x in 0..canvasSize.width.toInt()) {
            val y = canvasSize.height*(100f-percentage)/100f+animatedValue + amplitude * sin((x * frequency * Math.PI / canvasSize.width).toFloat() + waveOffset).toFloat()
            path.lineTo(x.toFloat(), y)
        }

        path.lineTo(x = canvasSize.width, y = canvasSize.height)
        path.lineTo(x = 0f, y = canvasSize.height)
        path.close()

        drawPath(
            path = path,
            color = wiggleColor,
            style = Fill,
            blendMode = BlendMode.SrcIn
        )

        with(painter) {
            draw(
                size = Size(sizePx, sizePx),
                colorFilter = ColorFilter.tint(color = outlineColor)
            )
        }
    }
}