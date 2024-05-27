package net.pettip.app.navi.viewmodel.login

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import net.pettip.app.navi.datamodel.data.login.LoginReq
import net.pettip.app.navi.datamodel.repository.LoginRepository
import net.pettip.app.navi.utils.service.ApiService
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * @Project     : PetTip-Android
 * @FileName    : LoginViewModel
 * @Date        : 2024-05-01
 * @author      : CareBiz
 * @description : net.pettip.app.navi.viewmodel
 * @see net.pettip.app.navi.viewmodel.LoginViewModel
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val loginRepository: LoginRepository
) : ViewModel() {

    companion object {
        const val TAG = "KakaoViewModel"
    }

    private val _email = MutableStateFlow<String?>(null)

    private val _unqId = MutableStateFlow<String?>(null)

    private val _nickName = MutableStateFlow<String?>(null)
    val nickName: StateFlow<String?> = _nickName.asStateFlow()
    fun updateNickName(newValue: String?) { _nickName.value = newValue }

    suspend fun kakaoLogin(context: Context): Boolean {
        return suspendCancellableCoroutine { continuation ->
            // 카카오 계정으로 로그인을 위한 콜백
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                    continuation.resume(false)
                } else if (token != null) {
                    Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")

                    UserApiClient.instance.me { user, error ->
                        if (user != null) {
                            val email = user.kakaoAccount?.email ?: ""
                            val id = user.id.toString()
                            val nickname = user.kakaoAccount?.profile?.nickname ?: ""

                            Log.d("KAKAO","email:${email},id:${id},nick:${nickname}")

                            continuation.resume(true)
                        }
                    }
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                    } else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")

                        UserApiClient.instance.me { user, error ->
                            if (user != null) {
                                val email = user.kakaoAccount?.email ?: ""
                                val id = user.id.toString()
                                val nickname = user.kakaoAccount?.profile?.nickname ?: ""

                                _email.value = email
                                _unqId.value = id
                                _nickName.value = nickname

                                continuation.resume(true)
                            }
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        }
    }

    suspend fun naverLogin(context: Context):Boolean{
        return suspendCancellableCoroutine { continuation ->

            val oAuthLoginCallback = object : OAuthLoginCallback {
                override fun onError(errorCode: Int, message: String) {
                    Log.d("NAVER","error : ${message}")
                    continuation.resume(false)
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Log.d("NAVER","failure : ${message}")
                    continuation.resume(false)
                }

                override fun onSuccess() {
                    NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                        override fun onError(errorCode: Int, message: String) {
                            Log.d("NAVER","suc -> error : ${message}")
                            continuation.resume(false)
                        }

                        override fun onFailure(httpStatus: Int, message: String) {
                            Log.d("NAVER","suc -> failure : ${message}")
                            continuation.resume(false)
                        }

                        override fun onSuccess(result: NidProfileResponse) {
                            val email = result.profile?.email?: ""
                            val id = result.profile?.id?: ""
                            val nickname = result.profile?.nickname?: ""

                            _email.value = email
                            _unqId.value = id
                            _nickName.value = nickname

                            continuation.resume(true)
                        }

                    })
                }

            }

            NaverIdLoginSDK.authenticate(context,oAuthLoginCallback)
        }
    }

    suspend fun onLogin(): Int {
        val appTyNm = Build.MODEL.toString()
        val loginReq = LoginReq(appTypNm = appTyNm, userID = _email.value, userPW = _unqId.value)

        return try {
            val result = loginRepository.login(loginReq)
            result.getOrNull()?.let {
                if (it.statusCode == 200) {
                    0 // 로그인 성공
                } else {
                    1 // 로그인 실패
                }
            } ?: 1 // 로그인 실패
        } catch (e: Exception) {
            if (e.message == "Blocked user") {
                3 // 차단 사용자
            } else {
                2 // 통신 실패
            }
        }
    }
}
