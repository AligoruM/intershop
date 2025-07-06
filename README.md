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
    1. I just used

## NOTES

- Application is using H2 database with file mode, it saves data in **data** folder near jar executable
- Application has Liquibase integration, you are free to add something to it.

  In dev profile it will load some test items to DB, current implementation was done just to check how it integrated
  with spring profiles, not in some practical purpose.

  To generate changes based on JPA model changes need to execute ```mvnw liquibase:diff``` after compiling of project.  
  Also, you need reference to current DB state, that is **./data/shopDB** in default case for this project, see
  configuration of **liquibase-maven-plugin**.
- If you are running docker image, you can add volume in the **run** command
  ```-v <your folder to store here>:/intershop/data```