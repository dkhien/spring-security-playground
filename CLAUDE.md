# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Security Playground - an interactive web app for "Spring Security 101" sharing sessions. Each security concept is a self-contained scenario under its own URL prefix with its own SecurityFilterChain.

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.5.x, Spring Security 6.x, Maven
- **Frontend**: React 18+ (TypeScript), Vite, Tailwind CSS
- **Database**: PostgreSQL (H2 for tests), Flyway migrations

## Build & Run Commands

### Backend
```bash
# Maven is available via wrapper
mvn -pl backend compile          # Compile
mvn -pl backend test             # Run tests (uses H2)
mvn spring-boot:run -pl backend  # Run app (needs PostgreSQL)
```

### Frontend
```bash
cd frontend
npm install         # Install deps
npm run dev         # Dev server (port 5173, proxies /api to :8080)
npm run build       # Production build
```

### Docker
```bash
docker compose -f docker/docker-compose.dev.yml up    # PostgreSQL only
docker compose -f docker/docker-compose.yml up --build # Full stack
```

## Architecture

### Scenario-per-URL-Prefix Pattern
Each demo lives in `backend/.../scenario/{name}/` with:
- `{Name}Descriptor.java` - implements `ScenarioDescriptor` (auto-discovered)
- `{Name}SecurityConfig.java` - `@Bean SecurityFilterChain` with `securityMatcher("/api/demo/{name}/**")`
- `{Name}Controller.java` - REST endpoints under `/api/demo/{name}/`

### Key Packages
- `common/` - shared entities (AppUser, AppRole), repos, AppUserService, DTOs, config
- `scenario/` - ScenarioDescriptor interface, ScenarioRegistry, ScenarioController + all scenario sub-packages
- `debug/` - FilterChainDiagnosticService, DebugController

### Scenarios (14 total)
Authentication Basics: form-login, http-basic, in-memory, db-users, password-encoding, session
Authorization: role-based, method-security
JWT: jwt
Security Internals: filter-chain, security-context
Customization: custom-filter, custom-provider
OWASP: owasp

### Frontend Structure
- `api/` - Axios client with request tracing
- `components/common/` - EndpointTester, RequestResponseViewer, CredentialPanel, ExplanationCard
- `components/layout/` - AppLayout, Sidebar
- `pages/` - HomePage, ScenarioPage (generic, loads any scenario)
- `hooks/` - useApiCall (traced HTTP), useScenarios

### Test Accounts (seeded via Flyway)
- user/password (USER), admin/admin (ADMIN), manager/manager (MANAGER)
- locked/locked (locked account), disabled/disabled (disabled account)
