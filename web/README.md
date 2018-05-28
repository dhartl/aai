# Pharmacy Location Analyzer Backend

Spring boot backend to analyze pharmacy locations. It supports uploading doctor and pharmacy data via json and querying that data to create an application that display this data on a map.

## Getting Started

To use this project you need a mysql database running on port 3306 (mysql-default) on your computer.

### Setting up the database

Install mysql on your local computer. You need to create a schema `aai`, with an user `aai`and password `aai`. The application will use this user to connect to the database. You also have to create the database schema from the [mysql-model](../database/aai_database.mwb).

To reset the database you can use the [reset database sql file](../database/reset_database.sql).

### Launching the application

To run this application simply use the command

```
mvnw spring-boot:run
```

in the command line.

## Using the APIs

Examples for the usage of all endpoints can be found in the [postman collection](../postman/postman_requests.json). To import sample data with the import endpoints you can use the json-files in the [data-folder](../../data). 


## Built With

* [Spring Boot](https://projects.spring.io/spring-boot/) - Underlying web framework
* [Maven](https://maven.apache.org/) - Dependency Management
* [Spatial4j](https://projects.eclipse.org/projects/locationtech.spatial4j) - Calculating distances between geolocations
* [JTS Topology Suite](https://projects.eclipse.org/projects/locationtech.jts) - Creating spatial indices