import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val APACHE_HTTP_CLIENT = "4.5.13"
    const val JAXB = "2.4.0-b180830.0359"
    const val JJWT = "0.9.1"
    const val KOTEST = "5.1.0"
    const val KOTEST_SPRING = "1.1.0"
    const val KOTEST_TC = "1.2.1"
    const val KOTEST_WIREMOCK = "1.0.3"
    const val LOGBACK = "1.2.0"
    const val MOCKK = "1.12.3"
    const val SLF4J = "1.7.36"
    const val TC_MONGO = "1.16.3"
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

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

val integrationImplementation: Configuration by configurations.creating {
    extendsFrom(configurations.testImplementation.get())
}

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

    integrationImplementation("io.kotest", "kotest-assertions-core-jvm", Versions.KOTEST)
    integrationImplementation("io.kotest", "kotest-assertions-json", Versions.KOTEST)
    integrationImplementation("io.kotest.extensions", "kotest-extensions-spring", Versions.KOTEST_SPRING)
    integrationImplementation("io.kotest.extensions", "kotest-extensions-testcontainers", Versions.KOTEST_TC)
    integrationImplementation("org.apache.httpcomponents", "httpclient", Versions.APACHE_HTTP_CLIENT)
    integrationImplementation("org.testcontainers", "mongodb", Versions.TC_MONGO)
}

sourceSets {
    create("integration") {
        compileClasspath += project.sourceSets["main"].output + project.sourceSets["test"].output
        runtimeClasspath += project.sourceSets["main"].output + project.sourceSets["test"].output
        java.srcDir("src/integration/kotlin")
        resources.srcDir("src/integration/resources")
    }
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
    create<Test>("integration") {
        testClassesDirs = sourceSets["integration"].output.classesDirs
        classpath = sourceSets["integration"].runtimeClasspath
        mustRunAfter("test")
    }
    check {
        dependsOn("integration")
    }
}
