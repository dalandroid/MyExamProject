package net.pettip.app.navi.componet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @Project     : PetTip-Android
 * @FileName    : CustomPagerWithOffset
 * @Date        : 2024-05-13
 * @author      : CareBiz
 * @description : net.pettip.app.navi.componet.navbar
 * @see net.pettip.app.navi.componet.navbar.CustomPagerWithOffset
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomPagerWithOffset(
    pagerState:PagerState,
    pagingEffect: PagingEffect = Square,
    pagerHeight: Dp = 300.dp,
    pageSize:Float = 0.5f,
    pageSpacing:Float = 0.1f,
    itemGap:Float = 0.2f,
    content: @Composable () -> Unit
){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(pagerHeight),
        beyondViewportPageCount = 2,
        pageSize = PageSize.Fixed((screenWidth*pageSize).dp),
        pageSpacing = -(screenWidth*pageSpacing).dp,
        flingBehavior = PagerDefaults.flingBehavior(
            state = pagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(10)
        ),
        contentPadding = PaddingValues(end = 30.dp)
    ) {
        Box(modifier = Modifier
            .width((screenWidth * pageSize).dp)
            .fillMaxHeight()
            .graphicsLayer {
                val pageOffset = -pagerState.getOffsetDistanceInPages(it)
                val yOffset = when(pagingEffect){
                    Linear -> pageOffset
                    Stair -> calculateYOffset(pageOffset)
                    Square -> squareYOffset(pageOffset)
                }
                translationY = if (pageOffset < 0) yOffset * size.height * itemGap else 0f
                alpha = if (pageOffset > 0) 1-pageOffset else 1f
            }
            ,
            contentAlignment = Alignment.BottomCenter
        ){
            content()
        }

    }
}

sealed class PagingEffect

data object Stair : PagingEffect()
data object Linear : PagingEffect()
data object Square : PagingEffect()

fun calculateYOffset(pageOffset: Float): Float {
    return when {
        pageOffset >= -0.5 -> 0f
        pageOffset >= -1.0 -> (pageOffset + 0.5f) * 2
        pageOffset >= -1.5 -> - 1f  // -1.0에서 -1.5 사이는 1로 고정
        pageOffset >= -2.0 -> ((pageOffset + 1.5f) * 2) -1f
        pageOffset >= -2.5 -> - 2f
        pageOffset >= -3.0 -> ((pageOffset + 2.5f) * 2) -2f
        else -> 2f  // 그 외의 경우
    }
}

fun squareYOffset(pageOffset: Float): Float {
    return when {
        pageOffset >= -1.0f -> -(pageOffset * pageOffset)
        pageOffset >= -2.0f -> -((pageOffset + 1.0f)*(pageOffset + 1.0f)) -1f
        pageOffset >= -3.0f -> -((pageOffset + 2.0f)*(pageOffset + 2.0f)) -2f
        pageOffset >= -4.0f -> -((pageOffset + 3.0f)*(pageOffset + 3.0f)) -3f
        else -> -4f  // 그 외의 경우
    }
}