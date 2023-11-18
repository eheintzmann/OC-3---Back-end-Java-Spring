[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

# OC Rental
API of connection between future tenants and owners for seasonal rental
on the Basque coast at first and, later, all over France.

## Technologies
* Java 17
* Spring Boot 3.1
* MySql 8.0

## Getting started
### Prerequisite to all options
First clone the project repository with [git](https://git-scm.com/).  
`git clone https://github.com/eheintzmann/OC3-Back-end-Java-Spring.git`

__In the project root location,copy `.env.example` file and name it `.env`.  
Edit this new file, and modify the variables :__
* `MYSQL_ROOT_PASSWORD` : specifies the password for the MySQL __root__  account (docker only setting)
* `MYSQL_DATABASE` : specify the name of a database to be created and used
* `MYSQL_USER` : create a new user that will be granted superuser access on the created database
* `MYSQL_PASSWORD` : specify the password of the created user
* `MYSQL_PORT` : specify the port number of MySQL server
* `ADMINER_PORT` : specify the port number of Adminer server
* `TOMCAT_PORT`: specify the port number of Tomcat server
* `JWT_SECRET` : a secret key used to generate and verify Access tokens
* `JWT_SALT` :  additional string for Access tokens

### Option 1 : Docker Compose
#### Install dependencies
1. Install [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/)

#### Run application and MySQL with Docker compose
1. From the project root location, run `docker compose up` 
2. Wait until Spring start
3. API is accessible at http://localhost:9000/api/
4. Swagger UI can be viewed at : http://localhost:9000/doc/swagger-ui.html
5. (Optional) Adminer is available at http://localhost:8080. Connect it to *mysql* server

### Option 2 : Docker Compose + Maven
#### Install dependencies
1. Install [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/)
3. [Download JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)
4. [Install JDK 17](https://docs.oracle.com/en/java/javase/17/install/overview-jdk-installation.html)


#### Run database with Docker Compose
1. From the project root location, run `docker compose -f compose-mysql.yaml up`
2. (Optional) Adminer is available at http://localhost:8080. Connect it to *mysql* server.

#### Run application
_Please note that the images you upload will be stored by default
in the OpenClassRooms/Rental subdirectory of your personal folder._

1. From the project root location, execute Maven Goal _spring-boot:run_ : `./mvnw spring-boot:run`
2. API is accessible at : http://localhost:9000/api/
3. Swagger UI can be viewed at : http://localhost:9000/doc/swagger-ui.html

### Option 3 : manual installation + Maven
#### Install MySQL
_Official documentation is available at `https://dev.mysql.com/doc/` in installation section._
1. [Download MySQL Community](https://dev.mysql.com/downloads/mysql/) (choose 8.x.x stable version)
2. Install MySQL
3. Start MySQL (verify with `mysql -V`) 
4. If not done during installation, define a root password : `mysqladmin -u root password 'YourRootPassword'`

#### Create MySQL user and database
1. Connect yourself to MySQL as root : `mysql -u root -p`
2. Create the new database `CREATE DATABASE db_rental;`
3. Create the user `CREATE USER 'springuser'@'%' IDENTIFIED BY 'YourPassword';`
4. Give all privileges to the new user on newly created database : `GRANT ALL ON db_rental.* TO 'springuser'@'%';`
5. Quit MySQL : `exit;`

_This connects to MySQL as root and allows access to the user from all hosts.  
This is not the recommended way for a production server._

#### Install Java Development Kit 17
1. [Download JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)
2. [Install JDK 17](https://docs.oracle.com/en/java/javase/17/install/overview-jdk-installation.html)


#### Run the application with Maven
_Please note that the images you upload will be stored by default
in the OpenClassRooms/Rental subdirectory of your personal folder._

1. From the project root location, execute Maven Goal _spring-boot:run_ : `./mvnw spring-boot:run`
2. API is accessible at : http://localhost:9000/api/
3. Swagger UI can be viewed at : http://localhost:9000/doc/swagger-ui.html

## Architecture of the project
* `main/java/com.openclassrooms.api.configuration/` : contains configuration classes
* `main/java/com.openclassrooms.api.controller/` : contains controllers
* `main/java/com.openclassrooms.api.exception/` : contains custom exceptions
* `main/java/com.openclassrooms.api.model/` : contains models (DTO and Entity)
* `main/java/com.openclassrooms.api.repository/` : contains repositories
* `main/java/com.openclassrooms.api.services/` : contains Spring services
* `main/ressources/` : contains Spring resources
* `test/` : contains unit and integration tests
