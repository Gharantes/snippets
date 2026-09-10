plugins {
    kotlin("jvm") version "2.2.0"
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.serialization") version "1.9.10"
}

group = "dev.personal"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(21)
    compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict") }
}
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")

    runtimeOnly("org.postgresql:postgresql")

    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-cio:2.3.4") // or ktor-client-okhttp if preferred
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}
