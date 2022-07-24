package com.jessosborn.simpleweather.utils

fun String.isValidZip(): Boolean {
    return this.isUsZip() || this.isCanadianZip() || this.isUkZip()
}

fun String.isUsZip(): Boolean {
    val americanZipFormat = Regex("^[0-9]{5}\$")
    return americanZipFormat.matches(this.uppercase())
}

fun String.isCanadianZip(): Boolean {
    val canadianZipFormat = Regex("^[ABCEGHJKLMNPRSTVXY][0-9][A-Z]\$")
    return canadianZipFormat.matches(this.uppercase())
}

fun String.isUkZip(): Boolean {
    val ukZipFormat = Regex("^[A-Z]{1,2}[0-9R][0-9A-Z]?\$")
    return ukZipFormat.matches(this.uppercase())
}

fun getCountryFromZip(zip: String): String {
    return when {
        zip.isUsZip() -> "us"
        zip.isCanadianZip() -> "ca"
        zip.isUkZip() -> "gb"
        else -> "NULL"
    }
}
