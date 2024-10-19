plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.plantsapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.plantsapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Statistics Contract
    implementation(project(":plantStatsContract"))
    // Domain Module
    implementation(project(":domain"))

    // UI
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)
    // viewBinding delegate
    implementation(libs.kirich1409.viewbinding.delegate)
    // Glide
    implementation(libs.bumptech.glide)
    // Shimmering
    implementation(libs.facebook.shimmer)
    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    // Timber
    implementation(libs.jakewharton.timber)
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    // WorkManager
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)
    // Firebase
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.firestore)
    implementation(libs.google.firebase.storage)
    implementation(libs.google.firebase.auth.ktx)
    // Play services
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.android.gms.play.services.auth)
    // JUnit 4 framework
    testImplementation(libs.junit)
    testImplementation(libs.assertj)
    testImplementation(libs.test.coroutines)
}