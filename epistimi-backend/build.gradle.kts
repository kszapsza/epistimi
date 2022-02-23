import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("groovy")
    id("org.springframework.boot") version "2.5.9"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.32"
    kotlin("plugin.spring") version "1.5.32"
}

group = "pl.edu.wat.wcy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.2.10")
    implementation("ch.qos.logback:logback-core:1.2.10")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.spockframework:spock-core:2.0-groovy-3.0")
    testImplementation("org.spockframework:spock-spring:2.1-M2-groovy-3.0")
    testImplementation("org.codehaus.groovy:groovy-all:3.0.8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("cglib:cglib-nodep:3.3.0")
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
    named<GroovyCompile>("compileTestGroovy") {
        classpath += files(compileTestKotlin.get().destinationDirectory)
    }
}
