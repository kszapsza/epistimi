# epistimi-backend

A monolithic backend for Epistimi, built with Spring Boot.

----

## Prerequisites

Setup and run local PostgreSQL database:

```bash
docker compose -f docker-compose.yml up -d
```

Setup required environment variables:
* `JWT_SECRET` – secret used to encrypt JWT tokens

## Running

Run unit tests:

```bash
./gradlew test
```

Run integration tests:

```bash
./gradlew integration
```

Start application:

```bash
./gradlew run
```
