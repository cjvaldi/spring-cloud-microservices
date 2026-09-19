FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
EXPOSE 9411
COPY ./zipkin-server-3.4.1.jar zipkin-server.jar

ENTRYPOINT ["java", "-jar", "zipkin-server.jar"]