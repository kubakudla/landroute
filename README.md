README
====

1) Necessary tools:
- JDK 1.8 or higher
- Maven 3
- application code

2) How to run the application:

- build the project with Maven command, from the main folder of the project:

```
 mvn clean install
```

- run from command line (or run Application.java from your IDE):

```
 java -jar target/landroute-1.0-SNAPSHOT.jar
```

Now application should be running. There should be also Swagger UI available on the following url (to quickly test the REST API):

 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)