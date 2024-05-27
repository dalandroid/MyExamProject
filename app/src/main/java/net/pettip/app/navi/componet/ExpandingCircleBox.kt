package net.pettip.app.navi.componet

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @Project     : PetTip-Android
 * @FileName    : CustomFloatingButton
 * @Date        : 2024-05-20
 * @author      : CareBiz
 * @description : net.pettip.app.navi.componet
 * @see net.pettip.app.navi.componet.CustomFloatingButton
 */
@Composable
fun ExpandingCircleBox(
    expanded:Boolean,
    size: Dp = 50.dp,
    paddingValue: PaddingValues,
    modifier: Modifier = Modifier,
    collapsedContent:@Composable ()->Unit,
    expandableContent:@Composable ()->Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val width by animateDpAsState(targetValue = if (expanded) screenWidth - 40.dp else size, label = "")
    val cornerRadius by animateDpAsState(targetValue = if (expanded) 0.dp else size/2, label = "")
    val backgroundColor by animateColorAsState(targetValue = if (expanded) Color.Gray else Color.LightGray, label = "")

    Box(
        modifier = modifier
            .padding(paddingValue)
            .width(width)
            .height(size)
            .background(backgroundColor, shape = RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        if (expanded) {
            // Add any content for expanded state
            expandableContent()
        } else {
            // Add any content for collapsed state
            collapsedContent()
        }
    }
}