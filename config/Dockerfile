FROM maven:3.9.6-eclipse-temurin-21-alpine as builder

WORKDIR /app

COPY ./utils-dependencies/pom.xml ./utils-dependencies/pom.xml

RUN mvn -f ./utils-dependencies/pom.xml clean install -DskipTests

COPY ./config/pom.xml ./config/pom.xml
COPY ./config/src ./config/src

RUN mvn -f ./config/pom.xml clean install -DskipTests


FROM eclipse-temurin:21.0.2_13-jre-alpine

WORKDIR /app

COPY --from=builder /app/config/target/*.jar /app/config.jar

CMD ["java", "-jar", "config.jar"]