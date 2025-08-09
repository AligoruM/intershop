FROM amazoncorretto:21

WORKDIR /intershop

COPY target/intershop-service-1.0.jar intershop-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "intershop-service.jar"]
