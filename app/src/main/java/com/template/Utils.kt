package com.template

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build.*
import android.os.Build.VERSION.RELEASE
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.BuildConfig
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

const val APP_SETTINGS = "App settings"
const val IS_STARTED_UP = "Is started up"
const val SERVER_URL = "server url"
const val CHANNEL_ID = "notificationChannel"
const val CHANNEL_NAME = "channelName"
const val DOCUMENT = "database/check"
const val USER_AGENT = "User-Agent"
const val CODE_403 = 403
const val CODE_200 = 200


fun String.removeBraces() = this.replace("[", "").replace("]", "")

@RequiresApi(VERSION_CODES.M)
fun internetAvailable(context: Context) = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetwork != null

fun getUrl(context: Context, document: DocumentSnapshot) = String.format(
    context.getString(R.string.url),
    document.data?.values.toString().removeBraces(),
    BuildConfig.APPLICATION_ID,
    UUID.randomUUID(),
    TimeZone.getDefault().id
)

fun getUserAgent(context: Context) = String.format(
    Locale.US, context.getString(R.string.user_agent),
    RELEASE, MODEL, BRAND, DEVICE, Locale.getDefault().language)