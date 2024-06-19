package net.pettip.app.navi.utils.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.PowerManager
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresPermission
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.video.AudioConfig
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import com.google.android.gms.stats.WakeLock
import dagger.hilt.android.scopes.ServiceScoped
import net.pettip.app.navi.R
import net.pettip.app.navi.componet.LoadingState
import net.pettip.app.navi.screen.map.camera.CameraXManager
import net.pettip.app.navi.screen.map.camera.CaptureMode
import net.pettip.app.navi.screen.map.camera.StopServiceReceiver
import net.pettip.app.navi.screen.map.camera.VideoState
import net.pettip.app.navi.utils.function.CameraObject
import net.pettip.app.navi.utils.singleton.MySharedPreference
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @Project     : PetTip-Android
 * @FileName    : RecordingService
 * @Date        : 2024-05-30
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.service
 * @see net.pettip.app.navi.utils.service.RecordingService
 */


class RecordingService : LifecycleService() {

    companion object {
        const val ACTION_START_FOREGROUND = "com.example.action.START_FOREGROUND"
        const val ACTION_SAVE_VIDEO = "com.example.action.SAVE_VIDEO"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MyLifecycleService", "Service Created")


    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("MyLifecycleService", "Service Started")

        // Intent로 전달된 액션을 확인
        val action = intent?.action
        when (action) {
            ACTION_START_FOREGROUND -> {
                startForegroundService()
            }
            ACTION_SAVE_VIDEO -> {

            }
            else -> {
                Log.d("MyLifecycleService", "Unknown action received")
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyLifecycleService", "Service Destroyed")

    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun startForegroundService() {
        val channelId = "my_service_channel"
        val channelName = "My Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notification = createNotification()
        startForeground(1, notification)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "my_service_channel")
            .setContentTitle("Foreground Service")
            .setContentText("Recording video...")
            .setSmallIcon(R.drawable.baseline_camera_alt_24)
            .setWhen(System.currentTimeMillis())
            .build()
    }
}