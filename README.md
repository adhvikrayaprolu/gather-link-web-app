# GatherLink Web

Discover interest groups, join communities and share posts through a browser.

## Demo / Screenshots

No hosted demo or reviewed screenshot is published. Run the real app locally below.

## What it does / Key Features

- Registration and BCrypt-backed session login.
- Group creation/discovery and idempotent membership joins.
- Member/owner post access and owner-controlled role changes.
- CSRF-protected forms, validated input and escaped JSP output.

Developed during a software engineering internship at ClayHR. The related Android prototype uses separate Firebase persistence; these clients do not share a live database.

## Architecture / Tech Stack

```text
Browser → Spring MVC / JSP + Spring Security → services → JPA → persistent H2
```

Java 17 and an executable WAR; one non-root container and a database volume. No MySQL installation is needed.

## Quick Start

Install Docker with Compose v2, then:

```sh
git clone https://github.com/adhvikrayaprolu/gather-link-web-app.git
cd gather-link-web-app
cp .env.example .env
docker compose up --build --wait
```

Open **http://localhost:8082**, register, create or join a group, and post. The first build downloads dependencies and runs regression tests. No accounts are seeded. H2 persists across container replacement.

## Configuration

`.env.example` selects the loopback port. Docker uses local H2; real environment/database/build files are excluded. See [development](docs/development.md) for native editing, logs and intentional resets; [MySQL compatibility](docs/mysql-compatibility-review.md) requires separate review.

## Testing

```sh
make check
```

Builds the tested WAR, checks real registration/login/CSRF/group/post behavior, then recreates the container and verifies persistence. Only its disposable project is removed; developer data is preserved. CI runs the same command. PR dependency review flags newly introduced high/critical risks, not historical vulnerabilities.

## API / Data Model

Users own groups, memberships authorize access, and posts belong to groups/creators. Unscoped CRUD endpoints are retired. `/health/live` reports the process; `/health/ready` also checks the database, returning generic 503 on failure.

## Project Structure

- `src/main/java/com/gather_link/`: controllers, security, services, JPA models/repositories.
- `src/main/webapp/WEB-INF/jsp/`: views; `src/main/resources/`: profile configuration.
- `src/test/`: regression tests; `scripts/container/`: disposable packaged-HTTP checks.

## Design Decisions / Known Limitations

H2 supports one local app per volume. Bootstrap styling needs CDN access. HTTP tests do not establish visual/mobile/accessibility readiness; MySQL hosting and dependency support need separate verification.

## Future Work

Group leave remains [issue #3](https://github.com/adhvikrayaprolu/gather-link-web-app/issues/3); comments are not advertised as complete. Legacy MySQL migration review is [#8](https://github.com/adhvikrayaprolu/gather-link-web-app/issues/8). See [readiness tracker #5](https://github.com/adhvikrayaprolu/gather-link-web-app/issues/5). Nothing is deployed automatically.
