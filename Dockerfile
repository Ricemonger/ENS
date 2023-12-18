FROM alpine:3.18
 
ENV JAVA_HOME=/usr/lib/jvm/java-19-openjdk
ENV MAVEN_HOME=/usr/share/maven
 
RUN apk add --no-cache openjdk19 maven
 
ENV PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH
 
LABEL maintainer="Ricemonger"
LABEL version="1.0"
 
WORKDIR /app
 
COPY . /app
 
RUN mvn clean install
 
CMD ["java", "-jar", "target/myapp.jar"]