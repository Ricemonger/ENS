FROM openjdk:11-ea-17-jre-slim

WORKDIR /app

COPY target/*.jar /app/config.jar

CMD ["java", "-jar", "config.jar"]