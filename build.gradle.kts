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
        languageVersion.set(JavaLanguageVersion.of(22))
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
    // Starter Web de Spring Boot: Proporciona funcionalidades web básicas
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Starter de Seguridad de Spring Boot: Agrega funciones de autenticación y seguridad
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Starter de Seguridad de Spring Boot: Agrega funciones de autenticación y seguridad
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Lombok: Reduce el código repetitivo en clases Java (Getters, Setters, etc.)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    // Dependencia de prueba
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Spring Boot JPA (para Hibernate y ORM)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // Driver JDBC para Oracle (versión 21.x)
    implementation("com.oracle.database.jdbc:ojdbc11:21.9.0.0")

    // Mapper: Crea instancias de mapeo.
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // Simple Java Mail: Librería para enviar correos electrónicos
    implementation("org.simplejavamail:simple-java-mail:8.12.5")
    implementation("org.simplejavamail:batch-module:8.12.5")

    // libphonenumber: Librería para los prefijos y los países
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.30")

}

tasks.withType<Test>{
    useJUnitPlatform()
}