package net.pettip.app.navi.utils.singleton

import android.content.Context
import android.content.SharedPreferences

/**
 * @Project     : PetTip-Android
 * @FileName    : MySharedPreference
 * @Date        : 2024-05-01
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.singleton
 * @see net.pettip.app.navi.utils.singleton.MySharedPreference
 */
object MySharedPreference {
    private const val DATA = "data"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(DATA, Context.MODE_PRIVATE)
    }

    fun setAccessToken(accessToken: String?) {
        sharedPreferences.edit().putString("AccessToken", accessToken).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("AccessToken", null)
    }

    fun setRefreshToken(refreshToken: String?) {
        sharedPreferences.edit().putString("RefreshToken", refreshToken).apply()
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("RefreshToken", null)
    }
}