plugins {
    java
    // Plugin para Spring Boot, necesario para construir y ejecutar la aplicación.
    // Se utiliza la versión de Spring Boot 3.4.1
    id("org.springframework.boot") version "3.4.1"
    // Plugin para la gestión de dependencias en proyectos Spring.
    id("io.spring.dependency-management") version "1.1.7"
}

// Descripción basÍca del proyecto.
group = "co.edu.uniquindio"
version = "1.0-SNAPSHOT"
description = "Proyecto basado en el desarrollo para el sistema Store-It!, " +
        "siguiendo las normas del negocio. " +
        "Autor: MrZ Leviatán"


java{
    toolchain{
        // Se define la versión de Java a utilizar (Java 22).
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations {
    compileOnly {
        //  Asegura que los procesadores de anotaciones solo se incluyan en tiempo de compilación.
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    // Define Maven Central como el repositorio para las dependencias
    mavenCentral()
}

// Bloque donde definiremos las dependencias a usar en el Backend del proyecto.
dependencies {
    // ==== Core Spring Boot ====
    // Starter Web de Spring Boot: Proporciona funcionalidades web básicas (REST, Tomcat embebido, JSON).
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Starter de validación: Anotaciones como @Email, @NotNull, etc.
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Starter de seguridad: Autenticación y autorización básica.
    implementation("org.springframework.boot:spring-boot-starter-security")

    // ==== Autenticación con Google y seguridad avanzada ====
    // Cliente OAuth2: Permite iniciar sesión con Google (OAuth2/OIDC).
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    // Resource Server: Permite proteger APIs con tokens JWT (lo usaremos después del login/2FA).
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    // JOSE (JSON Object Signing and Encryption): Manejo de firmas JWS/JWT para OAuth2 y OIDC.
    implementation("org.springframework.security:spring-security-oauth2-jose")

    // Librería para el login mediante google
    implementation("com.google.api-client:google-api-client:2.6.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.http-client:google-http-client-gson:1.44.1")

    // ==== JWT ====
    // Librería para generar y firmar tokens JWT (se combina con Spring Security).
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // ==== Persistencia ====
    // Spring Boot JPA (para Hibernate y ORM).
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // Driver JDBC para Oracle (versión 21.x).
    implementation("com.oracle.database.jdbc:ojdbc11:21.9.0.0")

    // ==== Utilidades ====
    // Lombok: Reduce el código repetitivo (Getters, Setters, etc.).
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // MapStruct: Mapeo entre DTOs y entidades.
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // Simple Java Mail: Librería para enviar correos electrónicos.
    implementation("org.simplejavamail:simple-java-mail:8.12.5")
    implementation("org.simplejavamail:batch-module:8.12.5")

    // libphonenumber: Librería para validación y formato de teléfonos con prefijo internacional.
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.30")

    // ==== Loggers ====
    implementation("org.springframework.boot:spring-boot-starter-logging")
    // Optional: actuator para health/metrics
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // JSON encoder + appenders for Logback -> Logstash/ELK
    implementation("net.logstash.logback:logstash-logback-encoder:7.4") // encoder JSON para logback -> ELK

    // 🔹 Cloudinary: Librería para el guardado de imágenes en la web
    implementation("com.cloudinary:cloudinary-http44:1.32.2")

    // ==== Testing ====
    // Starter de pruebas de Spring Boot (JUnit 5, Mockito, etc.).
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Looger
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.postgresql:postgresql:42.6.0")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
