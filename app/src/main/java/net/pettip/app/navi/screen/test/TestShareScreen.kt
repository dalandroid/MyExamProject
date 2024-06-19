package net.pettip.app.navi.screen.test

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import net.pettip.app.navi.R
import java.io.File
import java.io.OutputStream
import kotlin.contracts.contract

/**
 * @Project     : PetTip-Android
 * @FileName    : TestShareScreen
 * @Date        : 2024-06-18
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.test
 * @see net.pettip.app.navi.screen.test.TestShareScreen
 */
@Composable
fun TestShareScreen() {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                //val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.exam_dog)
                //
                //val imageUri = saveImageToMediaStore(context, "shared_image.jpg", bitmap)
                //
                //// 공유 인텐트 생성
                //imageUri?.let {
                //    val shareIntent = Intent().apply {
                //        action = Intent.ACTION_SEND
                //        putExtra(Intent.EXTRA_STREAM, it)
                //        type = "image/jpeg"
                //        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                //    }
                //    // 인텐트 실행
                //    context.startActivity(Intent.createChooser(shareIntent, null))
                //}

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "이것은 공유할 텍스트입니다.")
                    type = "text/plain"
                }
                // 인텐트 실행
                context.startActivity(Intent.createChooser(shareIntent, null))
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(text = "Share", color = Color.White)
        }
    }
}


fun saveImageToMediaStore(context: Context, imageName: String, bitmap: Bitmap): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val resolver = context.contentResolver
    val uri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        val outputStream: OutputStream? = resolver.openOutputStream(it)
        outputStream.use {outputStream1 ->
            if (outputStream1 != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream1)
            }
        }
    }

    return uri
}