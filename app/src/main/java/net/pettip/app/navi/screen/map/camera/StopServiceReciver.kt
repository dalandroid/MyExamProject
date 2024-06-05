package net.pettip.app.navi.screen.map.camera

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import net.pettip.app.navi.utils.service.RecordingService

/**
 * @Project     : PetTip-Android
 * @FileName    : StopServiceReciver
 * @Date        : 2024-06-04
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.map.camera
 * @see net.pettip.app.navi.screen.map.camera.StopServiceReciver
 */
class StopServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "STOP_SERVICE") {
            Log.d("StopServiceReceiver", "Stop service button clicked")
            context?.stopService(Intent(context, RecordingService::class.java))
        }
    }
}
