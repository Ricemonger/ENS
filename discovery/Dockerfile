FROM maven:3.9.6-eclipse-temurin-21-alpine as builder

WORKDIR /app

COPY ./utils-dependencies/pom.xml ./utils-dependencies/pom.xml

RUN mvn -f ./utils-dependencies/pom.xml clean install -DskipTests

COPY ./discovery/pom.xml ./discovery/pom.xml
COPY ./discovery/src ./discovery/src

RUN mvn -f ./discovery/pom.xml clean install -DskipTests


FROM eclipse-temurin:21.0.2_13-jre-alpine

WORKDIR /app

COPY --from=builder /app/discovery/target/*.jar /app/discovery.jar

CMD ["java", "-jar", "discovery.jar"]