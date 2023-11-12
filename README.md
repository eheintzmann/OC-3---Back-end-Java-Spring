[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

# OC Rental
API of connection between future tenants and owners for seasonal rental on the Basque coast at first and, later, all over France.

## Technologies
* Java 17
* Spring Boot 3.1
* MySql 8.1

## Getting started

### Prerequisite to all options
Don't forget to clone the project repository with [git](https://git-scm.com/).
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

#### Run MySQL database and adminer

1. Go to docker directory (`cd docker`)
2. Run Docker Compose (`docker compose up`)
3. (Optional) Navigate to `http://localhost:9000` or `http://127.0.0.1:9000` (Don't use `https`)


### Option 2 : manual installation
#### Install MySQL

Official documentation is available at `https://dev.mysql.com/doc/` in installation section.

1. [Download MySQL Community](https://dev.mysql.com/downloads/mysql/) (choose 8.x.x stable version)
2. Install MySQL
3. Start MySQL (verify with `mysql -V`) 
4. If not done during installation, define a root password : `mysqladmin -u root password 'YourRootPassword'`
5. Connect yourself to MySQL as root : `mysql -u root -p`
6. Create the new database `CREATE DATABASE db_rental;`
7. Create the user `CREATE USER 'springuser'@'%' IDENTIFIED BY 'YourPassword';`
8. Give all privileges to the new user on newly created database : `GRANT ALL ON db_rental.* TO 'springuser'@'%';`
9. Quit MySQL : `exit;`
10. 
This connects to MySQL as root and allows access to the user from all hosts. This is not the recommended way for a production server.

#### Install Java Development Kit 17
1. [Download JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)
2. [Install JDK 17](https://docs.oracle.com/en/java/javase/17/install/overview-jdk-installation.html)

#### Install  Maven
1. [Download Maven](https://maven.apache.org/download.html) 
2. [Install Maven](https://maven.apache.org/install.html)

#### Run the application with Maven
* Run `mvn spring-boot:run` in the project root location

#### Run the application as jar
1. Build application with Maven
2. 

### API URLs
API is accessible at : http://localhost:8080/api/

### Swagger URL
The Swagger UI can be viewed at : http://localhost:8080/doc/swagger-ui.html

## Architecture of the project
* `main/java/com.openclassrooms.api/configuration/` : contains configuration classes
* `main/java/com.openclassrooms.api/controller/` : contains controllers
* `main/java/com.openclassrooms.api/model/` : contains models (DTO and Entity)
* `main/java/com.openclassrooms.api/repository/` : contains repositories
* `main/java/com.openclassrooms.api/services/` : contains Spring services
* `main/ressources/` : contains Spring resources
* `test/` : contains unit and integration tests

