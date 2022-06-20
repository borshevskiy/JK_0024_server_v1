package com.template

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.template.databinding.ActivityLoadingBinding
import okhttp3.*
import java.io.IOException

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)

        Firebase.firestore.document(DOCUMENT).get().addOnSuccessListener {
            val data = it.data
            preferences.edit().putBoolean(IS_STARTED_UP, true).apply()
            if (!data.isNullOrEmpty() && data.containsKey(KEY) && data[KEY] != EMPTY) getData(getUrl(this, data[KEY].toString()))
            else startActivity(Intent(this, MainActivity::class.java))
        }.addOnFailureListener { startActivity(Intent(this, MainActivity::class.java)) }
    }

    private fun getData(url: String) {
        OkHttpClient().newCall(Request.Builder().url(url)
            .header(USER_AGENT, getUserAgent(this))
            .build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { }

            override fun onResponse(call: Call, response: Response) {
                when (response.code()) {
                    CODE_403 -> startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
                    CODE_200 -> {
                        runOnUiThread {
                            startActivity(Intent(this@LoadingActivity, WebActivity::class.java))
                            preferences.edit().putString(SERVER_URL, response.body()!!.string()).apply()
                        }
                    }
                }
            }
        })
    }
}