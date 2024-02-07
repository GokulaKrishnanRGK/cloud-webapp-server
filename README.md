# CSYE6225 - Cloud Application

## Stack
- Language: Java
- Framework: SpringBoot
- ORM: Hibernate
- Database: PostgresSQL

## Setup Steps
1. **Configure and Run**
    - Clone the repository to your local machine.
    - Ensure you have Java, SpringBoot, and PostgresSQL installed.
    - Configure the database connection in the application properties.
    - Run the application.

## Change Log

### Version #01
1. **Configure Database with Hibernate, PostgresSQL**
    - Added necessary configurations for Hibernate to connect to PostgresSQL.

2. **Add healthz Endpoint to Check Database Connectivity**
    - Implemented a healthz endpoint to check the connectivity and status of the database.

3. **Add slf4j Logback Configuration**
    - Integrated slf4j with Logback for logging configuration.
    - Logback configuration provides flexibility and customization for logging in the application.
