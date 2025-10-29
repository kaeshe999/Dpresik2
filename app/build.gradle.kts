
    plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)

        // ESTA LÍNEA ES CRÍTICA:
        id("com.google.gms.google-services")
    }

    android {
        namespace = "com.example.dpresik2"
        compileSdk = 36

        defaultConfig {
            applicationId = "com.example.dpresik2"
            minSdk = 24
            targetSdk = 36
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
        kotlinOptions {
            jvmTarget = "1.8"
        }

        // Habilitar ViewBinding
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

        // --- DEPENDENCIAS DE FIREBASE (¡LAS IMPORTANTES!) ---
        implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
        implementation("com.google.firebase:firebase-auth-ktx")
        implementation("com.google.android.gms:play-services-auth:21.2.0")
        // ------------------------------------------

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }