package net.pettip.app.navi.utils.service

import com.naver.maps.geometry.LatLng
import net.pettip.app.navi.datamodel.data.etc.SGISReq
import net.pettip.app.navi.datamodel.data.etc.SGISRes
import net.pettip.app.navi.datamodel.data.etc.SGISTransXY
import net.pettip.app.navi.datamodel.data.etc.UserAreaRes
import net.pettip.app.navi.datamodel.data.login.LoginReq
import net.pettip.app.navi.datamodel.data.login.LoginRes
import net.pettip.app.navi.datamodel.data.naver.AddressRes
import net.pettip.app.navi.datamodel.data.naver.NaverAddress
import net.pettip.app.navi.datamodel.data.naver.NaverGeocode
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * @Project     : PetTip-Android
 * @FileName    : ApiService
 * @Date        : 2024-05-01
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.service
 * @see net.pettip.app.navi.utils.service.ExternalApiService
 */
interface ExternalApiService {
    @GET
    fun getAddress(
        @Url url: String,
        @Header("X-NCP-APIGW-API-KEY-ID") apiKeyId: String,
        @Header("X-NCP-APIGW-API-KEY") apiKey: String
    ): Call<NaverAddress>

    @GET("map-geocode/v2/geocode")
    fun getGeocode(
        @Query("query") query: String,
        @Query("coordinate") coordinate: String?,
        @Header("X-NCP-APIGW-API-KEY-ID") apiKeyId: String,
        @Header("X-NCP-APIGW-API-KEY") apiKey: String
    ): Call<NaverGeocode>

    @GET
    fun getSearchToAddress(
        @Url url:String,
        @Query("query") query: String,
        @Query("display", encoded = false) display: Int?=null,
        @Query("start", encoded = false) start: Int?=null,
        @Query("sort", encoded = false) sort: String?=null,
        @Header("X-Naver-Client-Id") apiKeyId: String,
        @Header("X-Naver-Client-Secret") apiKey: String
    ):Call<String>

    @Headers("Content-Type: application/json")
    @GET
    suspend fun getAuthentication(
        @Url url:String,
        @Query("consumer_key", encoded = false) consumerKey: String,
        @Query("consumer_secret", encoded = false) consumerSecret: String,
    ): Response<SGISRes>

    @Headers("Content-Type: application/json")
    @GET
    suspend fun latLngToXY(
        @Url url:String,
        @Query("accessToken", encoded = false) accessToken: String,
        @Query("src", encoded = false) src: String,
        @Query("dst", encoded = false) dst: String,
        @Query("posX", encoded = false) posX: String,
        @Query("posY", encoded = false) posY: String
    ): Response<SGISTransXY>

    @Headers("Content-Type: application/json")
    @GET
    suspend fun userArea(
        @Url url:String,
        @Query("minx", encoded = false) minx: String,
        @Query("miny", encoded = false) miny: String,
        @Query("maxx", encoded = false) maxx: String,
        @Query("maxy", encoded = false) maxy: String,
        @Query("cd", encoded = false) cd: String,
        @Query("accessToken", encoded = false) accessToken: String
    ): Response<UserAreaRes>
}