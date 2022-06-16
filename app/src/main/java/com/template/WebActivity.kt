package com.template

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.template.databinding.ActivityWebBinding


class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding
    private lateinit var preferences: SharedPreferences

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_NoActionBar)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)

        with(binding.webView) {
            val cookieManager = CookieManager.getInstance()
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            settings.databaseEnabled = true

            if (savedInstanceState != null) restoreState(savedInstanceState)
            else loadUrl(getSharedPreferences(APP_SETTINGS, MODE_PRIVATE).getString(SERVER_URL, EMPTY)!!)

            cookieManager.setAcceptThirdPartyCookies(this, true)
            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (cookieManager.getCookie(url).contains("PHPSESSID")) {
                        val map = cookieManager.getCookie(url).split(",").associate {
                            val (left, right) = it.split("=")
                            left to right
                        }
                        Log.d("TEST123", map.toString())
                    }
                    view?.loadUrl(url!!, mapOf("PHPSESSID" to "uf6cfgfnpaq9k34b3iv8or88o6"))
                    return false
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    Log.d("TEST", url.toString())
                    Log.d("TEST", cookieManager.getCookie(url))
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
