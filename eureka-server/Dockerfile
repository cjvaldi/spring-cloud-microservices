# FROM --platform=linux/amd64 amazoncorretto:21-alpine-jdk   // solo si se usa mac arm64
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
EXPOSE 8761
COPY ./target/eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar

ENTRYPOINT ["java", "-jar", "eureka-server.jar"]