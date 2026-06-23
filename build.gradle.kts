// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.2.10-1.0.29")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.59.2")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
