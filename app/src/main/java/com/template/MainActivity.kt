package com.template

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.google.firebase.analytics.FirebaseAnalytics
import com.template.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        const val APP_SETTINGS = "App settings"
        const val IS_STARTED_UP = "Is started up"
        const val SERVER_URL = "server url"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var analytics: FirebaseAnalytics

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)

        if ((getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetwork != null) {
            analytics = FirebaseAnalytics.getInstance(this)
            if (preferences.contains(SERVER_URL)) {
                CustomTabsIntent.Builder().setToolbarColor(R.color.black).build().launchUrl(this, Uri.parse(preferences.getString(SERVER_URL, "")))
                finish()
            } else if (!preferences.contains(IS_STARTED_UP)) {
                val intent = Intent(this, LoadingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }
}