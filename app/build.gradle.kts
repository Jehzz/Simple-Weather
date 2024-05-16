import java.io.FileInputStream
import java.util.Properties

plugins {
	id("com.android.application")
	kotlin("android")
	id("com.google.devtools.ksp")
	id("dagger.hilt.android.plugin")
}

// Load keystore
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
	load(FileInputStream(keystorePropertiesFile))
}

// Load the API Key
val apiKeyFile = rootProject.file("apikey.properties")
val apiProperties = Properties().apply {
	load(apiKeyFile.inputStream())
}

android {
	namespace = "com.jessosborn.simpleweather"
	compileSdk = 34
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
		targetSdk = 34
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
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.2"
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
	implementation(libs.androidx.lifecycle.viewmodel.ktx)
	implementation(libs.androidx.core.splashscreen)
	implementation(libs.androidx.material3.android)

	//Compose
	platform(libs.androidx.compose.bom)
	implementation(libs.activity.compose)
	implementation(libs.androidx.animation)
	implementation(libs.androidx.material)
	implementation(libs.ui.tooling)
	implementation(libs.androidx.runtime.livedata)
	implementation(libs.androidx.material.icons.core)
	implementation(libs.androidx.material.icons.extended)
	implementation(libs.material3)
	implementation(libs.androidx.hilt.navigation.compose)
	implementation(libs.androidx.lifecycle.viewmodel.compose)
	implementation(libs.androidx.navigation.compose)

	//Coil
	implementation(libs.coil.compose)

	//DataStore
	implementation(libs.androidx.datastore.preferences)

	//Hilt
	implementation(libs.hilt.android)
	ksp(libs.hilt.android.compiler)

	//Retrofit
	implementation(libs.retrofit)
	implementation(libs.converter.gson)
	implementation(libs.logging.interceptor)

	//Testing
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.rules)
	testImplementation(libs.junit)
	testImplementation(libs.mockito.core)
	androidTestImplementation(libs.ui.test.junit4)
}
