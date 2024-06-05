package net.pettip.app.navi.screen.map

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.Projection
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
import kotlinx.coroutines.async
import net.pettip.app.navi.componet.ExpandingCircleBox
import net.pettip.app.navi.viewmodel.map.AddressResult
import net.pettip.app.navi.viewmodel.map.MapViewModel

/**
 * @Project     : PetTip-Android
 * @FileName    : NaverMapScreen
 * @Date        : 2024-05-14
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.map
 * @see net.pettip.app.navi.screen.map.NaverMapScreen
 */
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    var markerState by remember { mutableStateOf(MarkerState()) }
    var sheetShow by remember { mutableStateOf(false) }
    var expanded1 by remember{ mutableStateOf(false) }
    var expanded2 by remember{ mutableStateOf(false) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val context = LocalContext.current

    val address by viewModel.address.collectAsState()
    val addressResult by viewModel.addressResult.collectAsState()

    val searchToAddress by viewModel.searchToAddress.collectAsState()
    val searchToAddressResult by viewModel.searchResult.collectAsState()

    val admNm by viewModel.admNmList.collectAsState()

    val coordinateList by viewModel.coordinateList.collectAsState()

    LaunchedEffect(markerState) {
        sheetShow = markerState.position != LatLng(0.0, 0.0)

        if (markerState.position != LatLng(0.0, 0.0)) {
            // getAddress와 latLngToXY를 동시에 실행
            viewModel.getAddress(latLng = markerState.position)

            viewModel.getUserArea(distance = sliderPosition.toDouble(), latLng = markerState.position)


            sheetShow = true
        } else {
            viewModel.resetCoordinateList()
            sheetShow = false
        }
    }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
    ) {
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
                onClick = { marker ->
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

        AnimatedVisibility(
            visible = sheetShow,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopCenter)
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = addressResult,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(600)) togetherWith fadeOut(animationSpec = tween(600))
                    }, label = ""
                ) { state ->
                    when (state) {
                        is AddressResult.Loading -> LinearProgressIndicator(color = Color.LightGray, modifier = Modifier.padding(vertical = 20.dp))
                        is AddressResult.Success -> {
                            val (address1, address2, address3) = state
                            Text("법정동: ${address1?:""}\n행정동: ${address2?:""}\n도로명: ${address3?:""}")
                        }
                        is AddressResult.Failure -> Text(state.message)
                        else -> {}
                    }
                }
            }
        }

        ExpandingCircleBox(
            expanded = expanded1,
            size = 60.dp,
            paddingValue = PaddingValues(start = 0.dp, top = 0.dp, end = 20.dp, bottom = 20.dp),
            modifier = Modifier.align(Alignment.BottomEnd),
            collapsedContent = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { expanded1 = !expanded1 }
                )
                               },
            expandableContent = {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TextField(
                        value = address?:"",
                        onValueChange = { viewModel.updateAddress(it) },
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                viewModel.getGeocode()
                            }
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                expanded1 = !expanded1
                            }
                    )
                }
            }
        )


        ExpandingCircleBox(
            expanded = expanded2,
            size = 60.dp,
            paddingValue = PaddingValues(start = 0.dp, top = 0.dp, end = 20.dp, bottom = 120.dp),
            modifier = Modifier.align(Alignment.BottomEnd),
            collapsedContent = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { expanded2 = !expanded2 }
                )
            },
            expandableContent = {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TextField(
                        value = searchToAddress?:"",
                        onValueChange = { viewModel.updateSearchToAddress(it) },
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                viewModel.searchToAddress()
                            }
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                expanded2 = !expanded2
                            }
                    )
                }
            }
        )

        Box(modifier = Modifier
            .padding(horizontal = 100.dp, vertical = 20.dp)
            .align(Alignment.BottomCenter)
            .background(Color.White),
            contentAlignment = Alignment.Center
        ){
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                colors = SliderDefaults.colors(
                    thumbColor = Color.Blue,
                    activeTrackColor = Color.Blue,
                    inactiveTrackColor = Color.Gray,
                ),
                steps = 3,
                valueRange = 0f..600f
            )
        }

        Box(modifier = Modifier
            .padding(start = 100.dp, end = 100.dp, bottom = 100.dp)
            .align(Alignment.BottomCenter)
            .background(Color.White),
            contentAlignment = Alignment.Center
        ){
            Text(text = admNm.toString(), modifier = Modifier.fillMaxWidth())
        }
    }// Box
}