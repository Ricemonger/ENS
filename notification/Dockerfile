FROM eclipse-temurin:21.0.2_13-jre-alpine

WORKDIR /app

COPY target/*.jar /app/notification.jar

CMD ["java", "-jar", "notification.jar"]