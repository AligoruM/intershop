# Instruction to run

- Requirements:
    - Java 21
- Run locally
    1. Execute ```mvnw clean package```
    2. Execute ```java -jar <name of jar>```, default from project root ```java -jar target/intershop-1.0.jar```
    3. Alternatively, ```mvnw clean spring-boot:run``` instead 2 previous commands
- Run in docker
    1. Execute ```mvnw clean package```
    2. Execute ```docker build -t intershop .```
    3. Run by ```docker run -p 8080:8080 intershop```
- In IntelliJ Idea
  1. I just used ```spring-boot:run``` in run config