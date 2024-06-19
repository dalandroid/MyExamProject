package net.pettip.app.navi.screen.test

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import net.pettip.app.navi.componet.StaggeredItem
import net.pettip.app.navi.viewmodel.TestViewModel
import kotlin.random.Random

/**
 * @Project     : PetTip-Android
 * @FileName    : TestLazyVerticalGrid
 * @Date        : 2024-06-14
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.test
 * @see net.pettip.app.navi.screen.test.TestLazyVerticalGrid
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun TestLazyVerticalGrid(
    viewModel: TestViewModel = hiltViewModel()
){
    val storyList by viewModel.rtStoryList.collectAsState()
    val context = LocalContext.current
    val state = rememberLazyStaggeredGridState()

    LaunchedEffect(Unit){
        viewModel.getRTStoryList()
    }

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(storyList?.data?: emptyList()) { data ->
                    StaggeredItem(data = data)
                }
                item { 
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .border(width = 1.dp, color = Color.Black, shape = RectangleShape),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "+ 더보기",
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

data class ListItem(
    val height: Dp,
    val color:Color
)

@Composable
fun RandomColorBox(item:ListItem){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(item.height)
        .background(item.color)
        .clip(RoundedCornerShape(10.dp))
    )
}

data class ImageInfo(
    val painter:AsyncImagePainter,
    val height: Dp
)


@Composable
fun ImageBox(imageInfo: ImageInfo){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(imageInfo.height)
        .clip(RoundedCornerShape(10.dp))
    ){
        Image(
            painter = imageInfo.painter,
            contentDescription = "" ,
            contentScale = ContentScale.FillWidth
        )
    }
}