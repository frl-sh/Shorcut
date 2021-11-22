// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Plugins.buildTools)
        classpath(Plugins.kotlinGradle)
        classpath(Plugins.safeArgs)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}