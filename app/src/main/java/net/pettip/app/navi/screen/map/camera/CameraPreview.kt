package net.pettip.app.navi.screen.map.camera

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            CameraXManager.getPreviewView(it).apply {
                CameraXManager.bindCameraToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}