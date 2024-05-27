package net.pettip.app.navi.utils.function

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape

/**
 * @Project     : PetTip-Android
 * @FileName    : CustomShape
 * @Date        : 2024-05-23
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.function
 * @see net.pettip.app.navi.utils.function.CustomShape
 */
fun customShape(round:Float=200f): Shape {
    return GenericShape { size, _ ->
        // Define the custom path
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - round/2)
            quadraticTo(size.width / 2, size.height+round/2, 0f, size.height - round/2)
            close()
        }
        addPath(path)
    }
}