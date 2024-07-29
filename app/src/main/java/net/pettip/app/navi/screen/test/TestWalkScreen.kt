package net.pettip.app.navi.screen.test

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.compose.ArrowheadPathOverlay
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.GroundOverlay
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.PolygonOverlay
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import net.pettip.app.navi.activity.CameraActivity
import net.pettip.app.navi.screen.map.NaverMapComponent
import net.pettip.app.navi.screen.map.camera.CameraScreen
import net.pettip.app.navi.utils.service.LocationService
import net.pettip.app.navi.utils.service.RecordingService
import net.pettip.app.navi.utils.service.RecordingService.Companion.ACTION_START_FOREGROUND
import net.pettip.app.navi.utils.singleton.MySharedPreference
import net.pettip.app.navi.viewmodel.map.MapViewModel
import java.io.File

/**
 * @Project     : PetTip-Android
 * @FileName    : TestWalkScreen
 * @Date        : 2024-07-02
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.test
 * @see net.pettip.app.navi.screen.test.TestWalkScreen
 */
@OptIn(ExperimentalNaverMapApi::class, ExperimentalPermissionsApi::class)
@Composable
fun TestWalkScreen(
    viewModel: MapViewModel = hiltViewModel(),
){

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color.Transparent)

    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.Basic,
                maxZoom = 18.0,
                minZoom = 5.0,
                isBuildingLayerGroupEnabled = false,
                isCadastralLayerGroupEnabled = false,
                isTransitLayerGroupEnabled = false,
                locationTrackingMode = LocationTrackingMode.Follow,
                symbolScale = 1.0f
            )
        )
    }

    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                isLocationButtonEnabled = true,
                isLogoClickEnabled = false,
            )
        )
    }

    var bitmap by remember{ mutableStateOf<Bitmap?>(null) }

    var path by remember{ mutableStateOf(listOf<LatLng>()) }

    var locationList by remember { mutableStateOf(listOf<Location>()) }

    val context = LocalContext.current

    LaunchedEffect(locationList) {
        val tempPath = LocationService.loadGPXFile()?:emptyList()
        if (tempPath.isNotEmpty()){
            path = tempPath
        }
    }

    val permissionList = rememberMultiplePermissionsState(
        permissions = buildList {
            add(Manifest.permission.CAMERA)
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    )

    LaunchedEffect (Unit){
        if(!permissionList.allPermissionsGranted){
            permissionList.launchMultiplePermissionRequest()
        }
    }

    DisposableEffect(Unit) {

        onDispose { systemUiController.setStatusBarColor(Color.White) }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .navigationBarsPadding()
    ){
        /** 나중에 location 정보 받아올 곳 */
        NaverMap(
            locationSource = rememberFusedLocationSource(),
            properties = mapProperties,
            uiSettings = mapUiSettings,
            onLocationChange = {
                /** 나중에 location 정보 받아올 곳 */
                if (locationList.isEmpty()){
                    /** 비어있으면 추가 */
                    locationList = locationList + it
                }else{
                    /** 거리비교해서 1M 이상 15M 이하면 추가 */
                    val distance = it.distanceTo(locationList.last())
                    if (distance >= 1f && distance <15f){
                        locationList = locationList + it
                    }
                }
            }
        ) {
            /** Path 를 그리는 컴포저블 **/
            if (path.size >= 2){
                PathOverlay(
                    coords = path,
                    outlineWidth = 1.dp,
                    color = Color(0xA0FFDBDB),
                    outlineColor = Color(0xA0FF5000),
                    width = 8.dp
                )
            }
            if (path.isNotEmpty() && bitmap != null){
                Marker(
                    icon = OverlayImage.fromBitmap(bitmap!!)
                )
            }
        }

        Button(
            onClick = {
                val intent = Intent(context, CameraActivity::class.java)
                context.startActivity(intent) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(text = "카메라", color = Color.White)
        }

        Button(
            onClick = {
                val intent = Intent(context, LocationService::class.java)
                if (MySharedPreference.getServiceRunning()){
                    intent.action = LocationService.GPX_STOP
                    context.startService(intent)
                    Log.d("LOG","stop service")
                }else{
                    intent.action = LocationService.GPX_START
                    context.startService(intent)
                    Log.d("LOG","start service")
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue
            )
        ) {
            Text(
                text = "Start Location Service : ${path.size}",
                color = Color.White
            )
        }
    }
}

fun getCircularBitmap(bitmap: Bitmap): Bitmap {
    val size = Math.min(bitmap.width, bitmap.height)

    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    val paint = Paint()
    val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    paint.shader = shader
    paint.isAntiAlias = true

    val radius = size / 2f
    canvas.drawCircle(radius, radius, radius, paint)

    return output
}

fun savePathToFile(context: Context, path: List<LatLng>, fileName: String) {
    try {
        val gson = Gson()
        val pathJson = gson.toJson(path)
        val file = File(context.filesDir, fileName) // 내부 저장소 경로 사용
        file.writeText(pathJson)
        Log.d("FileSave", "Path saved to file: ${file.absolutePath}")
    } catch (e: Exception) {
        Log.e("FileSave", "Error saving path to file", e)
    }
}