package net.pettip.app.navi.screen.login

import android.credentials.GetCredentialException
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.componet.ButtonSingleTap
import net.pettip.app.navi.componet.LoadingState
import net.pettip.app.navi.screen.Screen
import net.pettip.app.navi.ui.theme.design_login_kakaobtn
import net.pettip.app.navi.ui.theme.design_login_naverbtn
import net.pettip.app.navi.viewmodel.login.LoginViewModel

/**
 * @Project     : PetTip-Android
 * @FileName    : LoginScreen
 * @Date        : 2024-04-30
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.login
 * @see net.pettip.app.navi.screen.login.LoginScreen
 */
@Composable
fun LoginScreen(
    modifier:Modifier = Modifier,
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("985887161836-gj9pqql898d85483bc1ik53a5t1kg6du.apps.googleusercontent.com")
        .requestServerAuthCode("985887161836-gj9pqql898d85483bc1ik53a5t1kg6du.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val mGoogleSignInClient = GoogleSignIn.getClient(context,gso)
    val googleAuthLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)
            Log.d("LOG",account.email.toString()+account.id.toString())

        } catch (e: ApiException){

        }
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
                    val signInIntent = mGoogleSignInClient.signInIntent
                    googleAuthLauncher.launch(signInIntent)
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
