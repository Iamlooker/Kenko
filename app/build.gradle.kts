/*
 * Copyright (C) 2026 LooKeR & Contributors
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

import java.util.*

plugins {
    alias(libs.plugins.android.app)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.hilt)
}

val keystoreProperties = Properties().apply {
    val keystorePropertiesFile = rootProject.file("keystore.properties")
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use(::load)
    }
}

android {
    namespace = "com.looker.kenko"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.looker.kenko"
        minSdk = 26
        targetSdk = 37
        versionName = "1.3.3"
        versionCode = versionCodeFor(versionName)
    }

    dependenciesInfo.includeInApk = false

    signingConfigs {
        if (keystoreProperties.isNotEmpty()) {
            create("release") {
                storeFile = file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs["release"]
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE*}"
            excludes += "DebugProbesKt.bin"
        }
    }

    lint.disable += "MissingTranslation"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")

        optIn.addAll(
            "kotlin.RequiresOptIn",
            "kotlin.time.ExperimentalTime",
        )
    }
}

composeCompiler {
    metricsDestination = file("$projectDir/reports/metrics")
    reportsDestination = file("$projectDir/reports")
}

room {
    generateKotlin = true
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.core.ktx)

    implementation(platform(libs.compose.bom))

    implementation(libs.bundles.lifecycle)
    implementation(libs.activity.compose)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.navigation3)

    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.debug)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    implementation(libs.datastore)

    implementation(libs.bundles.work)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
}

fun versionCodeFor(version: String?): Int? {
    if (version == null) return null
    val (major, minor, patch) = version
        .substringBefore('-')
        .trim()
        .split('.')
        .map { it.toUIntOrNull() }

    require(major != null && minor != null && patch != null) {
        "Each segment must be within 0..99 for mapping, was: '$version'"
    }

    return (major * 100_000u + minor * 1_000u + patch * 10u).toInt()
}
