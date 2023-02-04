package com.jessosborn.simpleweather.utils

fun String.isValidZip(): Boolean = this.isUsZip() || this.isCanadianZip() || this.isUkZip()

fun String.isInvalidZip(): Boolean = !this.isValidZip()

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
        zip.isUsZip() -> CountryCode.US.code
        zip.isCanadianZip() -> CountryCode.CANADA.code
        zip.isUkZip() -> CountryCode.UK.code
        else -> "NULL"
    }
}

enum class CountryCode(val code: String) {
    US("us"),
    CANADA("ca"),
    UK("gb")
}