FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/diagnostico-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} diagnostico.jar
EXPOSE 7777
ENTRYPOINT ["java", "-jar", "diagnostico.jar"]