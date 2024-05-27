package net.pettip.app.navi.datamodel.repository

import kotlinx.coroutines.suspendCancellableCoroutine
import net.pettip.app.navi.datamodel.data.login.LoginReq
import net.pettip.app.navi.datamodel.data.login.LoginRes
import net.pettip.app.navi.utils.function.errorBodyParse
import net.pettip.app.navi.utils.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * @Project     : PetTip-Android
 * @FileName    : LoginRepository
 * @Date        : 2024-05-14
 * @author      : CareBiz
 * @description : net.pettip.app.navi.datamodel.repository
 * @see net.pettip.app.navi.datamodel.repository.LoginRepository
 */
class LoginRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun login(loginReq: LoginReq): Result<LoginRes> {
        return suspendCancellableCoroutine { continuation ->
            val call = apiService.login(loginReq)
            call.enqueue(object : Callback<LoginRes> {
                override fun onResponse(call: Call<LoginRes>, response: Response<LoginRes>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            continuation.resume(Result.success(body))
                        } else {
                            continuation.resume(Result.failure(Exception("Response body is null")))
                        }
                    } else {
                        val errorBodyString = response.errorBody()?.string() ?: "Unknown error"
                        val status = errorBodyParse(errorBodyString, "status")
                        if (status == "403") {
                            continuation.resume(Result.failure(Exception("Blocked user")))
                        } else {
                            continuation.resume(Result.failure(Exception("Login failed")))
                        }
                    }
                }

                override fun onFailure(call: Call<LoginRes>, t: Throwable) {
                    continuation.resume(Result.failure(t))
                }
            })
            continuation.invokeOnCancellation {
                call.cancel()
            }
        }
    }

    // 다른 함수들 (예: kakaoLogin, naverLogin)도 여기에 추가할 수 있습니다.
}