package com.nsystem.jsandroid

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_local_web.*

class NetworkWebActivity : AppCompatActivity() {

    companion object {

        private const val ANDROID_NSYSTEM = "AndroidNSystem"

        private const val WEBVIEW_URL = "https://gifted-morse-49a628.netlify.app/hello.html"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_web)

        webview.settings.javaScriptEnabled = true
        webview.addJavascriptInterface(JavaScriptInterface(), ANDROID_NSYSTEM)
        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        webview.loadUrl(WEBVIEW_URL)

        btnSendToWeb.setOnClickListener {
            webview.evaluateJavascript("javascript:sendToWeb(\"${etToWeb.text}\")", null)
        }
    }

    override fun onDestroy() {
        webview.removeJavascriptInterface(ANDROID_NSYSTEM)
        super.onDestroy()
    }

    inner class JavaScriptInterface {

        @JavascriptInterface
        fun textFromWeb(fromWeb: String) {
            txtFromWeb.text = fromWeb
        }
    }
}