package net.pettip.app.navi.utils.service

import com.google.gson.Gson
import net.pettip.app.navi.datamodel.data.RefreshRes
import net.pettip.app.navi.utils.singleton.MySharedPreference
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Project     : PetTip-Android
 * @FileName    : TokenInterceptor
 * @Date        : 2024-05-01
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.service
 * @see net.pettip.app.navi.utils.service.TokenInterceptor
 */
class TokenInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 원래의 요청을 가져옵니다.
        val originalRequest = chain.request()

        //val token = G.accessToken
        //val refreshToken = G.refreshToken
        val token : String? = MySharedPreference.getAccessToken()
        val refreshToken : String? = MySharedPreference.getRefreshToken()

        val requestBuilder = originalRequest.newBuilder()
        if (!token.isNullOrBlank()) {
            // AccessToken이 있는 경우 Authorization 헤더를 추가
            requestBuilder.header("Authorization", token)
        }else{
            requestBuilder.header("Authorization", MySharedPreference.getAccessToken()?:"")
        }
        if (!refreshToken.isNullOrBlank()) {
            // RefreshToken이 있는 경우 Refresh 헤더를 추가
            requestBuilder.header("Refresh", refreshToken)
        }else{
            requestBuilder.header("Refresh", MySharedPreference.getRefreshToken()?:"")
        }


        val requestWithToken = requestBuilder.build()
        val response = chain.proceed(requestWithToken)

        val responseBodyString = response.body

        if (response.code == 403) {
            //G.dupleLogin = true
        } else if (response.code == 401) {
            // code 401 : access Token 만료시 응답 코드
            // response로 새로운 access Token과  Refresh가 떨어짐
            // 새로 받은 토큰으로 통신 재시도
            val gson = Gson()
            val tokenResponse = gson.fromJson(responseBodyString?.string(), RefreshRes::class.java)

            MySharedPreference.setAccessToken(tokenResponse.data?.accessToken)
            MySharedPreference.setRefreshToken(tokenResponse.data?.refreshToken)
            //G.accessToken = tokenResponse.data?.accessToken
            //G.refreshToken = tokenResponse.data?.refreshToken

            if (!MySharedPreference.getAccessToken().isNullOrBlank()) {
                val retryRequestBuilder = originalRequest.newBuilder()
                retryRequestBuilder.header("Authorization", MySharedPreference.getAccessToken()?:"")
                retryRequestBuilder.header("Refresh", MySharedPreference.getRefreshToken()?:"")

                val retryRequestWithToken = retryRequestBuilder.build()
                return chain.proceed(retryRequestWithToken)
            }
        }


        return response
    }

}