import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.libraryapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.libraryapp"
        minSdk = 26
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.firebase:firebase-firestore:24.9.1")

    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")


    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation ("androidx.navigation:navigation-compose:2.4.1") // Usa la última versión disponible

    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")

    implementation ("androidx.navigation:navigation-compose:2.5.3")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

    implementation ("com.google.android.gms:play-services-auth:19.2.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation ("androidx.compose.material:material-icons-core:1.0.5")
    implementation ("androidx.compose.material:material-icons-extended:1.0.5")

    /*
    #####DEPENDENCIAS DE TERCEROS####
    DEPENDENCIAS = https://maxkeppeler.notion.site/Sheets-Compose-Dialogs-804f0ebcb2c84b98b7afa5f687295aed
    REPOSITORIO = https://github.com/maxkeppeler/sheets-compose-dialogs
    FICHERO = signUpView
     */
    // CORE
    implementation ("com.maxkeppeler.sheets-compose-dialogs:core:1.0.2")
    // CALENDAR
    implementation ("com.maxkeppeler.sheets-compose-dialogs:calendar:1.0.2")
    //LECTOR BARRAS
    //implementation ("com.journeyapps:zxing-android-embedded:4.1.0")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")

    /*
    #####FIN DEPENDENCIAS DE TERCEROS####
     */

    implementation ("androidx.compose.material:material:1.4.2")
    implementation ("com.google.dagger:hilt-android:2.48.1")
    kapt ("com.google.dagger:hilt-compiler:2.48.1")

    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    //Progress bar
    implementation ("androidx.activity:activity-ktx:1.4.0")

    implementation ("io.ktor:ktor-client-android:2.3.6")
    implementation ("io.ktor:ktor-client-json-jvm:2.3.6")
    implementation ("io.ktor:ktor-client-serialization-jvm:2.3.6")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.6")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.6")


    implementation ("com.google.accompanist:accompanist-pager:0.32.0")

    //Google Services & Maps
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.maps.android:maps-compose:2.9.0")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")


    //Accompanist (Permission)
    implementation ("com.google.accompanist:accompanist-permissions:0.31.3-beta")
    implementation ("com.vanniktech:android-image-cropper:4.5.0")

}

kapt {
    correctErrorTypes = true
}
hilt {
    enableExperimentalClasspathAggregation = true
}