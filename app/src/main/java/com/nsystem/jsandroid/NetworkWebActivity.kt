package com.nsystem.jsandroid

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_local_web.*

class NetworkWebActivity : AppCompatActivity() {

    companion object {

        private const val ANDROID_NSYSTEM = "AndroidNSystem"

        private const val WEBVIEW_URL = "https://gifted-morse-49a628.netlify.app/hello.html"

        private const val NOTIFICATION_CHANNEL_ID = "Nsystem_Channel"

        private const val NOTIFICATION_TICKER = "Nsystem Notification Ticker"

        private const val NOTIFICATION_NAME = "Nsystem Notification"
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
            showNotification(fromWeb)

            /**
             *  Should use runOnUiThread for UI component,
             *  without runOnUiThread code below UI component code will not be called
             *  These happened due to javascript interface run in new thread (not in UI thread)
             */
            runOnUiThread {
                txtFromWeb.text = fromWeb
            }
            Toast.makeText(this@NetworkWebActivity, fromWeb, Toast.LENGTH_LONG).show()
        }

        private fun showNotification(fromWeb: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                showNotificationForOreoAndHigher(fromWeb)
            }
            showNotificationForBelowOreo(fromWeb)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun showNotificationForOreoAndHigher(fromWeb: String) {
            @SuppressLint("WrongConstant") val notificationChannel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_MAX
                ).apply {
                    description = fromWeb
                    enableLights(true)
                    lightColor = Color.RED
                    vibrationPattern = longArrayOf(0, 1000, 500, 1000)
                    enableVibration(true)
                }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        private fun showNotificationForBelowOreo(fromWeb: String) {
            val notificationBuilder =
                NotificationCompat.Builder(
                    this@NetworkWebActivity,
                    NOTIFICATION_CHANNEL_ID
                )
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(NOTIFICATION_TICKER) //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(NOTIFICATION_NAME)
                .setContentText(fromWeb)
                .setContentInfo(fromWeb)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notificationBuilder.build())
        }
    }
}