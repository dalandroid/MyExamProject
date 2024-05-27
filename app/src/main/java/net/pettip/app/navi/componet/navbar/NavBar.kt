package com.exyte.animatednavbar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.exyte.animatednavbar.animation.balltrajectory.BallAnimInfo
import com.exyte.animatednavbar.animation.balltrajectory.BallAnimation
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.balltrajectory.Straight
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.IndentAnimation
import com.exyte.animatednavbar.animation.indendshape.ShapeCornerRadius
import com.exyte.animatednavbar.animation.indendshape.StraightIndent
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.layout.animatedNavBarMeasurePolicy
import com.exyte.animatednavbar.utils.ballTransform
import com.exyte.animatednavbar.utils.toPxf
import net.pettip.app.navi.screen.mainscreen.BottomNav

/**
 *A composable function that creates an animated navigation bar with a moving ball and indent
 * to indicate the selected item.
 *
 *@param [modifier] Modifier to be applied to the navigation bar
 *@param [selectedIndex] The index of the currently selected item
 *@param [barColor] The color of the navigation bar
 *@param [ballColor] The color of the moving ball
 *@param [cornerRadius] The corner radius of the navigation bar
 *@param [ballAnimation] The animation to be applied to the moving ball
 *@param [indentAnimation] The animation to be applied to the navigation bar to indent selected item
 *@param [content] The composable content of the navigation bar
 */

@Composable
fun AnimatedNavigationBar(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    barColor: Color = Color.White,
    ballColor: Color = Color.Black,
    cornerRadius: ShapeCornerRadius = shapeCornerRadius(0f),
    ballAnimation: BallAnimation = Straight(tween(300)),
    indentAnimation: IndentAnimation = StraightIndent(tween(300)),
    horizontalPadding : Dp = 0.dp,
    ballOffset:Dp = (-20).dp,
    bottomNavItem: List<BottomNav>,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current

    var itemPositions by remember { mutableStateOf(listOf<Offset>()) }
    val measurePolicy = animatedNavBarMeasurePolicy {
        itemPositions = it.map { xCord ->
            Offset(xCord+horizontalPadding.toPxf(density), 0f)
        }
    }



    val selectedItemOffset by remember(selectedIndex, itemPositions) {
        derivedStateOf {
            if (itemPositions.isNotEmpty()) itemPositions[selectedIndex] else Offset.Unspecified
        }
    }

    val indentShape = indentAnimation.animateIndentShapeAsState(
        shapeCornerRadius = cornerRadius,
        targetOffset = selectedItemOffset
    )

    val ballAnimInfoState = ballAnimation.animateAsState(
        targetOffset = selectedItemOffset,
    )

    Box(
        modifier = modifier.background(Color.Transparent)
    ) {
        Layout(
            modifier = Modifier
                .graphicsLayer {
                    clip = true
                    shape = indentShape.value
                }
                .background(color = barColor, shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .padding(horizontal = horizontalPadding),
            content = content,
            measurePolicy = measurePolicy
        )

        if (ballAnimInfoState.value.offset.isSpecified) {
            ColorBall(
                ballAnimInfo = ballAnimInfoState.value,
                ballColor = ballColor,
                sizeDp = ballSize,
                modifier = Modifier.offset(y = ballOffset),
                bottomNavItem = bottomNavItem,
                selectedIndex = selectedIndex
            )
        }
    }
}

val ballSize = 54.dp

@Composable
private fun ColorBall(
    modifier: Modifier = Modifier,
    ballColor: Color,
    ballAnimInfo: BallAnimInfo,
    sizeDp: Dp,
    bottomNavItem: List<BottomNav>,
    selectedIndex: Int
) {
    Box(
        modifier = modifier
            .ballTransform(ballAnimInfo)
            .size(sizeDp)
            .shadow(elevation = 2.dp, shape = CircleShape, clip = true)
            .clip(shape = CircleShape)
            .background(ballColor),
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = ImageVector.vectorResource(id = bottomNavItem[selectedIndex].selectedIcon),
            contentDescription = "",
            tint = Color(0xFFF77D40)
        )
    }
}
