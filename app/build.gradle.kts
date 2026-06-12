import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("jacoco")
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
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jessosborn.weatherapp"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "${apiProperties["apikey"]}")
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as? String
            keyPassword = keystoreProperties["keyPassword"] as? String
            storeFile = (keystoreProperties["storeFile"] as? String)?.let { file(it) }
            storePassword = keystoreProperties["storePassword"] as? String
        }
    }

    buildTypes {
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter =
        listOf(
            "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
            "**/*Test*.*", "android/**/*.*", "**/*_Impl*.*", "**/*_ViewBinding*.*",
            "**/*MembersInjector*.*", "**/*_Factory*.*", "**/*_Provide*Factory*.*",
            "**/*_HiltModules*.*", "**/*Hilt_*.class", "**/Hilt_*.class",
            "**/view/**",
        )

    val debugTree =
        fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
            exclude(fileFilter)
        }
    val mainSrc = layout.projectDirectory.dir("src/main/java")

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(
        fileTree(layout.buildDirectory) {
            include("jacoco/testDebugUnitTest.exec", "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
        },
    )

    doLast {
        val reportFile = reports.html.outputLocation.asFile.get().resolve("index.html")
        if (reportFile.exists()) {
            val os = System.getProperty("os.name").lowercase()
            when {
                os.contains("win") -> ProcessBuilder("cmd", "/c", "start", reportFile.absolutePath).start()
                os.contains("mac") -> ProcessBuilder("open", reportFile.absolutePath).start()
                os.contains("linux") -> ProcessBuilder("xdg-open", reportFile.absolutePath).start()
            }
        }
    }
}

dependencies {

    // AndroidX
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3.android)
    implementation(libs.hilt.navigation.compose)
    debugImplementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Glance
    implementation(libs.glance.appwidget)

    // Graphs
    implementation(libs.ycharts)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
}
