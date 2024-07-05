package net.pettip.app.navi.screen.login

import android.annotation.SuppressLint
import android.credentials.GetCredentialException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.pettip.app.navi.R
import net.pettip.app.navi.componet.ButtonSingleTap
import net.pettip.app.navi.componet.LoadingState
import net.pettip.app.navi.screen.Screen
import net.pettip.app.navi.ui.theme.design_login_kakaobtn
import net.pettip.app.navi.ui.theme.design_login_naverbtn
import net.pettip.app.navi.viewmodel.login.LoginViewModel
import java.math.BigInteger
import java.security.SecureRandom
import java.util.Collections

/**
 * @Project     : PetTip-Android
 * @FileName    : LoginScreen
 * @Date        : 2024-04-30
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.login
 * @see net.pettip.app.navi.screen.login.LoginScreen
 */

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun LoginScreen(
    modifier:Modifier = Modifier,
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    /** 여기부터 credential 을 이용한 Login */
    fun generateNonce(size: Int = 32): String {
        val random = SecureRandom()
        val nonce = ByteArray(size)
        random.nextBytes(nonce)
        return BigInteger(1, nonce).toString(16)
    }

    Column(modifier = modifier
        .fillMaxSize()
        .safeContentPadding()
        .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ButtonSingleTap(
            modifier = modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = design_login_kakaobtn, contentColor = Color.Black),
            delayTime = 1500,
            onClick = {
                loginViewModel.viewModelScope.launch {
                    LoadingState.show()
                    val isSuccess = loginViewModel.kakaoLogin(context)
                    if (isSuccess){
                        val result = loginViewModel.onLogin()
                        LoadingState.hide()
                        if (result == 0){
                            /** 로그인 성공 status == 200 */
                            navController.navigate(Screen.MainScreen.route){
                                popUpTo(0)
                            }
                        }else if(result == 1){
                            /** 로그인 실패 status != 200 */
                        }else if(result == 3){
                            /** 로그인 실패 status == 403 */
                        }else{
                            /** 로그인 실패 통신실패 */
                        }
                    }else{
                        /** 카카오 로그인 실패 */
                        LoadingState.hide()
                    }
                }
            }
        ) {
            Box (modifier = modifier.fillMaxSize()){
                Icon(painter = painterResource(id = R.drawable.icon_kakao), contentDescription = "",
                    modifier = modifier.align(Alignment.CenterStart), tint = Color.Unspecified)
                Text(
                    text = "카카오톡으로 로그인",
                    modifier = modifier.align(Alignment.Center),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp
                )
            }
        }

        ButtonSingleTap(
            modifier = modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = design_login_naverbtn, contentColor = Color.White),
            delayTime = 1500,
            onClick = {
                loginViewModel.viewModelScope.launch {
                    LoadingState.show()
                    val isSuccess = loginViewModel.naverLogin(context)
                    if (isSuccess){
                        val result = loginViewModel.onLogin()
                        if (result == 0){
                            Log.d("LOG","0")
                            /** 로그인 성공 status == 200 */
                            navController.navigate(Screen.MainScreen.route){
                                popUpTo(0)
                            }
                        }else if(result == 1){
                            /** 로그인 실패 status != 200 */
                        }else if(result == 3){
                            /** 로그인 실패 status == 403 */
                        }else{
                            /** 로그인 실패 통신실패 */
                        }
                        LoadingState.hide()
                    }else{
                        /** 네이버 로그인 실패 */
                        LoadingState.hide()
                    }
                }
            }
        ) {
            Box (modifier = modifier.fillMaxSize()){
                Icon(
                    painter = painterResource(id = R.drawable.icon_naver),
                    contentDescription = "",
                    modifier = modifier.align(Alignment.CenterStart),
                    tint = Color.Unspecified
                )
                Text(
                    text = "네이버로 로그인",
                    modifier = modifier.align(Alignment.Center),
                    fontSize = 14.sp, letterSpacing = (-0.7).sp
                )
            }
        }

        ButtonSingleTap(
            modifier = modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            delayTime = 1500,
            onClick = {
                loginViewModel.viewModelScope.launch {
                    LoadingState.show()
                    val isSuccess = loginViewModel.googleLogin(context)
                    if (isSuccess){
                        val result = loginViewModel.onLogin()
                        if (result == 0){
                            Log.d("LOG","0")
                            /** 로그인 성공 status == 200 */
                            navController.navigate(Screen.MainScreen.route){
                                popUpTo(0)
                            }
                        }else if(result == 1){
                            /** 로그인 실패 status != 200 */
                        }else if(result == 3){
                            /** 로그인 실패 status == 403 */
                        }else{
                            /** 로그인 실패 통신실패 */
                        }
                        LoadingState.hide()
                    }else{
                        LoadingState.hide()
                    }
                }
            }
        ) {
            Box (modifier = modifier.fillMaxSize()){
                Icon(painter = painterResource(id = R.drawable.icon_google), contentDescription = "",
                    modifier = modifier.align(Alignment.CenterStart), tint = Color.Unspecified)
                Text(
                    text = "구글로 로그인",
                    modifier = modifier.align(Alignment.Center),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp
                )
            }
        }
    }
}
