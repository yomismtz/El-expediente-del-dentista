plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

val uploadKeystorePath = System.getenv("YSM_KEYSTORE_PATH")

android {
    namespace = "com.yomismtz.expedientedeldentista"
    compileSdk = 36
    testBuildType = "preview"

    defaultConfig {
        applicationId = "com.yomismtz.expedientedeldentista"
        minSdk = 26
        targetSdk = 36
        versionCode = 46
        versionName = "0.46"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (!uploadKeystorePath.isNullOrBlank()) {
            create("releaseUpload") {
                storeFile = file(uploadKeystorePath)
                storePassword = System.getenv("YSM_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("YSM_KEY_ALIAS")
                keyPassword = System.getenv("YSM_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        create("preview") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".preview"
            versionNameSuffix = "-preview"
            matchingFallbacks += listOf("debug")
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            if (!uploadKeystorePath.isNullOrBlank()) {
                signingConfig = signingConfigs.getByName("releaseUpload")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("io.coil-kt:coil-compose:2.7.0")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test:core-ktx:1.6.1")

    debugImplementation("androidx.compose.ui:ui-tooling")
    "previewImplementation"("androidx.compose.ui:ui-tooling")
}
