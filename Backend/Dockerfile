# Verwende ein offizielles OpenJDK-Image als Basis
FROM openjdk:17-jdk-slim

# Setze das Arbeitsverzeichnis im Container
WORKDIR /app

# Kopiere die JAR-Datei ins Arbeitsverzeichnis
COPY ./target/your-backend-app.jar ./backend-app.jar

# Exponiere den Port, auf dem der Backend-Server läuft (Standard ist 8080)
EXPOSE 8080

# Setze die Umgebungsvariable für das Spring-Profil (falls erforderlich)
ENV SPRING_PROFILES_ACTIVE=dev

# Starte die Spring Boot Anwendung
CMD ["java", "-jar", "backend-app.jar"]
