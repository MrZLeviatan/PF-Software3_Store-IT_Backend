# =======================
# Build stage
# =======================
FROM openjdk:17-jdk-slim AS build

WORKDIR /home/app

# Copiar todo el proyecto
COPY . .

# Dar permisos al gradlew
RUN chmod +x ./gradlew

# Limpiar y compilar ignorando tests
RUN ./gradlew clean bootJar -x test

# =======================
# Package stage
# =======================
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar el JAR del stage anterior
COPY --from=build /home/app/build/libs/*.jar app.jar

# Exponer el puerto de Render
ARG PORT=8080
EXPOSE $PORT

# Ejecutar la app
ENTRYPOINT ["java","-jar","/app.jar"]
