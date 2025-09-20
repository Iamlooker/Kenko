/*
 * Copyright (C) 2025. LooKeR & Contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.android.app)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.looker.kenko"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.looker.kenko"
        minSdk = 26
        targetSdk = 36
        // 1.2.0 -> 102000
        // 1.12.2 -> 112020
        // 1.12.10 -> 112100
        versionCode = 103000
        versionName = "1.3.0"

        testInstrumentationRunner = "com.looker.kenko.KenkoTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    ksp {
        arg("room.generateKotlin", "true")
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    dependenciesInfo {
        includeInApk = false
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
            languageVersion = KotlinVersion.KOTLIN_2_2
            apiVersion = KotlinVersion.KOTLIN_2_2

            freeCompilerArgs.add("-Xcontext-parameters")

            optIn.add("kotlin.RequiresOptIn")
            optIn.add("kotlin.time.ExperimentalTime")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    sourceSets {
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }

    lint {
        disable += "MissingTranslation"
    }

    composeCompiler {
        metricsDestination = file("$projectDir/reports/metrics")
        reportsDestination = file("$projectDir/reports")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE*}"
            excludes += "DebugProbesKt.bin"
        }
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {

    kotlin("stdlib")
    implementation(libs.core.ktx)

    implementation(platform(libs.compose.bom))

    implementation(libs.bundles.lifecycle)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)

    implementation(libs.savedstate)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.coroutines)

    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.debug)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    implementation(libs.datastore)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    testImplementation(kotlin("test-junit5"))

    androidTestImplementation(kotlin("test-junit5"))
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.instrumented.test)
    androidTestImplementation(libs.room.test)
    androidTestImplementation(libs.hilt.test)

    kspAndroidTest(libs.hilt.test)
}

fun DependencyHandlerScope.kotlin(name: String): Any = kotlin(name, libs.versions.kotlin.get())
