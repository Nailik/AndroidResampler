@file:Suppress("UnstableApiUsage")

import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("com.vanniktech.maven.publish") version "0.25.3"
}


mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01)
    signAllPublications()
}

mavenPublishing {
    coordinates("io.github.nailik", "androidresampler", "0.1")

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
    compileSdk = 34

    defaultConfig {
        minSdk = 1
        aarMetadata {
            minCompileSdk = 1
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
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
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