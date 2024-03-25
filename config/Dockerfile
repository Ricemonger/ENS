FROM maven:3.9.6-eclipse-temurin-21-alpine as builder

WORKDIR /app

COPY ../pom.xml .

COPY ./pom.xml ./config/pom.xml
COPY ./src ./config/src

RUN mvn -f ../pom.xml clean install

FROM eclipse-temurin:21.0.2_13-jre-alpine

WORKDIR /app

COPY --from=builder /app/config/target/*.jar ./config.jar

CMD ["java", "-jar", "config.jar"]