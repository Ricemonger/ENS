FROM eclipse-temurin:21.0.2_13-jre-alpine

WORKDIR /app

COPY target/*.jar /app/security.jar

CMD ["java", "-jar", "security.jar"]