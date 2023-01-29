# epistimi-backend

A monolithic backend for Epistimi, built with Spring Boot.

## Prerequisites

Setup and run local PostgreSQL database:

```bash
docker compose -f docker-compose.yml up -d
```

Setup required environment variables:
* `JWT_SECRET` – secret used to encrypt JWT tokens

Run SQL script (`data/sql/users.sql`) in order to create an admin user with `EPISTIMI_ADMIN` role,
which is eligible to create new organizations in the systems.

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
