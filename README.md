# CSYE6225 - Cloud Application

## Stack
- Language: Java
- Framework: SpringBoot
- ORM: Hibernate
- Database: MySQL

## Setup Steps
1. **Configure and Run**
   - Configure Maven
   - Clone the repository/Download source to your local machine.
   - Run command 'mvn clean install' - to compile and generate jar file
   - java -cp /full/path/to/jar/file com.neu.csye6225.cloud.CloudApplication

## Change Log

### Version #01
1. **Configure Database with Hibernate, MySQL**
   - Added necessary configurations for Hibernate to connect to MySQL.

2. **Add healthz Endpoint to Check Database Connectivity**
   - Implemented a healthz endpoint to check the connectivity and status of the database.

3. **Add slf4j Logback Configuration**
   - Integrated slf4j with Logback for logging configuration.
   - Logback configuration provides flexibility and customization for logging in the application.

### Version #02
1. **Add user support**
   - Add support for CRUD operations for User object model
   - Endpoint: /v1/user
2. **Add BASIC Auth**
   - Add BASIC Auth support for user authorization
3. **Add URL filter for non-configured URL patterns

### Version #3
1. **Add Integration tests**
   - Add integration test to Create and update user
2. **Add workflow**
   - Add workflow to configure DB server and execute tests in Git Actions - Workflow