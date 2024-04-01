# Order Management Application

## Project Overview

This project uses Gradle and Spring Boot version 3.2.4 to provide an application for order management.

## Configuring the Google Maps API Key

To configure the Google Maps API Key, update the `application.properties` file with your API key (google.api.key). **Note:** Avoid pushing your key into the repository for security reasons. 

## Automatic Deployment

The `start.sh` script automates the execution of the Docker Compose file for easy deployment. You can provide an argument, which will be the Google API key. For example, at the root of the project, run:

```bash
./start.sh googleApiKey
```

If you've already manually updated the `application.properties`, simply run:

```bash
./start.sh
```

## Running the Project with Docker manually

To run the project using Docker, follow these steps:

1. Build the Docker image:

   ```bash
   docker-compose build
   ```

2. Start the Docker containers:

   ```bash
   docker-compose up
   ```

3. Stop the Docker containers:

   ```bash
   docker-compose down
   ```

## Dependencies

- **Spring Boot Starter Data JPA**: Provides support for Spring Data JPA.
- **Spring Boot Starter Web**: Enables the development of web applications using Spring MVC.
- **Google Maps Services (Version 0.17.0)**: Integrates Google Maps services into the application for distance calculation.
- **Project Lombok**: Simplifies Java code by providing annotations to generate boilerplate code (getter, setter, equals, and hashcode) during compilation.
- **MySQL Connector J**: Allows the application to connect to a MySQL database.
- **H2 Database (Version 2.2.222)**: Provides an in-memory database for testing purposes.

## Project Structure

The project follows the structure outlined below:

- `config`
   - `validators`
- `controller`
- `dto`
- `entity`
- `enums`
- `exception`
- `repository`
- `service`
- `tests`
  - `integration`
  - `unit`

## Testing

The project includes both integration tests for the API and the Google Maps service, as well as unit tests.

## Application Properties

The `application.properties` file contains configurations for the app, including:

- **Data Source Configuration**: Specifies database platform, initialization mode, and Hibernate dialect for MySQL.
- **JPA Configuration**: Controls schema updates and SQL statement visibility.
- **Google Maps API Key**: Specifies the API key required for accessing Google Maps services.
- **Logging Configuration**: Sets log levels for different packages and components.
