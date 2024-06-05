package net.pettip.app.navi.utils.function

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @Project     : PetTip-Android
 * @FileName    : RememberGravity
 * @Date        : 2024-05-29
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.function
 * @see net.pettip.app.navi.utils.function.RememberGravity
 */

/**
 * gravity 의 X angle 값을 반환
 * */
@Composable
fun rememberGravity(): Float {

    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(SENSOR_SERVICE) as SensorManager }
    val gravitySensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) }
    val xrAngle = remember { Animatable(0f) }

    val scope = rememberCoroutineScope()

    // 기존 기울기 값을 기억하는 상태 변수
    var lastAngle by remember { mutableFloatStateOf(0f) }

    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val r = sqrt(x.pow(2) + y.pow(2) + z.pow(2))
                val angle = (90 - acos(x / r) * 180 / PI).toFloat()

                // 기존 값과 5f 이상 차이가 나는 경우에만 변경
                if (kotlin.math.abs(angle - lastAngle) >= 5f) {
                    lastAngle = angle
                    scope.launch {
                        xrAngle.animateTo(
                            targetValue = angle,
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    DisposableEffect(Unit) {
        sensorManager.registerListener(sensorEventListener, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    return xrAngle.value
}