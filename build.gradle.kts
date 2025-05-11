// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.room) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.kotlinAndroidKsp) apply false
    alias(libs.plugins.gradle.ktlint) apply false
}

task<Delete>("clean") {
    delete = setOf(getLayout().buildDirectory)
}
