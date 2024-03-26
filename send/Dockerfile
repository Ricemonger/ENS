FROM maven:3.9.6-eclipse-temurin-21-alpine as builder

WORKDIR /app

COPY ./utils-dependencies/pom.xml ./utils-dependencies/pom.xml

RUN mvn -f ./utils-dependencies/pom.xml clean install -DskipTests

COPY ./utils-library/pom.xml ./utils-library/pom.xml
COPY ./utils-library/src ./utils-library/src

RUN mvn -f ./utils-library/pom.xml clean install -DskipTests

COPY ./send/pom.xml ./send/pom.xml
COPY ./send/src ./send/src

RUN mvn -f ./send/pom.xml clean install -DskipTests


FROM eclipse-temurin:21.0.2_13-jre-alpine

WORKDIR /app

COPY --from=builder /app/send/target/*.jar /app/send.jar

CMD ["java", "-jar", "send.jar"]