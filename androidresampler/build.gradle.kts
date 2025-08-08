@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.mavenPublish)
}


mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
}

mavenPublishing {
    coordinates("io.github.nailik", "androidresampler", "0.3")

    pom {
        name.set("androidresampler")
        description.set("This is a simple Audio Resampler using Oboe Resampler in order to change Channel and SampleRate live.")
        inceptionYear.set("2023")
        url.set("https://github.com/Nailik/AndroidResampler")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("Nailik")
                name.set("Nailik")
                url.set("https://github.com/Nailik/")
            }
        }
        scm {
            url.set("https://github.com/Nailik/AndroidResampler/")
            connection.set("scm:git:git://github.com/Nailik/AndroidResampler.git")
            developerConnection.set("scm:git:ssh://git@github.com/Nailik/AndroidResampler.git")
        }
    }
}

android {
    namespace = "io.github.nailik.androidresampler"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        aarMetadata {
            minCompileSdk = 21
        }
        externalNativeBuild {
            cmake {
                arguments += listOf("-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON")
            }
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
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "4.0.3"
        }
    }
    testFixtures {
        enable = true
    }
    publishing {
        multipleVariants {
            allVariants()
            withJavadocJar()
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_22)
    }
}