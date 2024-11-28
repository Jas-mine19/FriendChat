plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.friendchat"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.friendchat"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}



dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.datastore.preferences)
    implementation(libs.androidx.room.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    implementation (libs.androidx.fragment.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.hilt.android.v244)
    implementation(libs.firebase.appcheck.playintegrity)
    testImplementation(libs.testng)
    kapt (libs.hilt.compiler.v244)
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler.v2511)

    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    implementation (libs.firebase.bom.v3211)
    implementation (libs.firebase.appcheck)
    implementation (libs.firebase.appcheck.playintegrity.v1701)
    // Room
    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)

    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation(libs.shimmer)
    implementation(libs.firebase.appcheck.playintegrity.v1600)
    implementation (libs.glide)
    kapt (libs.compiler)
    implementation( libs.firebase.bom.v3200)
    implementation (libs.firebase.messaging)



}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kapt {
    correctErrorTypes = true
}
