import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinCocoapods)
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
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
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
            baseName = "SmartcarSDK"

            // Optional properties
            // Specify the framework linking type. It's dynamic by default.
            isStatic = true
            // Dependency export
            // Uncomment and specify another project module if you have one:
            // export(project(":<your other KMP module>"))
            transitiveExport = false // This is default.
        }

        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
                implementation(libs.kermit)
                implementation(libs.kable.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
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
            isTestCoverageEnabled = true
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    publishing {
        // Configuring the release variant for publishing artifacts
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    androidTestImplementation("androidx.annotation:annotation:1.9.1")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test:rules:1.6.1")
    testAnnotationProcessor("com.google.auto.service:auto-service:1.0-rc4")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.1.0")
    testImplementation("org.robolectric:robolectric:4.13")
}

/**
 * Configures packaging of modules/artifacts to publish.
 *
 * @plugin maven-publish
 */
publishing {
    publications {
        create<MavenPublication>("release") {
            // This block is deferred until after evaluation to configure components
            afterEvaluate {
                from(components["release"])
            }

            groupId = libGroup
            artifactId = libName
            version = libVersion

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

/**
 * Requires the following environment variables to be set:
 *  - ORG_GRADLE_PROJECT_signingKey
 *  - ORG_GRADLE_PROJECT_signingPassword
 *
 * @see https://docs.gradle.org/current/userguide/signing_plugin.html#sec:in-memory-keys
 */
afterEvaluate {
    // Only configure signing if not running publishToMavenLocal
    if (!gradle.startParameter.taskNames.any { it.contains("publishToMavenLocal") }) {
        signing {
            // Cast findProperty results to String? as needed.
            useInMemoryPgpKeys(
                findProperty("signingKey") as String?,
                findProperty("signingPassword") as String?
            )
            sign(publishing.publications["release"])
        }
    }
}
