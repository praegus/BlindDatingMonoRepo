FROM openjdk:21
EXPOSE 8080
ARG JAR_FILE=target/profileservice-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]