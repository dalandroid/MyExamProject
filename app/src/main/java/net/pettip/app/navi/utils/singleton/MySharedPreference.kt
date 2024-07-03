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

    fun setServiceRunning(newValue: Boolean) {
        sharedPreferences.edit().putBoolean("ServiceRunning", newValue).apply()
    }

    fun getServiceRunning(): Boolean {
        return sharedPreferences.getBoolean("ServiceRunning", false)
    }

    fun setIsLogin(newValue : Boolean){
        sharedPreferences.edit().putBoolean("IsLogin",newValue).apply()
    }

    fun getIsLogin():Boolean{
        return sharedPreferences.getBoolean("IsLogin", false)
    }

    fun setIsInit(newValue : Boolean){
        sharedPreferences.edit().putBoolean("IsInit",newValue).apply()
    }

    fun getIsInit():Boolean{
        return sharedPreferences.getBoolean("IsInit", true)
    }
}