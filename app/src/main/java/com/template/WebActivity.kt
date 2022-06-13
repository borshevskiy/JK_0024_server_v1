package com.template

import android.content.SharedPreferences
import android.os.Bundle
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
    }

    private fun setupWebView() {
        with(binding.webView) {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            getSharedPreferences(APP_SETTINGS, MODE_PRIVATE).getString(SERVER_URL, "")?.let { loadUrl(it) }
        }
    }
}