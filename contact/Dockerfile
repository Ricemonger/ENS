FROM maven:3.9.6-eclipse-temurin-21-alpine as builder

WORKDIR /app

COPY ../utils-dependencies ./utils-dependencies

RUN mvn -f ./utils-dependencies/pom.xml clean install -DskipTests

COPY ../utils-library ./utils-library

RUN mvn -f ./utils-library/pom.xml clean install -DskipTests

COPY ./pom.xml ./contact/pom.xml
COPY ./contact/src ./contact/src

RUN mvn -f ./contact/pom.xml clean install -DskipTests


FROM eclipse-temurin:21.0.2_13-jre-alpine

WORKDIR /app

COPY --from=builder /app/contact/target/*.jar ./contact.jar

CMD ["java", "-jar", "contact.jar"]