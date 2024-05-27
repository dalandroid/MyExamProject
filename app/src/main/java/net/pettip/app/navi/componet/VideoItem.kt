package net.pettip.app.navi.componet

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest

/**
 * @Project     : PetTip-Android
 * @FileName    : VedioItem
 * @Date        : 2024-05-02
 * @author      : CareBiz
 * @description : net.pettip.app.navi.componet
 * @see net.pettip.app.navi.componet.VedioItem
 */
@Composable
fun VideoItem(
    context:Context,
    uri: Uri,
    size: Dp,
    contentScale: ContentScale = ContentScale.Crop,
    filterQuality: FilterQuality = FilterQuality.None
){
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(VideoFrameDecoder.Factory())
        }
        .build()

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(uri)
            .crossfade(400)
            .build(),
        imageLoader = imageLoader,
        filterQuality = filterQuality
    )


    Image(
        painter = painter,
        modifier = Modifier.size(size),
        contentDescription ="",
        contentScale = contentScale
    )
}