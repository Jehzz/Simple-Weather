import java.util.Properties

plugins {
	id("com.android.application")
	kotlin("android")
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.hiltAndroid)
	alias(libs.plugins.kotlinAndroidKsp)
}

// Load keystore
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
	load(keystorePropertiesFile.inputStream())
}

// Load the API Key
val apiKeyFile = rootProject.file("apikey.properties")
val apiProperties = Properties().apply {
	load(apiKeyFile.inputStream())
}

android {
	namespace = "com.jessosborn.simpleweather"
	compileSdk = 35

	buildFeatures {
		compose = true
	}

	kotlinOptions {
		jvmTarget = JavaVersion.VERSION_1_8.toString()
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
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
}

dependencies {

	//AndroidX
	implementation(libs.androidx.activity.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.core.splashscreen)

	//Compose
	platform(libs.androidx.compose.bom)
	implementation(libs.compose.material)
	implementation(libs.compose.material.icons.extended)
	implementation(libs.compose.material3.android)
	implementation(libs.hilt.navigation.compose)
	implementation(libs.ui.tooling)

	//Coil image loading
	implementation(libs.coil.compose)

	//DataStore
	implementation(libs.androidx.datastore.preferences)

	//Hilt
	implementation(libs.hilt.android)
	ksp(libs.hilt.android.compiler)

	//Junit
	testImplementation(libs.junit)

	//Retrofit
	implementation(libs.retrofit)
	implementation(libs.converter.gson)
	implementation(libs.logging.interceptor)
}
