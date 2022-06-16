package com.template

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.template.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var cookieManager: CookieManager

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_NoActionBar)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)
        cookieManager = CookieManager.getInstance()

        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true);

        with(binding.webView) {
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            settings.databaseEnabled = true
            settings.setAppCachePath(applicationContext.filesDir.absolutePath + "/cache")
            settings.databasePath = applicationContext.filesDir.absolutePath + "/databases"

//            if (savedInstanceState != null) restoreState(savedInstanceState)
//            else loadUrl(preferences.getString(SERVER_URL, EMPTY)!!)
            loadUrl(preferences.getString(SERVER_URL, EMPTY)!!)

//            cookieManager.setAcceptThirdPartyCookies(this, true)
            webViewClient = object : WebViewClient() {

//                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                    preferences.getString("COOKIES", "")?.let { cookieManager.setCookie(url, it) }
//                    super.onPageStarted(view, url, favicon)
//                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url);
                    cookieManager.flush()
//                    val cookies = cookieManager.getCookie(url)
//                    if (!cookies.contains("userId=0")) { preferences.edit().putString("COOKIES",cookies).apply() }
                }
            }
        }
    }


//    override fun onSaveInstanceState(outState: Bundle) {
//        binding.webView.saveState(outState)
//        super.onSaveInstanceState(outState)
//    }

    override fun onBackPressed() {
        with(binding.webView) { if (canGoBack()) goBack() }
    }
}