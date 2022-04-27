package com.template

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.template.databinding.ActivityLoadingBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences(MainActivity.APP_SETTINGS, MODE_PRIVATE)

        analytics = Firebase.analytics
        Firebase.database.reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
//                preferences.edit().putBoolean(IS_STARTED_UP, true).apply()
                var firebaseDomain = ""
                val matcher = Patterns.WEB_URL.matcher(snapshot.value.toString())
                if (matcher.find()) firebaseDomain = matcher.group()
                val url = "$firebaseDomain/?packageid=${BuildConfig.APPLICATION_ID}&usserid=${UUID.randomUUID()}&getz=${TimeZone.getDefault().id}&getr=utm_source=google-play&utm_medium=organic"
            }

            override fun onCancelled(error: DatabaseError) {
                startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
            }
        })

        try {
            val test = OkHttpClient().newCall(Request.Builder()
                .url("https://angryy.xyz/?packageid=com.template&usserid=e91ba3ec-6181-456d-a98d-4bbab7f1d2b8&getz=Asia/Omsk&getr=utm_source=google-play&utm_medium=organic")
                .build()).execute().body.toString()
            Log.d("TEST", test)
        } catch (e: Exception) {

        }
    }
}