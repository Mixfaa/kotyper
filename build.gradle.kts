plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "ua.crushme"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.beust:klaxon:5.5")


    implementation("io.github.spair:imgui-java-app:1.86.10")


    platform("org.lwjgl:lwjgl-bom:3.3.3")

    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")
    runtimeOnly("org.lwjgl:lwjgl::natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-glfw::natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-opengl::natives-windows")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}