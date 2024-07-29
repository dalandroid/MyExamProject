package net.pettip.app.navi.utils.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.migration.CustomInjection.inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.activity.MainActivity
import net.pettip.app.navi.datamodel.data.naver.Track
import net.pettip.app.navi.utils.function.GPXWriter
import net.pettip.app.navi.utils.function.GPX_TICK_FORMAT
import net.pettip.app.navi.utils.singleton.MySharedPreference
import net.pettip.app.navi.viewmodel.map.MapViewModel
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.TimeUnit

/**
 * @Project     : PetTip-Android
 * @FileName    : LocationService
 * @Date        : 2024-07-05
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.service
 * @see net.pettip.app.navi.utils.service.LocationService
 */
class LocationService : Service() {

    companion object {
        const val GPX_START = "START_FOREGROUND_GPX"
        const val GPX_STOP = "STOP_FOREGROUND_GPX"

        fun loadGPXFile(): List<LatLng>? {
            val filePath = MySharedPreference.getGPXFileName()
            if (filePath.isNullOrEmpty()){
                Log.d("FileLoad", "File path not found")
                return null
            }
            val file = File(filePath)
            if (!file.exists()) {
                Log.d("FileLoad", "File not found: ${file.absolutePath}")
                return null
            }

            val locations = mutableListOf<LatLng>()

            try {
                val inputStream = FileInputStream(file)
                val factory = XmlPullParserFactory.newInstance()
                val parser = factory.newPullParser()
                parser.setInput(inputStream, null)

                var eventType = parser.eventType

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    val tagName = parser.name

                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            if (tagName.equals("trkpt", ignoreCase = true)) {
                                val lat = parser.getAttributeValue(null, "lat").toDouble()
                                val lon = parser.getAttributeValue(null, "lon").toDouble()
                                locations.add(LatLng(lat, lon))
                            }
                        }
                    }
                    eventType = parser.next()
                }
            } catch (e: Exception) {
                Log.e("FileLoad", "Error loading path from file", e)
            }

            Log.d("LOG",locations.toString())

            return locations
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val fileName = "path_backup.json"
    private var pathList = mutableListOf<LatLng>()
    private var trackList = mutableListOf<Track>()
    private var lastLocation: Location? = null
    private lateinit var context: Context
    private var lastUpdateTime: Long = 0

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val handler = Handler(Looper.getMainLooper())
    private var startTime: Long = 0
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val action = intent?.action
        when (action) {
            GPX_START -> {
                startForegroundService()
            }
            GPX_STOP -> {
                stopSelf()
                MySharedPreference.setServiceRunning(false)
            }
            else -> {
                Log.d("LOG", "Unknown action received")
            }
        }
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::fusedLocationClient.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
        MySharedPreference.setServiceRunning(false)
        handler.removeCallbacks(updateNotificationRunnable)
    }

    private fun shouldAddLocation(newLocation: Location, lastLocation: Location?, currentTime: Long): Boolean {
        if (lastLocation == null) return true
        val distance = newLocation.distanceTo(lastLocation)
        val timeDiff = currentTime - lastUpdateTime

        return when {
            // 최소 이동 거리 미만인 경우
            distance < 1f -> false

            // 사람이 걷는 속도 이하인 경우
            distance >= 1f && distance <= 7f -> true

            // 사람이 뛰는 속도 이하인 경우
            distance > 7f && distance <= 15f && timeDiff >= 1000L -> true

            // 자전거를 타는 속도 이하인 경우
            distance > 15f && distance <= 30f && timeDiff >= 2000L -> true

            // 자동차를 타는 속도 이하인 경우
            distance > 30f && distance <= 50f && timeDiff >= 3000L -> true

            // 이상한 값인 경우
            else -> false
        }
    }

    private fun savePathToFile(track: List<Track>) {
        coroutineScope.launch {
            try {
                val file = File("${gpxs(context = context)}/${name(track.first().time)}.gpx")
                if (!file.exists()){
                    file.parentFile?.mkdirs()
                    file.createNewFile()
                    MySharedPreference.setGPXFileName(file.absolutePath)
                }
                file.let { GPXWriter().write(tracks = track, file = file) }
                Log.d("FileSave", "Path saved to file: ${file.absolutePath}")
            } catch (e: Exception) {
                Log.e("FileSave", "Error saving path to file", e)
            }
        }
    }

    private fun startForegroundService() {
        locationServiceStart()
        startTime = System.currentTimeMillis()
        notificationBuilder = createNotificationBuilder()
        startForeground(1, notificationBuilder.build())
        handler.post(updateNotificationRunnable)
    }

    private fun locationServiceStart() {
        MySharedPreference.setServiceRunning(true)
        context = this

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val currentTime = System.currentTimeMillis()
                    if (trackList.isEmpty() || shouldAddLocation(location, lastLocation, currentTime)) {
                        lastLocation = location
                        lastUpdateTime = currentTime
                        trackList.add(Track(loc = location))
                        savePathToFile(trackList)
                        Log.d("LocationService", "Location added and saved to file: $location")
                    }
                }
            }
        }

        startLocationUpdates()
    }

    private fun createNotificationBuilder(): NotificationCompat.Builder {
        val channelId = "my_service_channel"
        val channelName = "My Service"

        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val activityPendingIntent = PendingIntent.getActivity(
            this,
            0,
            launchActivityIntent(),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("행복한 산책중입니다")
            .setContentText("펫팁에서 위치정보를 확인중입니다.")
            .setSmallIcon(R.drawable.currentlocation)
            .setOngoing(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.drawable.baseline_camera_alt_24,
                "열기",
                activityPendingIntent
            )
            .setContentIntent(activityPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
    }

    private fun launchActivityIntent(): Intent {
        return Intent(this, MainActivity::class.java).apply {
            action = "GPX_OPEN"
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    private val updateNotificationRunnable = object : Runnable {
        override fun run() {
            val elapsedTime = System.currentTimeMillis() - startTime
            val hours = TimeUnit.MILLISECONDS.toHours(elapsedTime)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60

            val elapsedTimeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            notificationBuilder.setContentTitle("행복한 산책중입니다 - $elapsedTimeText")
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(1, notificationBuilder.build())

            handler.postDelayed(this, 1000)
        }
    }

    fun root(context: Context): String {
        return context.filesDir.path
    }

    fun gpxs(context: Context): String {
        return "${root(context)}/.GPX"
    }

    fun name(time: Long): String {
        return GPX_TICK_FORMAT.format(time)
    }
}