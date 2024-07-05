package net.pettip.app.navi.screen.test

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.location.Location
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
import net.pettip.app.navi.activity.CameraActivity
import net.pettip.app.navi.screen.map.NaverMapComponent
import net.pettip.app.navi.screen.map.camera.CameraScreen
import net.pettip.app.navi.utils.service.LocationService
import net.pettip.app.navi.utils.service.RecordingService
import net.pettip.app.navi.viewmodel.map.MapViewModel

/**
 * @Project     : PetTip-Android
 * @FileName    : TestWalkScreen
 * @Date        : 2024-07-02
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.test
 * @see net.pettip.app.navi.screen.test.TestWalkScreen
 */
@OptIn(ExperimentalNaverMapApi::class)
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
                    if (distance >= 10f && distance <150f){
                        locationList = locationList + it
                        path = path + LatLng(it.latitude,it.longitude)
                    }
                }
                Log.d("LOG",locationList.toString())
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
                // service 시작/종료 영역
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue
            )
        ) {
            Text(
                text = "Start Location Service",
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