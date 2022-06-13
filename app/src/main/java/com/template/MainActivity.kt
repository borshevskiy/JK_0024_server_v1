package com.template

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.template.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var analytics: FirebaseAnalytics

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (internetAvailable(this)) {
            val preferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)
            analytics = FirebaseAnalytics.getInstance(this)
            if (preferences.contains(SERVER_URL)) {
                startActivity(Intent(this, WebActivity::class.java))
            } else if (!preferences.contains(IS_STARTED_UP)) {
                val intent = Intent(this, LoadingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }
}