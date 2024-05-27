package net.pettip.app.navi.utils.function

import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextUtils
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.vector.PathParser

/**
 * @Project     : PetTip-Android
 * @FileName    : PathStringToPath
 * @Date        : 2024-05-22
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.function
 * @see net.pettip.app.navi.utils.function.PathStringToPath
 */
fun String?.toPath(size: Size?, pathDestination: Path? = null): Path? {
    this?.let {
        size?.let {
            if (!TextUtils.isEmpty(this)) {
                val pathDestinationResult = pathDestination ?: kotlin.run {
                    Path()
                }
                val scaleMatrix = Matrix()
                val rectF = RectF()
                val path =
                    PathParser().parsePathString(this)
                        .toPath(pathDestinationResult)
                val bounds = path.getBounds()
                val rectPath = Rect(bounds.left.toInt(), bounds.top.toInt(), bounds.right.toInt(), bounds.bottom.toInt())
                val scaleXFactor = size.width / rectPath.width().toFloat()
                val scaleYFactor = size.height / rectPath.height().toFloat()
                val androidPath = path.asAndroidPath()
                scaleMatrix.setScale(scaleXFactor, scaleYFactor, rectF.centerX(), rectF.centerY())
                androidPath.computeBounds(rectF, true)
                androidPath.transform(scaleMatrix)
                return androidPath.asComposePath()
            }
        }
    }
    return null
}