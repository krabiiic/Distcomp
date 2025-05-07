FROM openjdk:21-jdk-slim
WORKDIR /app
COPY first-service/target/first-service-0.0.1-SNAPSHOT.jar /app/first-service.jar
EXPOSE 24110
ENTRYPOINT ["java", "-jar", "first-service.jar"]