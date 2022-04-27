package com.template

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import com.template.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        const val APP_SETTINGS = "App settings"
        const val IS_STARTED_UP = "Is started up"
    }
//    private lateinit var FIREBASE_URL: String
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)

        if (!preferences.contains(IS_STARTED_UP)) {
            val intent = Intent(this, LoadingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
//        CustomTabsIntent.Builder().build().launchUrl(this, Uri.parse(FIREBASE_URL))
    }
}