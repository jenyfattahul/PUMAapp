plugins {
    alias(libs.plugins.android.application)  // Using the version catalog alias
    id("com.google.gms.google-services")     // Firebase plugin
}

android {
    namespace = "com.example.himpuunananapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.himpuunananapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase dependencies
    implementation("com.google.firebase:firebase-auth:21.0.5")  // Firebase Authentication
    implementation("com.google.firebase:firebase-database:20.0.5")  // Firebase Realtime Database
    implementation("com.google.firebase:firebase-firestore:24.4.0")  // Firebase Firestore (Optional)
}
