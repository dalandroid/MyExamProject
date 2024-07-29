package net.pettip.app.navi.screen.mainscreen

import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import net.pettip.app.navi.componet.LoadingState
import net.pettip.app.navi.utils.singleton.MySharedPreference
import net.pettip.app.navi.utils.singleton.WebViewSingleton

/**
 * @Project     : PetTip-Android
 * @FileName    : WalkScreen
 * @Date        : 2024-04-30
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.mainscreen
 * @see net.pettip.app.navi.screen.mainscreen.WalkScreen
 */
@Composable
fun MallScreen(){

    //LaunchedEffect(Unit){
    //    LoadingState.show()
    //    delay(2000)
    //    LoadingState.hide()
    //}

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        val context = LocalContext.current
        AndroidView(
            factory = {
                val webView = WebViewSingleton.getWebView(context)
                // 이미 부모가 있다면 제거
                (webView.parent as? ViewGroup)?.removeView(webView)
                webView

            } ,
            update = {
                it.loadUrl("https://www.google.com/")
            }
        )
    }
}