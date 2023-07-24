@file:Suppress("UnstableApiUsage")

import java.net.URI

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    `maven-publish`
    signing
}

    publishing {
        publications {
            register<MavenPublication>("maven") {
                groupId = "io.github.nailik"
                artifactId = "androidresampler"
                version = "0.1"

                artifact("$buildDir/outputs/aar/${artifactId}-release.aar")

                pom {
                    name = "androidresampler"
                    packaging = "aar"
                    description =
                        "This is a simple Audio Resampler using Oboe Resampler in order to change Channel and SampleRate live."
                    url = "https://github.com/Nailik/AndroidResampler"
                    developers {
                        developer {
                            id = "Nailik"
                            name = "Kilian"
                        }
                    }
                    scm {
                        connection = "scm:git:git://github.com/Nailik/AndroidResampler.git"
                        developerConnection = "scm:git:ssh://github.com/Nailik/AndroidResampler.git"
                        url = "https://github.com/Nailik/AndroidResampler"
                    }
                }
            }
        }

        repositories {
            maven {

                credentials {
                    username = project.properties["NEXUS_USERNAME"].toString()
                    password = project.properties["NEXUS_PASSWORD"].toString()
                }

                name = "io.github.nailik.androidresampler"
                url = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
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