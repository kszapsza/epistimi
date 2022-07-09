# epistimi-backend

A monolithic backend for Epistimi, build with Spring Boot.

## Prerequisites

Setup and run local databases:

```bash
docker compose -f docker-compose.yml
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
