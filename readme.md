# GatherLink Web
An interest-group community application for discovering groups, joining them and sharing posts through a browser.

## Overview
GatherLink organizes community discussion around shared interests, with group owners, members and authenticated posting.

## Project Context
Developed during a software engineering internship at ClayHR. The [Android application](https://github.com/adhvikrayaprolu/gather-link-mobile-app) explores the same GatherLink product using Firebase. These are related clients/prototypes, not a shared live database: web uses JPA/H2 or MySQL; Android uses Firestore.

## Key Features
- Registration and BCrypt-backed session login.
- Group discovery/creation and idempotent membership joins.
- Member/owner post access and owner-controlled role changes.
- CSRF-protected forms, validated input and escaped JSP output.

## Architecture / Tech Stack
Spring Boot MVC + Spring Security → services/authorization boundaries → JPA repositories → persistent local H2. JSP/CSS renders server-side pages. Optional production MySQL uses environment configuration; local startup requires no database installation.

## Quick Start
Install JDK17; use the checked-in Maven wrapper:
```sh
./mvnw spring-boot:run
```
Open http://localhost:8080, register an account, create or join a group, and post. The default `local` profile stores H2 data under ignored `data/`; it survives restart. No credentials are edited into source. Stop with Ctrl-C.

## Validation / Tests
```sh
./mvnw verify
```
Spring integration tests cover real login, password hashing, CSRF, authorization, input validation, idempotent joins, counters and retired unscoped API writes. CI builds the WAR and executes these tests with isolated H2.

## Environment Variables
Default local H2 needs none. The optional `mysql` profile requires `DB_URL`, `DB_USERNAME` and `DB_PASSWORD`, a provisioned schema and HTTPS/cookie configuration. Review `application-mysql.properties` before using an existing database; legacy plaintext passwords and duplicate/counter data need migration review.

## Project Structure
`src/main/java/com/gather_link/`: controllers, security, services, models and repositories; `src/main/webapp/WEB-INF/jsp/`: views; `src/main/resources/`: profile configuration; `src/test/`: integration suite.

## Current Status / Limitations
The web flow supports joining; leaving a group is not implemented and remains a scoped issue. Comments and a public entity CRUD API are not advertised as completed features. Production MySQL and existing-data migration require human verification. The deprecated unrestricted entity endpoints were removed to prevent bypassing MVC authorization.

## Related Projects
[GatherLink Android](https://github.com/adhvikrayaprolu/gather-link-mobile-app) — native client for the same community concept, with separate Firebase persistence.

Read [AGENTS.md](AGENTS.md) and GitHub Issues before implementation.
