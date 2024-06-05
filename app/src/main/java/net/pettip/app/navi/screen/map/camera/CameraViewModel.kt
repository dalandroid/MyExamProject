package net.pettip.app.navi.screen.map.camera

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/**
 * @Project     : PetTip-Android
 * @FileName    : CameraViewModel
 * @Date        : 2024-05-31
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.map.camera
 * @see net.pettip.app.navi.screen.map.camera.CameraViewModel
 */

enum class CaptureMode {
    PHOTO, VIDEO
}
@HiltViewModel
class CameraViewModel @Inject constructor():ViewModel(){

    private var cameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null

    private val _currentMode = MutableStateFlow(CaptureMode.PHOTO)
    val currentMode: StateFlow<CaptureMode> = _currentMode.asStateFlow()


    fun startCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(previewView.context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindPreviewUseCase(lifecycleOwner, previewView)
        }, ContextCompat.getMainExecutor(previewView.context))
    }


    private fun bindPreviewUseCase(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageCapture = ImageCapture.Builder().build()
        val recorder = Recorder.Builder().build()
        val videoCapture = VideoCapture.withOutput(recorder)

        cameraProvider?.unbindAll()
        try {
            cameraProvider?.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview, imageCapture, videoCapture
            )
            this.preview = preview
            this.imageCapture = imageCapture
            this.videoCapture = videoCapture
        } catch (exc: Exception) {
            // Handle binding errors
        }
    }

    fun setCaptureMode(mode: CaptureMode, lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        _currentMode.value = mode
        bindPreviewUseCase(lifecycleOwner, previewView)
    }

    fun takePhoto(context: Context, currentUri: (Uri?)-> Unit){
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

        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object :ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri
                    if (savedUri != null){
                        // 저장이 성공한 케이스 uri 반환
                        currentUri(savedUri)
                    }else{
                        val msg = "Photo capture failed."
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    val msg = "save error"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}