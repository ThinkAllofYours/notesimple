plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Spring Web (MVC)
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Spring Data JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// PostgreSQL Driver
	runtimeOnly("org.postgresql:postgresql")

	// Spring Boot Actuator
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	// Validation
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Spring Boot DevTools (개발용, 배포시에는 제외)
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// Spring Boot Starter AOP
	implementation("org.springframework.boot:spring-boot-starter-aop")

	// Spring Data Elasticsearch
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")

	testImplementation("com.h2database:h2")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
