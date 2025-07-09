import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Properties
import java.util.TimeZone

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinSerialization)
}

val buildProperties = Properties()
val buildPropertiesFile = rootProject.file("build.properties")
if (buildPropertiesFile.exists()) {
    buildProperties.load(FileInputStream(buildPropertiesFile))
}
val appVersionCode = buildProperties.getProperty("versionCode", "1").toInt()
val appVersionName: String? = buildProperties.getProperty("versionName", "1.0.0")
val appProductName: String? = buildProperties.getProperty("productName", "arkhe")
val now: Instant? = Clock.systemUTC().instant()
val localDateTime: LocalDateTime? = now?.atZone(TimeZone.getDefault().toZoneId())?.toLocalDateTime()
val timestampFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("yyMMddHHmm")
val timestampString = localDateTime?.format(timestampFormatter)
val buildTimestamp = timestampString

android {
    namespace = "com.arkhe.rbi"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.arkhe.rbi"
        minSdk = 24
        targetSdk = 36
        versionCode = appVersionCode
        versionName = "$appVersionName.build$buildTimestamp"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    buildFeatures {
        compose = true
    }
}

base {
    archivesName.set("$appProductName-$appVersionName.build$buildTimestamp")
}

configurations.all {
    exclude(group = "com.google.ar")
    exclude(group = "com.google.ar", module = "core")
    exclude(group = "com.google.android.gms", module = "play-services-vision")
    exclude(group = "com.google.mlkit")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.slf4j)
}