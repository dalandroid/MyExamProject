package net.pettip.app.navi.datamodel.data.naver

import android.location.Location
import android.net.Uri

const val TRACK_ZERO_NUM = "P00000000000000"
val TRACK_ZERO_URI = Uri.parse("")!!

data class Track(
    private val loc: Location,
    val no: String = TRACK_ZERO_NUM,
    val event: EVENT = EVENT.NNN,
    val uri: Uri = TRACK_ZERO_URI,
) {
    companion object {

    }

    fun toText(): String {
        return "($latitude, $longitude)"
    }

    enum class EVENT {
        NNN, IMG, PEE, POO, MRK
    }

    val location: Location
        get() = loc

    val latitude: Double
        get() = loc.latitude

    val longitude: Double
        get() = loc.longitude

    val time: Long
        get() = loc.time

    val speed: Float
        get() = loc.speed

    val altitude: Double
        get() = loc.altitude

    val bearing: Float
        get() = loc.bearing
}