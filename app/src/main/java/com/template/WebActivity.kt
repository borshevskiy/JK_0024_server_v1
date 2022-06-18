package com.template

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.CookieManager
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

        if (preferences.contains(COOKIES)) {
            cookieManager.removeAllCookies(null)
            preferences.getString(COOKIES, EMPTY)!!.split(DIVIDER).forEach {
                cookieManager.setCookie(preferences.getString(URL, EMPTY), it)
            }
        }


        with(binding.webView) {
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true

            if (savedInstanceState != null) restoreState(savedInstanceState)
            else loadUrl(preferences.getString(SERVER_URL, EMPTY)!!)

            cookieManager.setAcceptThirdPartyCookies(this, true)
            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    val cookies = cookieManager.getCookie(url)
                    if(cookies.contains(USER_ID) && !cookies.contains(EMPTY_USER)) {
                        preferences.edit().putString(COOKIES, cookies).apply()
                        preferences.edit().putString(URL, url).apply()
                    }
                    super.onPageFinished(view, url)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        with(binding.webView) { if (canGoBack()) goBack() }
    }
}
