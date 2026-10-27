# Uretek Inventory Tracker — Backend

REST API for Uretek Argentina's internal inventory management system. Tracks geopolymer product usage per job, manages stock levels, and flags low-stock alerts.

🔗 **Frontend:** [uretek-inventory-ui-production.up.railway.app](https://uretek-inventory-ui-production.up.railway.app)

---

## What it does

Uretek's geopolymer product is a two-component mix (ISO + Resina) used on every job. This API:

- Records jobs and deducts the exact amount of product used from current stock
- Calculates ISO and Resina consumption per job (63/37 ratio)
- Compares quoted vs actual product usage per job
- Triggers low-stock alerts when inventory drops below minimum thresholds
- Secured with JWT authentication — all endpoints require a valid token

---

## Tech Stack

- **Java 17** + **Spring Boot 3**
- **Spring Security** + **JWT** (jjwt) — stateless auth, BCrypt password hashing
- **Spring Data JPA** + **PostgreSQL**
- **Maven**
- **Railway** — cloud deployment + environment variable management

---

## Architecture

Client → JwtFilter → Controller → Service → Repository → PostgreSQL

- `JwtFilter` — extends `OncePerRequestFilter`, validates Bearer token on every request and sets the Spring `SecurityContext`
- `JwtUtil` — token generation, validation, and claim extraction
- `SecurityConfig` — stateless session policy, CORS configuration, route permissions

---

## Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/auth/login` | ❌ | Returns JWT token |
| GET | `/api/items` | ✅ | All inventory items |
| GET | `/api/jobs` | ✅ | All jobs |
| POST | `/api/jobs` | ✅ | Create job + deduct stock |
| PUT | `/api/jobs/:id` | ✅ | Update job |

---

## Running locally

```bash
git clone <repo-url>
cd uretek-inventory
```

Set environment variables:

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/uretek
SPRING_DATASOURCE_USERNAME=your_user
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET=your_base64_secret

```bash
./mvnw spring-boot:run
```

---

## Tests

```bash
./mvnw test
```

3 unit tests covering job creation and stock deduction logic.

