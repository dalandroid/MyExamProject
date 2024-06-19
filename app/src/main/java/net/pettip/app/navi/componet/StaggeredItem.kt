package net.pettip.app.navi.componet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import net.pettip.app.navi.R
import net.pettip.app.navi.datamodel.data.test.RTStoryData

/**
 * @Project     : PetTip-Android
 * @FileName    : StaggeredItem
 * @Date        : 2024-06-17
 * @author      : CareBiz
 * @description : net.pettip.app.navi.componet
 * @see net.pettip.app.navi.componet.StaggeredItem
 */
@Composable
fun StaggeredItem(
    data : RTStoryData
){

    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black),
        startY = sizeImage.height.toFloat()/5*3,
        endY = sizeImage.height.toFloat()
    )

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .onGloballyPositioned {
                sizeImage = it.size
            }
    ){
        AsyncImage(
            model = data.storyFile,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        /** 배경 그라디언트 */
        Box(modifier = Modifier
            .matchParentSize()
            .background(gradient))

        Column (modifier= Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .align(Alignment.BottomCenter)
            .padding(bottom = 16.dp)
            .background(color = Color.Transparent)
        ){
            /** 타이틀 */
            Text(
                text = data.schTtl?:"",
                fontSize = 18.sp,
                letterSpacing = (-0.9).sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            /** 펫 이름 */
            Text(
                text = data.petNm?:"",
                fontSize = 14.sp,
                letterSpacing = (-0.7).sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.padding(bottom = 16.dp))


            /** 카테고리 영역 */
            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ){
                items(data?.schSeNmList?: emptyList()){ item ->
                    Box(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = item?.cdNm?:"",
                            fontSize = 12.sp, letterSpacing = (-0.6).sp,
                            lineHeight = 12.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}