package com.example.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.weatherapp.App.Companion.context


const val baseApiUrl: String = "https://api.openweathermap.org/data/2.5/"

fun isValidZip(text: String): Boolean {
    val scrubbedZip = text.replace(" ", "").uppercase()
    return isUsZip(scrubbedZip)
}

fun isUsZip(zip: String): Boolean {
    val americanZipFormat = Regex("^[0-9]{5}\$")
    return americanZipFormat.matches(zip)
}

fun isCanadianZip(zip: String): Boolean {
    val canadianZipFormat = Regex("^[ABCEGHJKLMNPRSTVXY]\\d[A-Z] *\\d[A-Z]\\d\$")
    return canadianZipFormat.matches(zip)
}

fun isOnline(): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
        if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            || (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
            || (it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        ) {
            return true
        }
    }
    return false
}