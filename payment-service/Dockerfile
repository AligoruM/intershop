FROM amazoncorretto:21

WORKDIR /payment-service

COPY target/payment-service-1.0-SNAPSHOT.jar payment-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "payment-service.jar"]
