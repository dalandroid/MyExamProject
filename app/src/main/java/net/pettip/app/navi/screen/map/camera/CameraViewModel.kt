package net.pettip.app.navi.screen.map.camera

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.pettip.app.navi.utils.service.RecordingService
import net.pettip.app.navi.utils.singleton.MySharedPreference
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
enum class VideoState {
    VIDEO_READY, VIDEO_RECORDING
}

@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
):ViewModel(){

    private var cameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null

    private var currentRecording: Recording? = null
    private lateinit var recordingState: VideoRecordEvent

    private val _currentMode = MutableStateFlow(CaptureMode.PHOTO)
    val currentMode: StateFlow<CaptureMode> = _currentMode.asStateFlow()

    private val _videoState = MutableStateFlow(VideoState.VIDEO_READY)
    val videoState: StateFlow<VideoState> = _videoState.asStateFlow()

    val previewView: PreviewView by lazy {
        PreviewView(application)
    }

    fun startCamera(lifecycleOwner: LifecycleOwner) {
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
        val recorder = Recorder.Builder()
            .setAspectRatio(AspectRatio.RATIO_4_3) // 4:3 비율 설정
            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
            .build()
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

    fun setCaptureMode(mode: CaptureMode, lifecycleOwner: LifecycleOwner) {
        _currentMode.value = mode
        bindPreviewUseCase(lifecycleOwner, previewView)
    }

    fun takePhoto(currentUri: (Uri?)-> Unit){
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

        val context: Context = application

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

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun onRecord(){
        if (_videoState.value == VideoState.VIDEO_READY){
            recording()
        }else{
            saveVideo()
        }
    }
    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun recording(){
        if (_currentMode.value != CaptureMode.VIDEO && _videoState.value == VideoState.VIDEO_RECORDING) return

        _videoState.value = VideoState.VIDEO_RECORDING

        val randomNumber = kotlin.random.Random.nextInt(10000, 100000)
        val day = SimpleDateFormat("yyyyMMdd", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "PETTIP_${day}_${randomNumber}")
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/PetTip")
            }
        }

        val context: Context = application

        val mediaStoreOutput = MediaStoreOutputOptions.Builder(
            context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        )
            .setContentValues(contentValues)
            .build()

        val captureListener = Consumer<VideoRecordEvent> { event ->
            recordingState = event
            if (event is VideoRecordEvent.Finalize) {
                Log.d("LOG","record end : ${event.outputResults.outputUri}")
            }
        }

        currentRecording = videoCapture?.output
            ?.prepareRecording(context, mediaStoreOutput)
            ?.apply { withAudioEnabled() }
            ?.start(ContextCompat.getMainExecutor(context), captureListener)
    }

    private fun saveVideo() {

        if (currentRecording == null) {
            return
        }


        if (currentRecording != null) {
            currentRecording?.stop()
            currentRecording = null
            _videoState.value = VideoState.VIDEO_READY
        }
    }

    fun deleteFileFromContentUri(uri: Uri?, context: Context) {
        try {
            val deletedRows = uri?.let { context.contentResolver.delete(it, null, null) }
            if (deletedRows != null) {
                if (deletedRows > 0) {
                    Log.d("DELETE", "파일 삭제 성공: $uri")
                } else {
                    Log.e("DELETE", "파일 삭제 실패: $uri")
                }
            }
        } catch (e: Exception) {
            Log.e("DELETE", "파일 삭제 중 예외 발생: $uri", e)
        }
    }

}