# Build stage
FROM gradle:9.0-jdk21 AS build

WORKDIR /home/gradle/src

COPY --chown=gradle:gradle . .

# Dar permisos de ejecución al wrapper
RUN chmod +x ./gradlew

# Build ignorando tests
RUN ./gradlew clean bootJar -x test

# Package stage
FROM openjdk:21-jdk-slim
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]




