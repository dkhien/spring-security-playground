# Spring Security Playground

Hands-on Spring Security learning project. Each "stage" adds a new authentication/authorization mechanism, isolated via separate `SecurityFilterChain` beans.

## Tech Stack

- **Backend:** Spring Boot 4.0.2, Spring Security 7, Java 21, PostgreSQL, JJWT 0.12.6, MapStruct 1.6.3, Lombok
- **Frontend:** (not yet created — `frontend/` is a placeholder)
- **Build:** Maven multi-module (parent POM at root, single `backend` module)
- **API docs:** Scalar UI via `springdoc-openapi-starter-webmvc-scalar` 3.0.1

## Environment

- Java 21
- Active Spring profile: `db` (PostgreSQL)

## Build & Run

```bash
# Compile
./mvnw -pl backend compile

# Test
./mvnw -pl backend test

# Run
./mvnw -pl backend spring-boot:run
```

## Project Structure

```
spring-security-playground/
├── pom.xml                              # Parent POM (multi-module)
├── backend/
│   ├── pom.xml                          # Spring Boot 4.0.2, all dependencies
│   └── src/main/
│       ├── java/com/dkhien/springsecurityplayground/
│       │   ├── config/                  # SecurityFilterChain configs (one per stage)
│       │   ├── security/               # SecurityUser, Role, JwtTokenProvider, JwtAuthenticationFilter
│       │   ├── entity/                 # AppUser, RefreshToken (JPA)
│       │   ├── repository/             # AppUserRepository, RefreshTokenRepository
│       │   ├── service/                # AppUserService, RefreshTokenService, CustomUserDetailsService
│       │   ├── controller/             # Controllers (one package per stage)
│       │   ├── exception/              # GlobalExceptionHandler + custom exceptions
│       │   ├── mapper/                 # MapStruct mapper (AppUser -> UserResponse)
│       │   └── utility/                # Utils (SHA-256 helper)
│       └── resources/
│           ├── application.properties
│           └── *.yaml                  # OpenAPI specs (5 files: basic-auth, session, jwt, auth, users)
└── frontend/                            # Empty placeholder
```

---

## Coding Conventions

### General Principles

- **Keep it simple.** This is a learning project — clarity over cleverness. Avoid premature abstraction.
- **Single Responsibility.** Each class has one job: config classes configure, services contain business logic, controllers delegate to services, mappers map.
- **Dependency Inversion.** Controllers depend on services (not repositories). Services depend on repositories. Security classes depend on services. No circular dependencies.
- **Don't repeat yourself.** Shared logic goes to a single place (e.g., `SecurityUser.from(AppUser)` is the only place that converts AppUser→SecurityUser).

### Naming

- **Config classes:** `<Stage>SecurityConfig` (e.g., `JwtSecurityConfig`, `UsersSecurityConfig`)
- **Filter chain beans:** `<stage>FilterChain` (e.g., `jwtFilterChain`, `usersFilterChain`)
- **Controllers:** `<Stage>Controller` implementing generated `<Stage>Api` interface
- **Services:** `<Entity>Service` (e.g., `AppUserService`, `RefreshTokenService`)
- **Exceptions:** Descriptive name ending with `Exception` (e.g., `InvalidRefreshTokenException`)
- **Mappers:** `<Entity>Mapper` (MapStruct interface with `componentModel = "spring"`)
- **OpenAPI specs:** `<stage>-api.yaml` in `src/main/resources/`

### Layering Rules

```
Controller → Service → Repository
     ↑            ↑
  Generated    Security
  API interface  classes
```

- **Controllers** are thin — validate input (via generated DTOs), delegate to service, map response. No business logic.
- **Services** contain business logic and `@PreAuthorize` annotations. Services throw domain exceptions.
- **Repositories** are plain Spring Data JPA interfaces. No custom query logic beyond `findBy*`.
- **Security classes** (JwtTokenProvider, JwtAuthenticationFilter) can call services but services should NOT depend on security classes.

### API-First Workflow

1. Define the API in an OpenAPI YAML spec under `src/main/resources/<stage>-api.yaml`
2. Add an execution block in `backend/pom.xml` under `openapi-generator-maven-plugin`
3. Run `./mvnw compile` — generates interfaces in `api.<stage>` and DTOs in `model.<stage>` under `target/generated-sources/openapi/`
4. Create a controller that `implements` the generated interface
5. Plugin config: `interfaceOnly=true`, `useSpringBoot3=true`, `useTags=true`, `skipDefaultInterface=true`, `openApiNullable=false`

