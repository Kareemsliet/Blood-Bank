# BloodBank API

A Spring Boot REST API for managing blood-donation records and the supporting
data used by a blood bank. It provides separate authenticated APIs for public
clients and dashboard users, with role-based permissions for dashboard
operations.

## Contents

- [Features](#features)
- [Technology](#technology)
- [Requirements](#requirements)
- [Run locally](#run-locally)
- [Configuration](#configuration)
- [Project structure](#project-structure)
- [Database](#database)
- [Authentication and authorization](#authentication-and-authorization)
- [API reference](#api-reference)
- [Responses and errors](#responses-and-errors)
- [Tests and packaging](#tests-and-packaging)
- [Operational notes](#operational-notes)

## Features

- Client registration, login, and account password changes.
- Donation creation and personal donation history for clients.
- Dashboard management for donations, clients, users, locations, blog content,
  roles, and permissions.
- Blog browsing and client blog favourites.
- JWT bearer authentication backed by revocable, expiring token records.
- Role/permission-based authorization for dashboard endpoints.
- Request validation and Flyway database migrations.

## Technology

- Java and Spring Boot
- Spring MVC, Spring Security, Spring Data JPA, and Bean Validation
- MySQL with Flyway
- Maven Wrapper
- JJWT for signed JWTs

The Maven compiler plugin currently sets Java source/target to **25**, while
`pom.xml` also declares `java.version` as **21**. Use a JDK 25 to build this
project as currently configured; reconcile these settings if the project is
intended to target Java 21.

## Requirements

- JDK 25
- MySQL server
- No separate Maven installation is needed; the checked-in wrapper downloads
  and runs Maven 3.10.0.

## Run locally

1. Create the database in MySQL:

   ```sql
   CREATE DATABASE blood_bank;
   ```

2. Set connection details and a strong JWT signing secret in the environment.
   The following PowerShell example uses Spring Boot's environment-variable
   property binding:

   ```powershell
   $env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/blood_bank"
   $env:SPRING_DATASOURCE_USERNAME = "root"
   $env:SPRING_DATASOURCE_PASSWORD = "your-local-mysql-password"
   $env:AUTH_JWT_SECRET = "replace-with-a-private-secret-at-least-32-characters"
   ```

   Do not use the development secret configured in `application.properties` in
   a shared or production environment.

3. Start the API:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   The server listens on `http://localhost:8080` by default. Flyway migrations
   run during application startup.

4. To stop the server, press `Ctrl+C`.

The environment variables above apply only to the current PowerShell session.
Alternatively, supply Spring Boot properties as command-line arguments or
configure them through your deployment's secret/configuration manager.

## Configuration

Application defaults are in
[`src/main/resources/application.properties`](src/main/resources/application.properties).

| Property | Default / purpose |
|---|---|
| `server.port` | `8080` |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/blood_bank` |
| `spring.datasource.username` | `root` |
| `spring.datasource.password` | Set locally; do not commit credentials |
| `auth.jwt.secret` | JWT HMAC signing secret; provide a private secret of at least 32 bytes |
| `auth.jwt.expiration` | `3600000` milliseconds (1 hour) |
| `spring.flyway.enabled` | `true` |
| `spring.jpa.hibernate.ddl-auto` | `update` |

The configured CORS origin is `http://localhost:3000`. Update the CORS
configuration in `SecurityConfig` for the actual frontend origin when deploying.

## Project structure

```text
src/
  main/
    java/com/example/bloodbank/
      config/          Spring Security and application configuration
      controller/      Client APIs, dashboard APIs, and exception handling
      dto/             Request, response, and data-transfer objects
      entity/          JPA persistence entities
      exception/       Application exceptions
      repository/      Spring Data repositories
      service/         Business logic
      util/            JWT/filter, blood types, validation annotations
    resources/
      application.properties
      db/migration/    Ordered Flyway SQL migrations
  test/                Automated tests
```

The application entry point is
`com.example.bloodbank.BloodBankApplication`. Controllers handle HTTP requests,
services implement the use cases, repositories access the database, and DTOs
define the API boundary.

## Database

Flyway scripts in `src/main/resources/db/migration` are applied in version
order. Current migrations create or seed:

- States and cities
- Blog categories and blogs
- Permissions, roles, and users
- Cache records
- Contacts and clients
- Donations
- Personal access tokens
- Initial dashboard permission definitions

The database also represents relationships between cities and states, blogs and
categories, clients and roles, donations and clients/cities, and users and
roles. JPA schema auto-update is enabled alongside Flyway; schema changes should
be added as new Flyway migrations and tested against the target database.

There is no migration that creates a default dashboard user. Create/configure a
dashboard account through a controlled setup process before using dashboard
login.

## Authentication and authorization

Send authenticated requests with:

```http
Authorization: Bearer <token>
```

Client and dashboard accounts use separate login endpoints and token types:

| Account type | Login | Token can access |
|---|---|---|
| Client | `POST /api/auth/login` | `/api/me/*` |
| Dashboard user | `POST /api/dashboard/auth/login` | `/api/dashboard/*` |

Client registration is available at `POST /api/auth/register`. Both login
endpoints accept JSON with `email` and `password`. Successful login/registration
returns a token and its expiry under `data`.

Tokens are signed JWTs and also stored in `personal_access_tokens`. Logging out
deletes the stored token, which makes it unusable before its expiry. Dashboard
authorities are loaded from the user's assigned roles and permissions.

Dashboard endpoints require both a dashboard-user token and the relevant
authority. Authorities follow the pattern `<RESOURCE>_<ACTION>`, for example
`BLOGS_VIEW`, `BLOGS_CREATE`, `BLOGS_EDIT`, and `BLOGS_DELETE`. The initial
permission set is defined by the permission seed migration.

The `/check` endpoints are public to allow a frontend to check the current
authentication context; their response data is `true` only when the request
contains a valid token of the matching account type. Logout endpoints require
authentication.

## API reference

All paths below are relative to `http://localhost:8080`. JSON request bodies
should use `Content-Type: application/json`, except blog image uploads.

### Client authentication

| Method | Path | Purpose |
|---|---|---|
| `POST` | `/api/auth/register` | Register a client and return an access token |
| `POST` | `/api/auth/login` | Authenticate a client |
| `POST` | `/api/auth/check` | Check whether a client token is valid |

### Client account (`/api/me`)

All routes in this section require a client token.

| Method | Path | Purpose |
|---|---|---|
| `GET` | `/api/me/donations?page=1` | List the signed-in client's donations |
| `GET` | `/api/me/donations/{id}` | Get one of the client's donations |
| `POST` | `/api/me/donations/new` | Create a donation for the signed-in client |
| `DELETE` | `/api/me/donations/{id}` | Delete one of the client's donations |
| `GET` | `/api/me/favourites/blogs?page=1` | List favourite blogs |
| `POST` | `/api/me/favourites/blogs/{id}/toggle` | Add/remove a blog from favourites |
| `POST` | `/api/me/change-password` | Change the signed-in client's password |
| `POST` | `/api/me/logout` | Revoke the current client token |

For client-created donations, the server sets the client ID from the token; the
client ID in the submitted body is not used as the account identity.

### Dashboard authentication

| Method | Path | Purpose |
|---|---|---|
| `POST` | `/api/dashboard/auth/login` | Authenticate a dashboard user |
| `POST` | `/api/dashboard/auth/check` | Check whether a dashboard token is valid |
| `POST` | `/api/dashboard/auth/logout` | Revoke the current dashboard token |

### Dashboard resources

All dashboard resource routes require a dashboard-user token, plus the
authority listed by that controller. For the CRUD resources below, the common
operations are:

| Method | Path suffix | Purpose |
|---|---|---|
| `GET` | `?page=1&q=` | Search/list, with pagination |
| `GET` | `/{id}` | Read one record |
| `POST` | `/create` | Create a record |
| `PATCH` | `/{id}` | Update a record |
| `DELETE` | `/{id}` | Delete a record |

The collection URL supplies the base path for the suffixes:

| Resource | Base path | Permissions |
|---|---|---|
| States | `/api/dashboard/states` | `STATES_VIEW`, `STATES_CREATE`, `STATES_EDIT`, `STATES_DELETE` |
| Cities | `/api/dashboard/cities` | `CITIES_VIEW`, `CITIES_CREATE`, `CITIES_EDIT`, `CITIES_DELETE` |
| Blog categories | `/api/dashboard/blog_categories` | `BLOG_CATEGORIES_VIEW`, `BLOG_CATEGORIES_CREATE`, `BLOG_CATEGORIES_EDIT`, `BLOG_CATEGORIES_DELETE` |
| Blogs | `/api/dashboard/blogs` | `BLOGS_VIEW`, `BLOGS_CREATE`, `BLOGS_EDIT`, `BLOGS_DELETE` |
| Clients | `/api/dashboard/clients` | `CLIENTS_VIEW`, `CLIENTS_CREATE`, `CLIENTS_EDIT`, `CLIENTS_DELETE` |
| Donations | `/api/dashboard/donations` | `DONATIONS_VIEW`, `DONATIONS_CREATE`, `DONATIONS_EDIT`, `DONATIONS_DELETE` |
| Users | `/api/dashboard/users` | `USERS_VIEW`, `USERS_CREATE`, `USERS_EDIT`, `USERS_DELETE` |
| Roles | `/api/dashboard/roles` | `ROLES_VIEW`, `ROLES_CREATE`, `ROLES_EDIT`, `ROLES_DELETE` |

Users and clients also support
`POST /api/dashboard/users/{id}/change_password` and
`POST /api/dashboard/clients/{id}/change_password`; these require the
corresponding `*_EDIT` authority.

Blog image upload is
`POST /api/dashboard/blogs/{id}/upload_image`. Submit a `multipart/form-data`
request with a file field named `image`. The server stores uploaded files under
`src/main/resources/uploads` relative to its working directory.

Permissions and contacts are read-only dashboard resources:

| Method | Path | Purpose |
|---|---|---|
| `GET` | `/api/dashboard/permissions?page=1&q=` | List permissions (`PERMISSIONS_VIEW`) |
| `GET` | `/api/dashboard/permissions/{id}` | Read a permission (`PERMISSIONS_VIEW`) |
| `GET` | `/api/dashboard/contacts?page=1&q=` | List contacts (`CONTACTS_VIEW`) |
| `GET` | `/api/dashboard/contacts/{id}` | Read a contact (`CONTACTS_VIEW`) |

List routes use page numbers starting at 1 and return 12 records per page.
Dashboard lists accept an optional `q` search term. Paginated responses include
the current/last page, whether next/previous pages exist, and the total record
count.

### Example: client login

```powershell
$body = @{ email = "client@example.com"; password = "your-password" } | ConvertTo-Json
$login = Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8080/api/auth/login" `
  -ContentType "application/json" `
  -Body $body

$token = $login.data.token
Invoke-RestMethod `
  -Uri "http://localhost:8080/api/me/donations" `
  -Headers @{ Authorization = "Bearer $token" }
```

## Responses and errors

Most responses use this envelope:

```json
{
  "status": true,
  "message": null,
  "data": {}
}
```

Authentication responses place `token` and `expiresAt` in `data`. Paginated
responses place `items`, `currentPage`, `lastPage`, `hasNext`, `hasPrevious`,
and `total` in `data`.

Typical status codes include:

- `200 OK` for successful reads and updates.
- `201 Created` for successful dashboard creates.
- `400 Bad Request` for constraint validation failures.
- `401 Unauthorized` for unauthenticated requests or invalid credentials.
- `403 Forbidden` when an authenticated dashboard user lacks an authority.
- `404 Not Found` when a requested resource does not exist.

Validation errors return the standard response envelope with `status: false`
and field/message details in `data`. Not-found and type-mismatch errors may
return an empty `404` response.

## Tests and packaging

Run the test suite:

```powershell
.\mvnw.cmd test
```

Build the executable Spring Boot JAR:

```powershell
.\mvnw.cmd clean package
```

The output is written under `target/`. Run a packaged JAR with:

```powershell
java -jar .\target\BloodBank-0.0.1-SNAPSHOT.jar
```

Set the database and JWT configuration before running tests or the packaged
application if the test setup requires MySQL.

## Operational notes

- Keep database credentials and JWT secrets outside source control. Use a
  unique, sufficiently strong JWT secret per environment.
- Restrict the configured CORS origin to trusted frontend origins.
- Blog images are stored on local disk, not in the database; preserve or back up
  `src/main/resources/uploads` when deploying on persistent infrastructure.
- The application currently enables SQL logging and Hibernate schema
  auto-update in `application.properties`. Review both settings for production
  use and manage production schema changes through reviewed migrations.
- No OpenAPI/Swagger dependency is configured; this README is the API reference
  for the endpoints currently implemented in the controllers.
