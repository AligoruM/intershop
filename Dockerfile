FROM amazoncorretto:21

WORKDIR /intershop

COPY target/intershop-1.0.jar intershop.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "intershop.jar"]
