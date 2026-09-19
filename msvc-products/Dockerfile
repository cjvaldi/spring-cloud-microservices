FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY ./target/msvc-products-0.0.1-SNAPSHOT.jar msvc-products.jar

ENTRYPOINT ["java", "-jar", "msvc-products.jar"]