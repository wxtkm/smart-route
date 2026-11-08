# SmartRoute

SmartRoute is a Java Spring Boot REST API for user accounts and vehicle management. The planned v1.0 lets users register, sign in, save their vehicles, and record fuel consumption. Route planning and fuel-range calculations are future features unless implemented separately.

> **Implementation status:** The registration and JWT login endpoints, plus a protected user profile endpoint, are described in the current development walkthrough. The `Vehicle` entity has been drafted, but vehicle CRUD endpoints have **not yet been implemented or verified**. This document separates currently planned endpoints from working ones. Adjust commands to match the actual code and database configuration.

## Technology stack

- Java, Spring Boot, Spring Security, Spring Data JPA / Hibernate
- PostgreSQL
- JWT authentication with BCrypt password hashing
- Maven

## Local setup

1. Start PostgreSQL and create the database configured in `src/main/resources/application.yml` (for example, `smart_route`).
2. Set `JWT_SECRET` to a securely generated random value of at least 32 bytes (for HS256). Do not commit secrets.
3. Confirm your database credentials and JDBC URL in `application.yml` or environment variables.
4. Start the application from the project root:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The examples below assume `http://localhost:8080` and an existing account or a functioning registration endpoint.

## REST API testing

You can use curl in Bash, Git Bash, or PowerShell (`curl.exe` on Windows). All request bodies are JSON.

### 1. Register a user

**POST** `/api/auth/register`

```bash
curl -i -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Alex","lastName":"Smith","email":"alex@example.com","password":"ExamplePassword123!"}'
```

**Expected:** `201 Created` with a user response containing `id`, `firstName`, `lastName`, and `email`, but **not** the password. Re-registering the same email should be rejected (the exact error status depends on your exception handler).

### 2. Log in

**POST** `/api/auth/login`

```bash
curl -i -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"alex@example.com","password":"ExamplePassword123!"}'
```

**Expected:** `200 OK` with JSON similar to:

```json
{"token":"<JWT_TOKEN>","email":"alex@example.com"}
```

Incorrect credentials should return `401 Unauthorized`. Copy the actual token into the following examples. Never commit or publish real tokens.

### 3. Read the authenticated user's profile

**GET** `/api/users/me`

```bash
curl -i http://localhost:8080/api/users/me \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Expected:** `200 OK`, with the authenticated user's `id`, `firstName`, `lastName`, and `email`.

**Negative tests:**

```bash
# No token: expected 401 after the authentication entry point is configured
curl -i http://localhost:8080/api/users/me

# Invalid token: expected 401
curl -i http://localhost:8080/api/users/me \
  -H "Authorization: Bearer invalid.token.value"
```

### 4. Vehicle endpoints — planned, not yet available

The following are **proposed API contracts**, not commands that will work until `VehicleController`, service, repository, DTOs, and validation are implemented.

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/vehicles` | Add a vehicle to the signed-in account |
| `GET` | `/api/vehicles` | List only the signed-in user's vehicles |
| `GET` | `/api/vehicles/{id}` | Read one owned vehicle |
| `PUT` | `/api/vehicles/{id}` | Update an owned vehicle |
| `DELETE` | `/api/vehicles/{id}` | Delete an owned vehicle |

Proposed create request:

```bash
curl -i -X POST http://localhost:8080/api/vehicles \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"brand":"Toyota","model":"Corolla","productionYear":2019,"fuelConsumption":6.50,"tankCapacity":50.00}'
```

Proposed list request:

```bash
curl -i http://localhost:8080/api/vehicles \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

Proposed update request (replace `1` with a vehicle ID you own):

```bash
curl -i -X PUT http://localhost:8080/api/vehicles/1 \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"brand":"Toyota","model":"Corolla","productionYear":2019,"fuelConsumption":6.80,"tankCapacity":50.00}'
```

Proposed delete request:

```bash
curl -i -X DELETE http://localhost:8080/api/vehicles/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Authorization requirement:** Vehicle ownership must be resolved from the verified JWT identity on the server. Never accept a client-supplied `userId` as proof of ownership. Test that user A cannot read, edit, or delete user B's vehicle.

## PostgreSQL verification queries

Run the queries in `psql`, pgAdmin, or IntelliJ's database console against the **development/test database**. They are read-only.

Connect with `psql` (replace the database name/user if your configuration differs):

```bash
psql -h localhost -p 5432 -U postgres -d smart_route
```

### Inspect tables

```sql
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public'
ORDER BY table_name;
```

### Check registered users (no password hashes exposed)

```sql
SELECT id, email, first_name, last_name, created_at, updated_at
FROM users
ORDER BY id DESC;
```

### Check a specific test account

```sql
SELECT id, email, created_at
FROM users
WHERE email = 'alex@example.com';
```

### Check user roles

```sql
SELECT u.id AS user_id, u.email, r.name AS role_name
FROM users u
LEFT JOIN user_roles ur ON ur.user_id = u.id
LEFT JOIN roles r ON r.id = ur.role_id
ORDER BY u.id, r.name;
```

### Check whether a password is stored as a BCrypt hash

```sql
SELECT id, email,
       (password LIKE '$2a$%' OR password LIKE '$2b$%' OR password LIKE '$2y$%')
           AS looks_like_bcrypt
FROM users
WHERE email = 'alex@example.com';
```

This is only a format check, **not** proof that the password was hashed correctly or that login verification works.

### Vehicle queries — run only after the `vehicles` table exists

```sql
SELECT id, brand, model, production_year,
       fuel_consumption, tank_capacity, user_id
FROM vehicles
ORDER BY id DESC;
```

```sql
SELECT u.email, v.id AS vehicle_id, v.brand, v.model,
       v.fuel_consumption, v.tank_capacity
FROM vehicles v
JOIN users u ON u.id = v.user_id
ORDER BY u.email, v.id;
```

```sql
SELECT u.id, u.email, COUNT(v.id) AS vehicle_count
FROM users u
LEFT JOIN vehicles v ON v.user_id = u.id
GROUP BY u.id, u.email
ORDER BY u.id;
```

> **Schema note:** These queries assume Hibernate's default snake_case physical naming (`first_name`, `production_year`, etc.). Check the actual schema if custom naming rules or migrations are used. A Java entity does not guarantee a corresponding table exists until a migration or schema-generation step has run.

## Suggested API test checklist

- [ ] Registration creates exactly one database record.
- [ ] Duplicate email is rejected.
- [ ] Password is not returned by any API response.
- [ ] Password is stored as a BCrypt hash, not plaintext.
- [ ] Correct login returns a signed JWT.
- [ ] Incorrect login returns HTTP 401.
- [ ] `/api/users/me` rejects missing, invalid, and expired JWTs.
- [ ] `/api/users/me` returns the correct authenticated user.
- [ ] After implementation: vehicle CRUD works and survives application restart.
- [ ] After implementation: vehicles are isolated by account.
- [ ] After implementation: negative/zero fuel consumption and tank capacity are rejected.

## Roadmap to v1.0

1. Complete and test JWT login and authenticated profile.
2. Implement `VehicleRepository`, DTOs, service, controller, and validation.
3. Add vehicle ownership authorization tests.
4. Implement basic fuel range calculation.
5. Build a minimal web UI for registration, login, and vehicle management.
6. Add integration tests and deploy the application.

## Security reminders

Use test credentials only. Keep `.env`, database passwords, JWT secrets, and real tokens out of Git. Use HTTPS in deployment. Restrict production database access. Do not expose password hashes in API responses or diagnostic screenshots.
