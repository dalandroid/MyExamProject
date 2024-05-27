package net.pettip.app.navi.componet

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

/**
 * @Project     : PetTip-Android
 * @FileName    : ImageItem
 * @Date        : 2024-05-02
 * @author      : CareBiz
 * @description : net.pettip.app.navi.componet
 * @see net.pettip.app.navi.componet.ImageItem
 */
@Composable
fun ImageItem(
    context: Context,
    uri: Uri,
    size: Dp,
    contentScale: ContentScale = ContentScale.Crop,
    filterQuality: FilterQuality = FilterQuality.None,
    onClick:()->Unit = {}
){
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(uri)
            .crossfade(400)
            .build(),
        filterQuality = filterQuality,
    )

    Image(
        painter = painter,
        modifier = Modifier
            .size(size)
            .clickable { onClick() },
        contentDescription = "",
        contentScale = contentScale
    )
}