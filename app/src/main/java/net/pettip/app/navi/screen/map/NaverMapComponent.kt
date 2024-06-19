package net.pettip.app.navi.screen.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PolygonOverlay
import com.naver.maps.map.compose.rememberFusedLocationSource
import net.pettip.app.navi.viewmodel.map.MapViewModel

/**
 * @Project     : PetTip-Android
 * @FileName    : NaverMapComponent
 * @Date        : 2024-06-13
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.map.camera
 * @see net.pettip.app.navi.screen.map.camera.NaverMapComponent
 */
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMapComponent(
    viewModel: MapViewModel = hiltViewModel(),
    pickArea:(String?)->Unit
){
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.Basic,
                maxZoom = 18.0,
                minZoom = 5.0,
                isBuildingLayerGroupEnabled = true,
                locationTrackingMode = LocationTrackingMode.Follow
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

    var markerState by remember { mutableStateOf(MarkerState()) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val coordinateList by viewModel.coordinateList.collectAsState()
    val pickAreaRes by viewModel.pickArea.collectAsState()

    LaunchedEffect (pickAreaRes){
        pickArea(pickAreaRes)
    }

    LaunchedEffect(markerState) {
        if (markerState.position != LatLng(0.0, 0.0)) {
            // getAddress와 latLngToXY를 동시에 실행
            viewModel.getAddress(latLng = markerState.position)

            viewModel.getUserArea(distance = sliderPosition.toDouble(), latLng = markerState.position)
        } else {
            viewModel.updatePickArea(null)
            viewModel.resetCoordinateList()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        NaverMap(
            locationSource = rememberFusedLocationSource(),
            properties = mapProperties,
            uiSettings = mapUiSettings,
            onMapClick = { _, latLng ->
                markerState = MarkerState(latLng)
            }
        ) {
            Marker(
                state = markerState,
                onClick = {
                    markerState = MarkerState()
                    true
                }
            )

            if (coordinateList.isNotEmpty()){
                coordinateList.forEach {
                    PolygonOverlay(
                        coords = it,
                        color = Color(0x50147bb7),
                        outlineColor = Color(0xFF0000ff),
                        outlineWidth = 1.dp
                    )
                }
            }
        }
    }
}