# Spring Security Playground

Hands-on Spring Security learning playground. All stages coexist in a single codebase, isolated by URL prefix — no branch switching needed.

**Tech stack:** Spring Boot 4 · Spring Security 7 · Java 21 · React (Vite)

---

## Stages

- [x] **Stage 1 — Basic Auth** (`/api/basic-auth/**`) — HTTP Basic with in-memory users
- [x] **Stage 2 — Session** (`/api/session/**`) — Form login + JSESSIONID
- [x] **Stage 3 — JWT** (`/api/jwt/**`) — Bearer token, stateless, refresh rotation
- [ ] **Stage 4 — RBAC** — `@PreAuthorize`, method-level security, hierarchical roles
- [ ] **Stage 5 — Common Auth** — Remember Me, forgot password, MFA/OTP, account lockout
- [ ] **Stage 6 — OAuth2** — Login with Google/GitHub, OIDC, resource server
- [ ] **Stage 7 — Enterprise** — RS256 keys, audit logging, rate limiting, CORS
- [ ] **Stage 8 — Secrets Management** — HashiCorp Vault, dynamic secrets, environment profiles
- [ ] **Stage 9 — Security Testing** — `@WithMockUser`, MockMvc security tests, integration tests

Each implemented stage has its own `SecurityFilterChain` with `securityMatcher` + `@Order` for isolation. All share the same in-memory user store.

---

## Test Users

| Username | Password | Roles |
|----------|----------|-------|
| `user` | `password` | `ROLE_USER` |
| `admin` | `password` | `ROLE_ADMIN` |

---

## Quick Test

```bash
# Stage 1 — Basic Auth
curl -u user:password http://localhost:8080/api/basic-auth/user

# Stage 2 — Session
curl -X POST http://localhost:8080/api/session/login \
  -d "username=user&password=password" -c cookies.txt -L
curl -b cookies.txt http://localhost:8080/api/session/user

# Stage 3 — JWT
curl -X POST http://localhost:8080/api/jwt/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"password"}'
curl http://localhost:8080/api/jwt/user -H "Authorization: Bearer <token>"
```

Each stage exposes `/public` (open), `/user` (authenticated), and `/admin` (ROLE_ADMIN) under its prefix.

---

## Getting Started

```bash
./mvnw spring-boot:run -pl backend
cd frontend && npm install && npm run dev
```
