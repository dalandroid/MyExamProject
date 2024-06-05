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
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import com.google.android.gms.stats.WakeLock
import net.pettip.app.navi.R
import net.pettip.app.navi.componet.LoadingState
import net.pettip.app.navi.screen.map.camera.CameraXManager
import net.pettip.app.navi.screen.map.camera.StopServiceReceiver
import net.pettip.app.navi.utils.function.CameraObject
import net.pettip.app.navi.utils.singleton.MySharedPreference
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * @Project     : PetTip-Android
 * @FileName    : RecordingService
 * @Date        : 2024-05-30
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.service
 * @see net.pettip.app.navi.utils.service.RecordingService
 */

class RecordingService : LifecycleService() {

    val context = this

    private var elapsedTime = 0
    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler

    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate() {
        super.onCreate()
        Log.d("MyLifecycleService", "Service Created")
        // HandlerThread 생성 및 시작
        handlerThread = HandlerThread("TimerThread")
        handlerThread.start()

        // Handler 초기화
        handler = Handler(handlerThread.looper)
        handler.post(runnable)

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CameraXApp:WakeLock")
        wakeLock.acquire()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("MyLifecycleService", "Service Started")

        if (MySharedPreference.getServiceRunning()){
            stopSelf()
            Log.d("MyLifecycleService","stopSelf")
        }else{
            startForegroundService()
            Log.d("MyLifecycleService","start")
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyLifecycleService", "Service Destroyed")
        MySharedPreference.setServiceRunning(false)

        // HandlerThread 종료 및 Handler의 콜백 제거
        handler.removeCallbacks(runnable)
        handlerThread.quitSafely()

        if (wakeLock.isHeld) {
            wakeLock.release() // Wake Lock 해제
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private fun startForegroundService() {
        val channelId = "my_service_channel"
        val channelName = "My Service"

        MySharedPreference.setServiceRunning(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notification = createNotification()
        startForeground(1, notification)
    }

    private fun createNotification(): Notification {
        val stopIntent = Intent(this, StopServiceReceiver::class.java).apply {
            action = "STOP_SERVICE"
        }
        val stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, "my_service_channel")
            .setContentTitle("Foreground Service")
            .setContentText("Elapsed time: ${formatElapsedTime(elapsedTime)}")
            .setSmallIcon(R.drawable.baseline_camera_alt_24)
            .setWhen(System.currentTimeMillis())
            .addAction(R.drawable.baseline_stop_24, "Stop", stopPendingIntent)
            .build()
    }

    private fun updateNotification() {
        val notification = createNotification()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)
    }

    private val runnable = object : Runnable {
        override fun run() {
            elapsedTime++
            updateNotification()
            handler.postDelayed(this, 1000)
        }
    }

    private fun formatElapsedTime(seconds: Int): String {
        val hrs = seconds / 3600
        val mins = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hrs, mins, secs)
    }
}