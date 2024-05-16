// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
	repositories {
		google()
		mavenCentral()

	}
	dependencies {
		classpath(libs.gradle)
		classpath(libs.kotlin.gradle.plugin)
		classpath(libs.hilt.android.gradle.plugin)
	}
}

plugins {
	id("com.google.devtools.ksp") version "1.9.0-1.0.13"
}

allprojects {
	repositories {
		google()
		mavenCentral()
	}
}

task<Delete>("clean") {
	delete = setOf(rootProject.buildDir)
}
