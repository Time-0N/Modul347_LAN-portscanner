FROM openjdk:21-jdk-slim

WORKDIR /app

COPY ./target/Backend-0.0.1-SNAPSHOT.jar ./Backend-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-dev}

CMD ["java", "-jar", "Backend-0.0.1-SNAPSHOT.jar"]
