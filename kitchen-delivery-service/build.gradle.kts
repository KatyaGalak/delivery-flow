plugins {
    id("java")
    id("org.springframework.boot") version "3.3.9"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "ru.ekaterina"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Spring Boot & Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.kafka:spring-kafka")

    implementation("org.springframework.boot:spring-boot-starter-security")

    // Camunda 7 Engine & Webapps (Cockpit, Tasklist)
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest:7.21.0")
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-webapp:7.21.0")

    // Database
    runtimeOnly("org.postgresql:postgresql")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}