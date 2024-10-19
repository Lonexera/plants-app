plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
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

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.fragment:fragment-ktx:1.8.4")

    // Hilt
    val hilt = "2.52"
    implementation("com.google.dagger:hilt-android:$hilt")
    kapt("com.google.dagger:hilt-android-compiler:$hilt")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")

    // viewBinding delegate
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.4.7")

    // Statistics Contract
    implementation(project(":plantStatsContract"))
    // Domain Module
    implementation(project(":domain"))
}