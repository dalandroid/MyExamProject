package net.pettip.app.navi.utils.singleton

import com.airbnb.lottie.BuildConfig
import net.pettip.app.navi.utils.service.ApiService
import net.pettip.app.navi.utils.service.ExternalApiService
import net.pettip.app.navi.utils.service.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

/**
 * @Project     : PetTip-Android
 * @FileName    : RetrofitClientServer
 * @Date        : 2024-05-01
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils
 * @see net.pettip.app.navi.utils.RetrofitClientServer
 */
//const val BASE_URL = "http://dev.pettip.net:8020/"
private const val BASE_URL = "https://pettip.net/"

private const val NAVER_BASE_URL = "https://naveropenapi.apigw.ntruss.com/"

object RetrofitClientServer {
    // 서버쪽 retrofit 싱글톤 객체

    private val tokenInterceptor = TokenInterceptor()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 로깅 레벨 설정
    }

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create()
    }

    val naverInstance: ExternalApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(NAVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create()
    }

    private val okHttpClient: OkHttpClient by lazy {
        if (BuildConfig.DEBUG){
            OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .addInterceptor(loggingInterceptor)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }else{
            OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .addInterceptor(loggingInterceptor)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }
    }

}

