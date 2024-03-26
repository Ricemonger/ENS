FROM maven:3.9.6-eclipse-temurin-21-alpine as builder

WORKDIR /app

COPY ./utils-dependencies/pom.xml ./utils-dependencies/pom.xml

RUN mvn -f ./utils-dependencies/pom.xml clean install -DskipTests

COPY ./gateway/pom.xml ./gateway/pom.xml
COPY ./gateway/src ./gateway/src

RUN mvn -f ./gateway/pom.xml clean install -DskipTests


FROM eclipse-temurin:21.0.2_13-jre-alpine

WORKDIR /app

COPY --from=builder /app/gateway/target/*.jar /app/gateway.jar

CMD ["java", "-jar", "gateway.jar"]