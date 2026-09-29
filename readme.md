# GatherLink Web
Find interest groups, join a community, and share posts with its members.

## What it does
Create an account, sign in, explore groups, create a group, join one, and post to groups you own or belong to. Group owners can assign member roles. Accounts can update their profile. Pages use native Spring MVC and JSP rather than a separate frontend application.

## Architecture and tech stack
Java 17, Spring Boot, Spring Security, Spring MVC/JSP/JSTL and JPA. Controllers handle page workflows; services validate data, enforce group access and perform transactions; repositories persist users, groups, memberships and posts. Passwords are BCrypt hashes, sessions authenticate requests and all mutation forms require CSRF tokens. User-generated output is escaped.

## Quick start
Install JDK 17. No manual MySQL installation is required for the preferred local workflow.

```sh
./mvnw spring-boot:run
```
Open http://localhost:8080 and choose **Create an account**. Local H2 storage persists in ignored `data/`; restarting preserves accounts and posts. The first run downloads Maven dependencies. No seeded account or shared default password is provided. The H2 console is disabled.

## Configuration
The default `local` profile uses a persistent H2 database. For an existing, provisioned MySQL environment, set `SPRING_PROFILES_ACTIVE=mysql`, `DB_URL`, `DB_USERNAME` and `DB_PASSWORD`. The MySQL profile validates an existing schema; production schema migration and HTTPS termination require separate deployment work. Secure session cookies are enabled in that profile. Never commit credentials or use the local profile for a public deployment.

## Testing
```sh
./mvnw verify
```
Integration tests use an isolated in-memory database and check registration/login, password hashing and non-disclosure, CSRF, membership and ownership boundaries, idempotent joining, posting and validation. The command also packages an executable WAR. GitHub Actions runs it with JDK 17 and a Maven cache.

## Project structure and data model
- `controller/`: registration, login, groups, posts and profile pages
- `service/`: validation, permissions and transactional operations
- `model/`, `repository/`: `Users`, `Groups`, `GroupMemberships`, `Posts`
- `src/main/webapp/WEB-INF/jsp/`: server-rendered templates

A group has one owner and unique user memberships. Posts belong to a group and an author. Joining locks the group and is idempotent; counters are recomputed from persisted rows during writes. Owner membership is implicit and excluded from `memberCount`.

## Design decisions
An embedded local database makes the existing JVM application reproducible without adding a container dependency. Spring Security supplies session fixation protection, login/logout and CSRF. The unused, unrestricted entity CRUD controllers were removed: browser workflows remain available, while an eventual public REST API must use scoped DTOs and explicit authorization.

## Known limitations and migration
Existing databases may contain plaintext passwords. They cannot authenticate under BCrypt; reset those accounts through a reviewed migration or use a fresh local database. Review duplicate usernames/emails and memberships before applying the new unique constraints. Historical counters may need a reconciliation migration. No credential rotation or database changes have been applied to a live environment.

Comments are a data model only, not an advertised working UI feature. Search, notifications, account recovery, production migrations, finer moderator permissions and a credential-backed MySQL test are future work. Roles do not grant administrative actions beyond the owner's existing role-management workflow. No deployment pipeline is supplied without a deployment target.

## Screenshots / demo
Run the local workflow to explore the native JSP interface. A reviewer can create two accounts to verify that joining is required before accessing group posts. Test data stays local.
