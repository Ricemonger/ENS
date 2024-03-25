FROM eclipse-temurin:21.0.2_13-jre-alpine

WORKDIR /app

COPY target/*.jar /app/config.jar

CMD ["java", "-jar", "config.jar"]