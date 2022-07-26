plugins {
	id("org.springframework.boot") version "2.3.9.RELEASE"
	id("io.spring.dependency-management") version "1.0.12.RELEASE"
	id("java")
}

group = "com.mcbanners"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(8))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(platform("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR10"))

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("com.github.ben-manes.caffeine:caffeine:3.1.1")
	implementation("org.flywaydb:flyway-core:9.0.2")
	implementation("org.mindrot:jbcrypt:0.4")
	implementation("io.jsonwebtoken:jjwt-api:0.11.2")

	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks {
	bootJar {
		archiveFileName.set("userapi.jar")
	}
}
