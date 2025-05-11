import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.kotlinAndroidKsp)
    alias(libs.plugins.room)
    alias(libs.plugins.gradle.ktlint)
}

// Load properties files
val keystoreProperties = Properties().apply { load(rootProject.file("keystore.properties").inputStream()) }
val apiProperties = Properties().apply { load(rootProject.file("apikey.properties").inputStream()) }

android {
    namespace = "com.jessosborn.simpleweather"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
        compose = true
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_10.toString()
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    defaultConfig {
        applicationId = "com.jessosborn.weatherapp"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "api_key", apiProperties["apikey"].toString())
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_10
        targetCompatibility = JavaVersion.VERSION_1_10
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {

    // AndroidX
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)

    // Compose
    platform(libs.androidx.compose.bom)
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.ui.tooling)

    // Coil image loading
    implementation(libs.coil.compose)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Glance
    implementation(libs.glance.appwidget)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Junit
    testImplementation(libs.junit)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
}
