# Verwende OpenJDK als Basis
FROM openjdk:21-jdk-slim

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere die JAR-Datei ins Arbeitsverzeichnis
COPY ./target/Backend-0.0.1-SNAPSHOT.jar ./Backend-0.0.1-SNAPSHOT.jar

# Exponiere Port 8080
EXPOSE 8080

# Setze Umgebungsvariable für das Spring Profil
ENV SPRING_PROFILES_ACTIVE=dev

# Starte die Spring Boot Anwendung
CMD ["java", "-jar", "Backend-0.0.1-SNAPSHOT.jar"]
