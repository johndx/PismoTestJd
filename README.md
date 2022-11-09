# Getting Started


## Installation Instructions
Having extracted the App from github, perform the following steps to build, configure and execute the App:

> mvn clean install

>./mvnw spring-boot:run

## Testing Strategy

## Unit Testing

- No Junit tests have been provided for the Domain classes as they are simple POJO (Getter and Setter) classes where testing adds no value.
- The Same applied to the Repository Tier Interfaces

## Integration Testing

Mock MVC Tests have been generated for the Web/Rest Tier, to exercise the REST API ( and subsequent tiers) , by using Mock API REST requests to emulate real REST requests to the API.
Invoke the tests vai maven with;

> mvn test


##  POSTMAN
Postman has been used as the REST Client and the following requests were co figured for testing the api;

- POST to http://localhost:8080/accounts with JSON document body
- GET to http://localhost:8080/accounts/52
- POST to http://localhost:8080/transactions with JSON DTO document body

### Notes

- OperationsTypes : Depending on the amount of Operations data that would be required and whether it would change dynamically this class could just be implemented as an Enum.
- Service Tier : Rest Controller classes delegates directly to a Repository Tier, as an intermediate 'Service' is not justified.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.5/maven-plugin/reference/html/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.5/reference/htmlsingle/#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.7.5/reference/htmlsingle/#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)



Built in IntelliJ
