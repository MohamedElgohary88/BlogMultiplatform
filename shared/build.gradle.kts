plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

group = "org.example.blogmultiplatform"
version = "1.0-SNAPSHOT"

kotlin {
    js(IR) { browser() }
    jvm()

    sourceSets {
        commonMain.dependencies {

        }

        jsMain.dependencies {

        }
        jvmMain.dependencies {

        }
    }
}
