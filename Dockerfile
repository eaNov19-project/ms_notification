FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD target/ms_notification.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]