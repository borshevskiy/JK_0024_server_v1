package com.template

import android.os.Build
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.template.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_NoActionBar)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWebView()
        with(binding.webView) {
            if (savedInstanceState != null) restoreState(savedInstanceState)
            else getSharedPreferences(APP_SETTINGS, MODE_PRIVATE).getString(SERVER_URL, "")?.let { loadUrl(it) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        with(binding.webView) { if (canGoBack()) goBack() }

    }

    private fun setupWebView() {
        with(binding.webView) {
            if (Build.VERSION.SDK_INT >= 21) CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
            else CookieManager.getInstance().setAcceptCookie(true)
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
        }
    }
}