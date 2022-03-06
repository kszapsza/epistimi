import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val JAXB = "2.4.0-b180830.0359"
    const val JJWT = "0.9.1"
    const val KOTEST = "5.1.0"
    const val LOGBACK = "1.2.0"
    const val MOCKK = "1.12.3"
    const val SLF4J = "1.7.36"
}

plugins {
    id("org.springframework.boot") version "2.5.9"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

group = "pl.edu.wat.wcy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback", "logback-classic", Versions.LOGBACK)
    implementation("ch.qos.logback", "logback-core", Versions.LOGBACK)
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    implementation("io.jsonwebtoken", "jjwt", Versions.JJWT)
    implementation("javax.xml.bind", "jaxb-api", Versions.JAXB)
    implementation("org.jetbrains.kotlin", "kotlin-reflect")
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
    implementation("org.slf4j", "slf4j-api", Versions.SLF4J)
    implementation("org.springframework.boot", "spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot", "spring-boot-starter-security")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    testImplementation("io.kotest", "kotest-assertions-core", Versions.KOTEST)
    testImplementation("io.kotest", "kotest-property", Versions.KOTEST)
    testImplementation("io.kotest", "kotest-runner-junit5", Versions.KOTEST)
    testImplementation("io.mockk", "mockk", Versions.MOCKK)
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }
}
