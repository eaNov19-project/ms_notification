FROM openjdk:8-jdk-alpine
ADD target/ms_notification.jar ms_notification.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","ms_notification.jar"]