package net.pettip.app.navi.utils.singleton

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * @Project     : PetTip-Android
 * @FileName    : WebViewSingleton
 * @Date        : 2024-07-10
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.singleton
 * @see net.pettip.app.navi.utils.singleton.WebViewSingleton
 */
@SuppressLint("StaticFieldLeak")
object WebViewSingleton {
    private var webView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    fun getWebView(context: Context): WebView {
        if (webView == null) {
            webView = WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
                webViewClient = WebViewClient()
            }
        }
        return webView!!
    }
}
