[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

# OC Rental
API of connection between future tenants and owners for seasonal rental on the Basque coast at first and, later, all over France.

## Technologies
* Java
* Spring Boot

## Getting started
1. Don't forget to clone the project repository with [git](https://git-scm.com/).
`https://github.com/eheintzmann/OC3-Back-end-Java-Spring.git`


Copy `.env.example` file and name it `.env`. In this new file, modify the variables
* `MYSQL_ROOT_PASSWORD` : specifies the password for the MySQL _root_ superuser account
* `MYSQL_DATABASE` : specify the name of a database to be created and used
* `MYSQL_USER` : create a new user that will be granted superuser access on the created database
* `MYSQL_PASSWORD` : specify the password of the created user
* `JWT_SECRET` : a secret key used to generate and verify Access tokens

### Option 1 : Docker Compose
#### Install dependencies

1. Install [Docker](https://docs.docker.com/get-docker/)
2. Install [Docker Compose](https://docs.docker.com/compose/install/)
3. Download and Install  [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)
4. [Download](https://maven.apache.org/download.html) and [install](https://maven.apache.org/install.html) [Maven](https://maven.apache.org/)


#### Run MySQL database and adminer

1. Go to docker directory (`cd docker`)
2. Run Docker Compose (`docker compose up`)
3. (Optional) Navigate to `http://localhost:9000` or `http://127.0.0.1:9000` (Don't use `https`)

#### Run the application

1. Run `mvn spring-boot:start`
2. THe API `http://localhost:8080`

## Architecture of the project
* `main/java/com.openclassrooms.api/configuration/` : contains configuration classes
* `main/java/com.openclassrooms.api/controller/` : contains controllers
* `main/java/com.openclassrooms.api/model/` : contains models (DTO and Entity)
* `main/java/com.openclassrooms.api/repository/` : contains repositories
* `main/java/com.openclassrooms.api/services/` : contains Spring services
* `main/ressources/` : contains Spring resources
* `test/` : contains unit and integration tests

