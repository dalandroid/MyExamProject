package net.pettip.app.navi.utils.service

import com.naver.maps.geometry.LatLng
import net.pettip.app.navi.datamodel.data.login.LoginReq
import net.pettip.app.navi.datamodel.data.login.LoginRes
import net.pettip.app.navi.datamodel.data.naver.AddressRes
import net.pettip.app.navi.datamodel.data.test.RTStoryListRes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * @Project     : PetTip-Android
 * @FileName    : ApiService
 * @Date        : 2024-05-01
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.service
 * @see net.pettip.app.navi.utils.service.ApiService
 */
interface ApiService {
    @POST("api/v1/member/login")
    fun login(@Body data: LoginReq): Call<LoginRes>
    @POST("api/v1/story/real-time-list")
    fun getRealTimeList(): Call<RTStoryListRes>
}