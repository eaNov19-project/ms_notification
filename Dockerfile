FROM openjdk:8
EXPOSE 8080
ADD target/ms_notification.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]