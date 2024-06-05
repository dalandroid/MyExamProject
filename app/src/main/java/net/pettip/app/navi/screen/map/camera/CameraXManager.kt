package net.pettip.app.navi.screen.map.camera

import android.content.Context
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

/**
 * @Project     : PetTip-Android
 * @FileName    : CameraXManager
 * @Date        : 2024-06-04
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.map.camera
 * @see net.pettip.app.navi.screen.map.camera.CameraXManager
 */
object CameraXManager {
    private var cameraController: LifecycleCameraController? = null
    private var previewView: PreviewView? = null
    private var boundLifecycleOwner: LifecycleOwner? = null

    fun getCameraController(context: Context): LifecycleCameraController {
        if (cameraController == null) {
            cameraController = LifecycleCameraController(context).apply {
                setEnabledUseCases(
                    CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
                )
            }
        }
        return cameraController!!
    }

    fun getPreviewView(context: Context): PreviewView {
        if (previewView == null) {
            previewView = PreviewView(context).apply {
                this.controller = getCameraController(context)
            }
        }
        return previewView!!
    }

    fun bindCameraToLifecycle(lifecycleOwner: LifecycleOwner) {
        cameraController?.unbind()
        cameraController?.bindToLifecycle(lifecycleOwner)
    }
}