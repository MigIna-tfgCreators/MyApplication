plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
}


android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    buildFeatures.viewBinding = true
    buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField ("String", "API_KEY", "\"${property("API_KEY")}\"")
        buildConfigField ("String", "BASE_URL", "\"${property("BASE_URL")}\"")
        buildConfigField ("String", "BASE_IMAGE_URL", "\"${property("BASE_IMAGE_URL")}\"")

        buildConfigField("int", "IMAGE_WIDTH", property("IMAGE_WIDTH") as String)
        buildConfigField ("int", "IMAGE_HEIGHT", property("IMAGE_HEIGHT") as String)
        buildConfigField ("double", "MAX_RATING", property("MAX_RATING") as String)
        buildConfigField("String", "REGION", "\"${property("REGION")}\"")
        buildConfigField("String", "LANGUAGE", "\"${property("LANGUAGE")}\"")

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
}

dependencies {

    //Firebase
    implementation(libs.firebase.auth)
    implementation(libs.firebase.bom)
    implementation(libs.firebase.firestore.ktx)

    //Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //Viewmodel
    implementation(libs.lifecycle.viewmodel.ktx)

    //Navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    //Fragments and navigation
    implementation(libs.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Glide
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    //Additions
    implementation (libs.circularprogressindicator)
    implementation (libs.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


}
