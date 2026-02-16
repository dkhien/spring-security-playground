# Spring Security Playground

Hands-on Spring Security learning playground.

**Tech stack:** Spring Boot 4 · Spring Security 7 · Java 21 · React (Vite)

---

## Stages

- [x] **Stage 1 — Basic Auth** (`/api/basic-auth/**`) — HTTP Basic with in-memory users
- [x] **Stage 2 — Session** (`/api/session/**`) — Form login + JSESSIONID
- [x] **Stage 3 — JWT** (`/api/jwt/**`) — Bearer token, stateless, refresh rotation
- [x] **Stage 4 — RBAC** (`/api/users/**`) — `@PreAuthorize`, method-level security, hierarchical roles
- [ ] **Stage 5 — Common Auth** — Remember Me, forgot password, MFA/OTP, account lockout
- [ ] **Stage 6 — OAuth2** — Login with Google/GitHub, OIDC, resource server
- [ ] **Stage 7 — Enterprise** — RS256 keys, audit logging, rate limiting, CORS
- [ ] **Stage 8 — Secrets Management** — HashiCorp Vault, dynamic secrets, environment profiles
- [ ] **Stage 9 — Security Testing** — `@WithMockUser`, MockMvc security tests, integration tests

Each implemented stage has its own `SecurityFilterChain` with `securityMatcher` + `@Order` for isolation.

Stages 1–3 use in-memory users. Stage 4+ uses a database-backed `AppUser` entity with `CustomUserDetailsService`.

---

## Test Users

| Username | Password | Roles | Store |
|----------|----------|-------|-------|
| `user` | `password` | `USER` | In-memory (Stages 1–3) |
| `admin` | `password` | `ADMIN` | In-memory (Stages 1–3) |

For Stage 4 (RBAC), users are created via `POST /api/jwt/signup` and stored in the database.

---

## Quick Test

```bash
# Stage 1 — Basic Auth
curl -u user:password http://localhost:8080/api/basic-auth/me

# Stage 2 — Session
curl -X POST http://localhost:8080/api/session/login \
  -d "username=user&password=password" -c cookies.txt -L
curl -b cookies.txt http://localhost:8080/api/session/me

# Stage 3 — JWT
curl -X POST http://localhost:8080/api/jwt/token \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"password"}'
curl http://localhost:8080/api/jwt/me -H "Authorization: Bearer <token>"

# Stage 4 — RBAC (requires JWT from Stage 3)
curl http://localhost:8080/api/users/me -H "Authorization: Bearer <token>"
curl http://localhost:8080/api/users -H "Authorization: Bearer <token>"       # ADMIN only
curl -X PUT http://localhost:8080/api/users/1/role \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"role":"ADMIN"}'                                                       # ADMIN only
```

Stages 1–3 expose `/public` (open), `/me` (authenticated), and `/admin` (ADMIN role) under their prefix.

### Stage 4 — RBAC Endpoints

| Endpoint | Method | Access |
|----------|--------|--------|
| `/api/users` | GET | ADMIN |
| `/api/users/me` | GET | Authenticated |
| `/api/users/{id}` | GET | ADMIN or self |
| `/api/users/{id}` | PUT | ADMIN or self |
| `/api/users/{id}` | DELETE | ADMIN |
| `/api/users/{id}/role` | PUT | ADMIN |
| `/api/users/me/password` | PUT | Authenticated |

Method-level security via `@PreAuthorize` with SpEL: `hasRole('ADMIN') or principal.id == #id`. Role hierarchy: `ADMIN` implies `USER`.

---

## Getting Started

```bash
./mvnw spring-boot:run -pl backend
cd frontend && npm install && npm run dev
```
