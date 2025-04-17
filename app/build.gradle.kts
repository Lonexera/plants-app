plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.plantsapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.plantsapp"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
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
    // Compose UI
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation)
    // Glide
    implementation(libs.bumptech.glide)
    implementation(libs.bumptech.glide.compose)
    // Shimmering
    implementation(libs.facebook.shimmer)
    // Hilt
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
    // Timber
    implementation(libs.jakewharton.timber)
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    // WorkManager
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
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
    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
}