### Dependency Injection

- Prefer **constructor injection** via `@RequiredArgsConstructor` (Lombok). All dependencies are `private final` fields.
- Do NOT use `@Autowired` on constructor-injected fields (it's redundant with `@RequiredArgsConstructor`). Some older files still have it — don't propagate this pattern.
- Use `@Bean` methods in `@Configuration` classes for framework beans (SecurityFilterChain, PasswordEncoder, etc.).

### SecurityUser and Principal Handling

- `SecurityUser` extends Spring's `User`, adds `Long id`. This enables `principal.id` in SpEL.
- `SecurityUser.from(AppUser)` is the **single conversion point**. All code that needs to create a SecurityUser from an AppUser must use this factory.
- **Cast once at the boundary.** Controllers cast `authentication.getPrincipal()` to `SecurityUser` once, then pass the typed object down. Services accept `SecurityUser` (or its fields) as parameters — they never cast internally.
- For endpoints where the user is already authenticated, prefer `@AuthenticationPrincipal SecurityUser user` as a method parameter — Spring does the cast for you.

### Error Handling

- Throw domain-specific exceptions from services (e.g., `UserNotFoundException`, `InvalidRefreshTokenException`).
- `GlobalExceptionHandler` (`@RestControllerAdvice`) catches them and maps to HTTP status codes.
- Error response format: `{"error": "<message>"}` using `Map.of("error", ex.getMessage())`.
- Do NOT catch exceptions in controllers — let them propagate to the global handler.

### Entity and DTO Patterns

- **Entities** use `@Data` (Lombok) and JPA annotations. They map directly to database tables.
- **DTOs** are generated from OpenAPI specs — never hand-write DTOs that the generator should produce.
- **Mapping:** Use MapStruct interfaces (e.g., `AppUserMapper`) for entity→DTO conversion. Annotate with `@Mapper(componentModel = "spring")`.

### @PreAuthorize Conventions

- Place `@PreAuthorize` on **service methods**, not controllers. This ensures authorization is enforced regardless of the caller.
- Use `@Param` (from `org.springframework.data.repository.query.Param`) on method parameters so SpEL can reference them by name.
- Provide an `*Internal()` variant (e.g., `findByUsernameInternal()`) for methods called by the authentication flow itself, where `@PreAuthorize` would create a circular dependency or fail because there's no authenticated user yet.
- Common SpEL patterns:
  - `hasRole('ADMIN')` — admin-only
  - `hasRole('ADMIN') or principal.id == #id` — admin or own resource by ID
  - `hasRole('ADMIN') or #username == authentication.name` — admin or own resource by username

### Adding a New Stage

1. Create `<Stage>SecurityConfig` in `config/` with `@Order(N)` and `securityMatcher("/api/<stage>/**")`
2. Write the OpenAPI spec in `src/main/resources/<stage>-api.yaml`
3. Add the generator execution in `backend/pom.xml`
4. Create the controller implementing the generated API interface
5. Add any new services, entities, or security components as needed
6. Add a SpringDoc group config in `application.properties`
7. CSRF is disabled on all chains (API-only, no browser forms)

---

## Architecture

### SecurityFilterChain Isolation

Each stage has its own `SecurityFilterChain` with `securityMatcher` + `@Order` for isolation. A request to `/api/basic-auth/me` only hits the BasicAuth chain. Order (lower = higher priority):

| Order | Config Class | Path | Auth Mechanism |
|-------|-------------|------|----------------|
| 0 | `AuthSecurityConfig` | `/api/auth/**` | None (permitAll) |
| 1 | `BasicAuthSecurityConfig` | `/api/basic-auth/**` | HTTP Basic |
| 2 | `SessionSecurityConfig` | `/api/session/**` | Form login + JSESSIONID |
| 3 | `JwtSecurityConfig` | `/api/jwt/**` | Bearer token (stateless) |
| 4 | `UsersSecurityConfig` | `/api/users/**` | JWT + HTTP Basic, RBAC |

### User Store Profiles

- `@Profile("inmemory")`: `InMemoryUserDetailsManager` with `user/password` (USER) and `admin/password` (ADMIN)
- `@Profile("db")` (active): `CustomUserDetailsService` backed by PostgreSQL `app_user` table
- Stages 1-3 work with either profile. Stage 4 (RBAC) requires `db` profile.

### Shared Security Beans (SharedSecurityConfig)

- `@EnableMethodSecurity` — enables `@PreAuthorize`
- `PasswordEncoder`: BCryptPasswordEncoder
- `AuthenticationManager`: from AuthenticationConfiguration
- `RoleHierarchy`: ADMIN implies USER (so ADMIN can access any USER-level endpoint)
- `MethodSecurityExpressionHandler`: wires role hierarchy into method security SpEL

---

## Stages

### Stage 1 — Basic Auth (`/api/basic-auth/**`)

HTTP Basic authentication. Credentials in `Authorization: Basic <base64>` header on every request.

Endpoints: `/public` (open), `/me` (authenticated), `/admin` (ADMIN role)

### Stage 2 — Session (`/api/session/**`)

Form login at `POST /api/session/login` with `username` + `password` form params. Server creates JSESSIONID cookie. Session creation: `IF_REQUIRED`. Logout at `/api/session/logout` returns 200 + "Logged out successfully".

Endpoints: `/public` (open), `/me` (authenticated, returns username + session ID), `/admin` (ADMIN role)

### Stage 3 — JWT (`/api/jwt/**`)

Stateless Bearer token authentication. `JwtAuthenticationFilter` (extends `OncePerRequestFilter`) extracts token from `Authorization: Bearer <token>` header, validates, and sets SecurityContext.

**Token endpoints:**
- `POST /api/jwt/token` — authenticate with username/password, returns access + refresh token
- `POST /api/jwt/token/refresh` — exchange refresh token for new pair

**User endpoints:** `/public` (open), `/me` (authenticated), `/admin` (ADMIN role)

**Signup:** `POST /api/auth/signup` — creates new user with ROLE_USER (separate chain at Order 0)

**JWT details:**
- Access token: 15 min expiry, HMAC-SHA signed, claims: `jti` (user ID), `sub` (username)
- Refresh token: 30 day expiry, UUID stored as SHA-256 hash in `refresh_token` table

**Refresh token rotation with theft detection:**
1. Raw UUID token given to client, SHA-256 hash stored in DB
2. On refresh: hash raw token, look up in DB
3. If token is revoked → ALL tokens for that user are revoked (theft signal) → 401
4. If expired → 401
5. Otherwise: revoke used token, issue new access + refresh pair

### Stage 4 — RBAC (`/api/users/**`)

User management with role-based access control. Dual-layer security: URL-level rules in `UsersSecurityConfig` + method-level `@PreAuthorize` in `AppUserService`.

**Endpoints:**

| Endpoint | Method | Access |
|----------|--------|--------|
| `/api/users` | GET | ADMIN (URL-level) |
| `/api/users/me` | GET | Authenticated |
| `/api/users/{id}` | GET | ADMIN or self (`principal.id == #id`) |
| `/api/users/{id}` | PUT | ADMIN or self |
| `/api/users/{id}` | DELETE | ADMIN (URL-level + `@PreAuthorize`) |
| `/api/users/{id}/role` | PUT | ADMIN (URL-level + `@PreAuthorize`) |
| `/api/users/me/password` | PUT | Authenticated (`#username == authentication.name`) |

**Internal vs public methods:** `findByUsernameInternal()` / `findByIdInternal()` skip `@PreAuthorize` so the authentication flow itself does not trigger authorization checks. Public `findByUsername()` / `findById()` have `@PreAuthorize`.

---

## Key Classes

### SecurityUser (`security/SecurityUser.java`)

Extends `org.springframework.security.core.userdetails.User`. Adds `Long id` field so `@PreAuthorize` SpEL can use `principal.id`. Static factory `SecurityUser.from(AppUser)` for clean conversion without casts.

### Role (`security/Role.java`)

Enum: `USER`, `ADMIN`. `authority()` returns `"ROLE_" + name()`. Use `Role.ADMIN.name()` in `hasRole()` and `roles()` calls.

### JwtTokenProvider (`security/JwtTokenProvider.java`)

All JWT operations. Public API:
- `generateAccessToken(SecurityUser)` — build signed JWT
- `generateRefreshToken(SecurityUser)` / `generateRefreshToken(AppUser)` — create + persist refresh token
- `parseAccessToken(String)` — validate JWT, load user from DB, return `UsernamePasswordAuthenticationToken`
- `validateAccessToken(String)` — check signature + expiry (does not throw, returns boolean)
- `validateRefreshToken(String)` — rotation + theft detection, returns RefreshToken entity

### JwtAuthenticationFilter (`security/JwtAuthenticationFilter.java`)

`OncePerRequestFilter`. Extracts Bearer token from Authorization header. If valid, calls `parseAccessToken()` and sets SecurityContext. If invalid or missing, does nothing (lets the chain continue — downstream security rules decide whether to reject).

### AppUserService (`service/AppUserService.java`)

Core CRUD + authorization. Key patterns:
- `signup()` — no auth required, assigns ROLE_USER
- `find*Internal()` — no `@PreAuthorize`, used by auth flow
- `find*()` / `update*()` / `delete*()` — `@PreAuthorize` protected
- `changePassword()` — verifies old password before changing

### CustomUserDetailsService (`service/CustomUserDetailsService.java`)

`@Profile("db")` `UserDetailsService` implementation. Loads `AppUser` from DB, converts to `SecurityUser` via `SecurityUser.from()`. Bean name: `customUserDetailsService`.

### Entities

- **AppUser** — `app_user` table: `id`, `username` (unique, max 50), `password`, `role` (Role enum as STRING), `name`, `email`
- **RefreshToken** — `refresh_token` table: `id`, `tokenHash` (unique, max 64), `appUser` (ManyToOne LAZY), `expiryDate` (Instant), `revoked` (boolean)

### Exception Handling (GlobalExceptionHandler)

| Exception | HTTP Status |
|-----------|-------------|
| `UserNotFoundException` | 404 |
| `UsernameAlreadyTakenException` | 409 |
| `RefreshTokenNotFoundException` | 401 |
| `InvalidRefreshTokenException` | 401 |
| `IllegalArgumentException` | 400 |

---

## Database

PostgreSQL at `localhost:5432/spring_security_playground`. JPA `ddl-auto=update` (auto-creates/updates schema).

Tables: `app_user`, `refresh_token`.

---

## Design Decisions

- **Isolated chains:** Each stage has its own SecurityFilterChain — they don't interfere with each other.
- **Multiple UserDetailsService beans:** Expected (in-memory + DB-backed) — selected via `@Profile`.
- **PinAuthenticationProvider warning:** The warning about AuthenticationProvider bean is intentional.
- **Single conversion point:** `SecurityUser.from(AppUser)` is used everywhere (CustomUserDetailsService, JwtTokenProvider, JwtTokenController).
- **Boundary casting:** Controllers cast `authentication.getPrincipal()` to `SecurityUser` once. Services accept typed parameters — never cast internally.
- **CSRF disabled:** All chains are API-only (no browser forms). CSRF protection is not needed.
- **Dual-layer security (Stage 4):** URL-level rules in SecurityFilterChain for coarse access + `@PreAuthorize` in service methods for fine-grained. Both layers must pass.
- **Internal methods pattern:** Service methods called during authentication (where no user is authenticated yet) have `*Internal()` variants without `@PreAuthorize`.

---

## Pitfalls and Gotchas

- **`@Param` is required** on service method parameters for SpEL to resolve `#paramName` in `@PreAuthorize`. Without it, Spring cannot match the parameter by name at runtime.
- **`findByUsername` vs `findByUsernameInternal`:** The public method has `@PreAuthorize` — using it during authentication (e.g., in `JwtTokenProvider.parseAccessToken()`) will fail because there's no authenticated user yet. Always use the `*Internal()` variant in auth flows.
- **Role hierarchy must be wired into MethodSecurityExpressionHandler.** Without `methodSecurityExpressionHandler(RoleHierarchy)` bean, `@PreAuthorize("hasRole('USER')")` would NOT match ADMIN users even though the hierarchy says ADMIN implies USER.
- **OpenAPI generator runs at compile time.** If you only edit the YAML spec, you must `./mvnw compile` before the IDE sees the new interfaces/DTOs.
- **`@Order` matters.** If two SecurityFilterChains match the same path, the one with the lower order wins. Always use `securityMatcher` to limit scope.
- **Refresh token is raw UUID to client, SHA-256 hash in DB.** Never store the raw token. The `Utils.sha256()` helper handles the hashing.
