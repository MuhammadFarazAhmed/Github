plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    kotlin("plugin.serialization") version "1.9.0"
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"
}

android {
    namespace = "com.example.github"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.github"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.11.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("io.insert-koin:koin-androidx-compose:3.4.0")
    implementation("io.insert-koin:koin-core:3.4.0")

    implementation("io.ktor:ktor-client-core:2.1.0")
    implementation("io.ktor:ktor-client-android:2.1.0")
    implementation("io.ktor:ktor-client-serialization:2.1.0")
    implementation("io.ktor:ktor-serialization-gson:2.1.0")
    implementation("io.ktor:ktor-client-logging:2.1.0")
    implementation("io.ktor:ktor-client-cio:2.1.0")

    implementation("io.ktor:ktor-client-okhttp:2.1.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.0")
    implementation("io.ktor:ktor-client-auth:2.1.0")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.0")

    implementation("androidx.room:room-runtime:2.5.2")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    ksp("androidx.room:room-compiler:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")

}