import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.dokka)
    `maven-publish`
    signing
}

val libGroup: String by project
val libName: String by project
val libVersion: String by project
val libDescription: String by project

group = libGroup
version = libVersion

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        // Required properties
        // Specify the required Pod version here
        // Otherwise, the Gradle project version is used
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"

        // Optional properties
        // Configure the Pod name here instead of changing the Gradle project name
        name = "SmartcarSDKShared"

        framework {
            // Required properties
            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
            baseName = "SmartcarFramework"

            // Optional properties
            // Specify the framework linking type. It's dynamic by default.
            isStatic = false
            // Dependency export
            // Uncomment and specify another project module if you have one:
            // export(project(":<your other KMP module>"))
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            transitiveExport = false // This is default.
        }

        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kermit)
                implementation(libs.kable.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.android.ktx)
                implementation(libs.android.activity)
                implementation(libs.kotlinx.coroutines.android)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(libs.mockito.core)
                implementation(libs.robolectric)
            }
        }
    }
}

android {
    namespace = "com.smartcar.sdk"

    defaultConfig {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        buildConfigField("String", "VERSION_NAME", "\"$version\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    lint {
        disable += "UnusedResources"
    }

    buildTypes {
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

/**
 * Configures packaging of modules/artifacts to publish.
 *
 * @plugin maven-publish
 */
publishing {
    publications {
        afterEvaluate {
            named<MavenPublication>("androidRelease") {
                groupId = libGroup
                artifactId = libName
                version = libVersion

                artifact(javadocJar)

                pom {
                    // https://central.sonatype.org/publish/requirements/#sufficient-metadata
                    name.set(libName)
                    description.set(libDescription)
                    url.set("https://github.com/smartcar/android-sdk")
                    packaging = "aar"

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }

                    developers {
                        developer {
                            id.set("smartcar")
                            name.set("Smartcar")
                            email.set("hello@smartcar.com")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com/smartcar/android-sdk.git")
                        developerConnection.set("scm:git:ssh://github.com:smartcar/android-sdk.git")
                        url.set("https://github.com/smartcar/android-sdk.git")
                    }
                }
            }
        }
    }
}

// Task to generate documentation with Dokka and package it as a JAR
val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
}

/**
 * Requires the following environment variables to be set:
 *  - ORG_GRADLE_PROJECT_signingKey
 *  - ORG_GRADLE_PROJECT_signingPassword
 *
 * @see https://docs.gradle.org/current/userguide/signing_plugin.html#sec:in-memory-keys
 */
afterEvaluate {
    signing {
        // Only configure signing for public release
        setRequired {
            gradle.taskGraph.hasTask("publishAndroidReleasePublicationToSonatypeRepository")
        }
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["androidRelease"])
    }
}
