package net.pettip.app.navi.activity

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp
import net.pettip.app.navi.utils.singleton.MySharedPreference

/**
 * @Project     : PetTip-Android
 * @FileName    : MyApplication
 * @Date        : 2024-05-01
 * @author      : CareBiz
 * @description : net.pettip.app.navi.activity
 * @see net.pettip.app.navi.activity.MyApplication
 */
@HiltAndroidApp
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "226344419c2ba87b4309b7d42ac22ae0")
        NaverIdLoginSDK.initialize(this, "fk5tuUBi3UzTVQRcBMGK", "kbSHyo7KeQ", "PetTip")
        MySharedPreference.init(this)
    }
}