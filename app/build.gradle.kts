plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    kotlin("android")
    kotlin("kapt")
    // Kotlin - Deprecated
    // kotlin("android.extensions")
    id("kotlin-parcelize")
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "29.0.3"
    defaultConfig {
        applicationId = "com.udacity.asteroidradar"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildTypes.forEach {
        it.buildConfigField("String", "NasaAPIKey", properties["NasaAPIKey"].toString())
    }

    buildFeatures {
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        useIR = true
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin
    // implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    // Kotlin - Deprecated - No more required with Kotlin 1.4.0
    // implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")

    // Support libraries
    implementation("androidx.appcompat:appcompat:${Androidx.appcompat}")
    implementation("androidx.fragment:fragment-ktx:${Androidx.fragment}")

    // Android KTX
    implementation("androidx.core:core-ktx:${Androidx.core}")

    // Constraint Layout
    implementation("androidx.constraintlayout:constraintlayout:${Androidx.constraintlayout}")

    // ViewModel and LiveData (arch components)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Androidx.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Androidx.lifecycle}")

    // Navigation
    implementation("android.arch.navigation:navigation-fragment-ktx:${Android.Arch.navigation}")
    implementation("android.arch.navigation:navigation-ui-ktx:${Android.Arch.navigation}")

    // OkHttp - logging
    implementation("com.squareup.okhttp3:logging-interceptor:${Squareup.okhttp}")

    // Moshi
    implementation("com.squareup.moshi:moshi:${Squareup.moshi}")
    implementation("com.squareup.moshi:moshi-kotlin:${Squareup.moshi}")

    // Retrofit - Deprecated plugins - No more required
    // implementation("com.squareup.retrofit2:retrofit:${Squareup.retrofit}")
    // implementation("com.squareup.retrofit2:converter-scalars:${Squareup.retrofit}")

    // Retrofit with Moshi Converter
    implementation("com.squareup.retrofit2:converter-moshi:${Squareup.retrofit}")

    // Coroutines - Deprecated - No more required
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")

    // Retrofit Coroutines Support - Deprecated - No more required
    // implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:${Androidx.recyclerview}")

    // Picasso for images
    implementation("com.squareup.picasso:picasso:${Squareup.picasso}")

    // Room database
    implementation("androidx.room:room-runtime:${Androidx.room}")
    kapt("androidx.room:room-compiler:${Androidx.room}")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:${Androidx.room}")

    // WorkManager
    implementation("android.arch.work:work-runtime-ktx:${Android.Arch.work}")

    // Logging
    implementation("com.jakewharton.timber:timber:$timber")

    testImplementation("junit:junit:$junit")
    androidTestImplementation("androidx.test.ext:junit:${Androidx.Test.junit}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Androidx.Test.espresso}")
}
