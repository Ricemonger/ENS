FROM eclipse-temurin:21.0.2_13-jre-alpine

WORKDIR /app

COPY target/*.jar /app/gateway.jar

CMD ["java", "-jar", "gateway.jar"]