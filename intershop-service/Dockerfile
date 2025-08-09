FROM amazoncorretto:21

WORKDIR /intershop

COPY target/intershop-service-3.0-SNAPSHOT.jar intershop-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "intershop-service.jar"]
