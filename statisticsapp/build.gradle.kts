plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.example.statisticsapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.statisticsapp"
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
    // Hilt
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
    // Timber
    implementation(libs.jakewharton.timber)
}