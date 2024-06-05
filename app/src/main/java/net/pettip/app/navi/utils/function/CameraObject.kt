package net.pettip.app.navi.utils.function

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.navercorp.nid.NaverIdLoginSDK
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Random

/**
 * @Project     : PetTip-Android
 * @FileName    : SaveBitmapToGallery
 * @Date        : 2024-06-03
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.function
 * @see net.pettip.app.navi.utils.function.SaveBitmapToGallery
 */

object CameraObject{

    var isRecordingStoppedManually by mutableStateOf(false)
    fun saveBitmapToGallery(context: Context, bitmap: Bitmap): Boolean {
        val randomNumber = Random().nextInt(90000) + 10000 // 10000 ~ 99999 범위의 난수 생성
        val day = SimpleDateFormat("yyyyMMdd", Locale.US).format(System.currentTimeMillis())
        val title = "PETTIP_${day}_${randomNumber}"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, title)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/PetTip")
                put(MediaStore.MediaColumns.IS_PENDING, true)
            }
        }

        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            context.contentResolver.openOutputStream(it).use { outputStream ->
                if (outputStream != null) {
                    try {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            contentValues.clear()
                            contentValues.put(MediaStore.MediaColumns.IS_PENDING, false)
                            context.contentResolver.update(uri, contentValues, null, null)
                        }
                        return true
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return false
    }

    private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    fun startRecording(
        context: Context,
        controller: LifecycleCameraController,
        recording: Recording?,
        setRecording: (Recording?) -> Unit
    ) {
        if (controller.isRecording) {
            recording?.stop()
            setRecording(null)
            return
        }

        isRecordingStoppedManually = false

        val outputFile = File(context.filesDir, "my-recording.mp4")
        val newRecording = if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as ComponentActivity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO_PERMISSION
            )
            return
        } else {
            controller.startRecording(
                FileOutputOptions.Builder(outputFile).build(),
                AudioConfig.create(true),
                ContextCompat.getMainExecutor(context),
            ) { event ->
                when (event) {
                    is VideoRecordEvent.Finalize -> {
                        if (event.hasError()) {
                            recording?.close()
                            setRecording(null)
                        } else if (!isRecordingStoppedManually) {
                            val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())
                            val contentValues = ContentValues().apply {
                                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                                put(MediaStore.Video.VideoColumns.ORIENTATION, 90) // 회전 정보 추가
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    put(MediaStore.MediaColumns.RELATIVE_PATH, "Movies/PetTip")
                                }
                            }

                            val resolver = context.contentResolver
                            val uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
                            if (uri != null) {
                                try {
                                    resolver.openOutputStream(uri)?.use { outputStream ->
                                        FileInputStream(outputFile).use { inputStream ->
                                            inputStream.copyTo(outputStream)
                                        }
                                    }
                                    // 임시 파일 삭제
                                    Toast.makeText(context, "저장되었습니다", Toast.LENGTH_SHORT).show()
                                    outputFile.delete()
                                } catch (e: Exception) {
                                    Log.e("CaptureButtons", "Error moving file to MediaStore", e)
                                }
                            }
                        } else {
                            // Manually stopped, do not save to gallery
                            isRecordingStoppedManually = false
                        }
                    }
                }
            }
        }
        setRecording(newRecording)
    }


    fun stopRecording(
        context: Context,
        controller: LifecycleCameraController,
        recording: Recording?,
        setRecording: (Recording?) -> Unit
    ) {
        if (controller.isRecording) {
            recording?.stop()
            setRecording(null)
            isRecordingStoppedManually = true
            Toast.makeText(context, "녹화가 중지되었습니다", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "녹화 중이 아닙니다", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePhoto(
        controller: LifecycleCameraController,
        context: Context,
        currentUri : (Uri) -> Unit
    ) {
        val randomNumber = kotlin.random.Random.nextInt(10000, 100000)
        val day = SimpleDateFormat("yyyyMMdd", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "PETTIP_${day}_${randomNumber}")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PetTip")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        controller.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("LOG", "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    output.savedUri?.let {
                        // 이미지가 저장된 uri 반환
                        currentUri(it)
                    }
                }
            }
        )
    }


    /** Bitmap 반환 */
    fun takePhoto(
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(NaverIdLoginSDK.applicationContext),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )

                    onPhotoTaken(rotatedBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }
}
