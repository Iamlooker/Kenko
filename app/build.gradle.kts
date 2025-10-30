/*
 * Copyright (C) 2025 LooKeR & Contributors
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

import com.android.utils.text.dropPrefix
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
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
        versionName = "1.3.0"
        versionCode = versionCodeFor(versionName)

        testInstrumentationRunner = "com.looker.kenko.KenkoTestRunner"
    }

    room {
        generateKotlin = true
        schemaDirectory("$projectDir/schemas")
    }

    dependenciesInfo {
        includeInApk = false
    }

    val localProperties = rootProject.properties("local.properties")

    val releaseSigningConfig = signingConfigs.create("release") {
        storeFile = file(localProperties.getProperty("store.path"))
        storePassword = localProperties.getProperty("store.pass")
        keyAlias = localProperties.getProperty("key.alias")
        keyPassword = localProperties.getProperty("key.pass")
    }

    buildTypes {
        debug { applicationIdSuffix = ".debug" }
        release {
            signingConfig = releaseSigningConfig
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

fun Project.properties(fileName: String): Properties {
    val file = file(fileName)
    require(file.exists()) { "File not found: `${file.name}` at `${file.path}`" }

    val properties = Properties()
    properties.load(file.reader())
    return properties
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

val changelogMD by tasks.register("changelogMD") {
    group = "build"
    description = "Prepare CHANGELOG.md for release"
    notCompatibleWithConfigurationCache("Uses Android DSL and Project APIs in task action")

    doLast {
        val versionName = requireNotNull(android.defaultConfig.versionName)

        val changelogMd = rootProject.file("CHANGELOG.md")
        require(changelogMd.exists()) { "CHANGELOG.md not found at project root" }
        val lines = changelogMd.readLines()

        val unreleasedHeaderIdx = lines.indexOfFirst { it.startsWith("## [Unreleased]") }
        if (unreleasedHeaderIdx == -1) error("No [Unreleased] header found in CHANGELOG.md")

        if (lines.any { it.startsWith("## [$versionName") }) {
            logger.warn("Version $versionName already exists in CHANGELOG.md")
            return@doLast
        }

        val today = LocalDate.now(ZoneId.systemDefault()).toString()
        val newHeader = "## [$versionName] - $today"

        val updated = lines.toMutableList().apply {
            add(unreleasedHeaderIdx + 1, "\n" + newHeader)
        }
        changelogMd.writeText(updated.joinToString("\n"))
    }
}

val fastlaneChangelog by tasks.register("fastlaneChangelog") {
    group = "build"
    description = "Prepare Fastlane changelog from CHANGELOG.md and create metadata file"
    notCompatibleWithConfigurationCache("Uses Android DSL and Project APIs in task action")
    dependsOn(changelogMD)
    doLast {
        val vCode = requireNotNull(android.defaultConfig.versionCode) {
            "versionCode is not configured"
        }
        val vName = requireNotNull(android.defaultConfig.versionName) {
            "versionName is not configured"
        }

        val fastlaneFile = rootProject.file("metadata/en-US/changelogs/${vCode}.txt")
        if (fastlaneFile.exists()) {
            logger.warn("Fastlane changelog file already exists: ${fastlaneFile.path}")
            return@doLast
        }

        val changelogs = rootProject.file("CHANGELOG.md").readLines()

        var blockStartIndex = -1
        var blockEndIndex = changelogs.lastIndex

        for (i in changelogs.indices) {
            if (changelogs[i].startsWith("## [Unreleased]")) continue
            if (changelogs[i].startsWith("## [$vName")) {
                blockStartIndex = i + 1
                continue
            }
            if (changelogs[i].startsWith("## [") && blockStartIndex != -1) {
                blockEndIndex = i - 1
                break
            }
        }

        val unreleasedBlock = changelogs.subList(blockStartIndex, blockEndIndex + 1)

        val cleanedForFastlane = unreleasedBlock.joinToString("\n") { raw ->
            if (raw.startsWith('#')) raw.dropPrefix("### ").trim() else raw.trim()
        }.trim().ifEmpty { "No changes listed." }

        fastlaneFile.writeText(cleanedForFastlane)
    }
}

tasks.matching { it.name == "assembleRelease" }.configureEach { dependsOn(fastlaneChangelog) }
tasks.matching { it.name == "bundleRelease" }.configureEach { dependsOn(fastlaneChangelog) }
