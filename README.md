# Instruction to run

- Requirements:
    - Java 21
- Run locally
  1. Configure hosts and ports in application.properties files in payment-service and intershop-service
  2. Execute ```mvnw clean package```
  3. Execute ```java -jar <name of jar>``` for payment-service
  4. Start redis
  5. Execute ```java -jar <name of jar>``` for intershop-service
  6. Alternatively, you can use ```mvnw clean spring-boot:run``` instead 2 previous commands
- Run in docker compose
  1. Execute ```mvnw clean package```
  2. Execute ```docker compose -p intershop-project up --build```
  3. Wait until all services are up
- In IntelliJ Idea
  1. I just used docker compose in run config