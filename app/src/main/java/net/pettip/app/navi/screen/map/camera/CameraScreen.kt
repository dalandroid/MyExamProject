package net.pettip.app.navi.screen.map.camera

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.camera.view.PreviewView
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import net.pettip.app.navi.utils.singleton.MySharedPreference

/**
 * @Project     : PetTip-Android
 * @FileName    : CameraScreen
 * @Date        : 2024-05-30
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.map
 * @see net.pettip.app.navi.screen.map.CameraScreen
 */
@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
    enterPIPMode: () -> Unit,
    exitActivity: () -> Unit,
    isInPiPMode: Boolean
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentMode by viewModel.currentMode.collectAsState()
    var currentUri by remember{ mutableStateOf<Uri?>(null) }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = Color.Black)
    systemUiController.setNavigationBarColor(color = Color.Black)

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    val previewView = remember { PreviewView(context) }
    DisposableEffect(Unit) {
        viewModel.startCamera(lifecycleOwner, previewView)
        onDispose { }
    }

    /** 폴더블 상태를 감지 및 상태 변경 */
    //var isLayoutUnfolded by remember { mutableStateOf<Boolean?>(null) }
    //LaunchedEffect(lifecycleOwner, context) {
    //    val windowInfoTracker = WindowInfoTracker.getOrCreate(context)
    //    windowInfoTracker.windowLayoutInfo(context).collect { newLayoutInfo ->
    //        try {
    //            val foldingFeature = newLayoutInfo?.displayFeatures
    //                ?.firstOrNull { it is FoldingFeature } as FoldingFeature
    //            isLayoutUnfolded = (foldingFeature != null)
    //        } catch (e: Exception) {
    //            // If there was an issue detecting a foldable in the open position, default
    //            // to isLayoutUnfolded being false.
    //            isLayoutUnfolded = false
    //        }
    //    }
    //}

    BackHandler(enabled = true) {
        if (MySharedPreference.getServiceRunning()) {
            enterPIPMode()
        } else {
            exitActivity()
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    if (permissionState.allPermissionsGranted) {
        Box(modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
        ) {
            Column {

                /** 상단 메뉴 영역 */
                if (!isInPiPMode){
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black),
                        contentAlignment = Alignment.CenterStart
                    ){
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = Color.White,
                            modifier = Modifier
                                .padding(start = 16.dp, top = 12.dp, bottom = 12.dp)
                                .clickable { exitActivity() },
                            contentDescription = ""
                        )
                    }
                }

                /** 카메라 Preview 영역 */
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3 / 4f) // 4:3 비율로 설정
                ) {
                    Crossfade(
                        targetState = currentUri == null,
                        label = "",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (it){
                            /** 반환된 사진이 없는 경우 */
                            AndroidView(
                                factory = { previewView },
                                modifier = Modifier.fillMaxSize()
                            )
                        }else{
                            /** 반환된 사진이 있는 경우 */
                            val painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context)
                                    .data(currentUri)
                                    .build(),
                                filterQuality = FilterQuality.Low
                            )

                            Image(
                                painter = painter,
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentDescription = "",
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }


                /** 하단 컨트롤러 영역 */
                if (!isInPiPMode){
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.Black),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            Button(
                                onClick = { viewModel.setCaptureMode(CaptureMode.PHOTO, lifecycleOwner, previewView) },
                                modifier = Modifier.width(80.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.Black,
                                    containerColor = if (currentMode ==CaptureMode.PHOTO) Color.Blue else Color.LightGray
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = "사진",
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                            Button(
                                onClick = { viewModel.setCaptureMode(CaptureMode.VIDEO, lifecycleOwner, previewView) },
                                modifier = Modifier.width(80.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.Black,
                                    containerColor = if (currentMode ==CaptureMode.VIDEO) Color.Blue else Color.LightGray
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = "동영상",
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.padding(top = 12.dp))

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ){
                            Spacer(modifier = Modifier.size(50.dp))

                            Button(
                                onClick = {
                                          if (currentMode == CaptureMode.PHOTO){
                                              viewModel.takePhoto(context = context, currentUri = {newValue -> currentUri = newValue})
                                          }
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (currentMode == CaptureMode.VIDEO) Color.Red else Color.White
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .size(80.dp)
                            ) {

                            }

                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(Color.Transparent, CircleShape)
                                    .clip(CircleShape)
                                    .clickable {
                                        enterPIPMode()
                                    },
                                contentAlignment = Alignment.Center
                            ){
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    tint = Color.White,
                                    contentDescription =""
                                )
                            }
                        }
                    }
                }
            }// col
        }
    } else {
        CameraAndRecordAudioPermission(permissionState) {
            exitActivity()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraAndRecordAudioPermission(
    permissionState: MultiplePermissionsState,
    onPermissionsDenied: () -> Unit
) {
    if (!permissionState.allPermissionsGranted) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Camera and Audio permissions are required.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                Text(text = "Grant Permissions")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onPermissionsDenied) {
                Text(text = "Cancel")
            }
        }
    }
}

