# Development and container verification

The root Docker quick start is recommended. `docker compose logs -f app` shows startup/runtime diagnostics. `docker compose down` stops this project's app and preserves its database volume. The application is reachable only through the selected loopback port (default 8082); it runs as UID/GID 10001 with a read-only root filesystem and writable `/data` and temporary space. The JRE runtime includes the executable JSP-capable WAR and a small Java HTTP probe, not Maven/build tools.

`/health/live` is public process liveness. `/health/ready` performs `SELECT 1`, with bounded connection/query waits and generic failures. Both are independent of login/CSRF. Application startup must initialize H2 before readiness succeeds; normal pages remain authenticated and unsafe forms require CSRF.

## Validation

`make check` builds the image, which executes Maven regression tests before producing the WAR. BuildKit caches dependencies and passing unchanged build layers. The script chooses a new `gather-check-<timestamp>-<pid>` project, ephemeral host port and private test volume. Its standard-library Python HTTP client checks compiled JSPs, registration/BCrypt login, anonymous denial, CSRF, owner/member posting and idempotent joins. It replaces the app container, signs in again and verifies persisted accounts, group, membership and posts plus logout. Test accounts exist only in that disposable project; cleanup runs on success/failure/interrupt and prints application diagnostics on failure. Do not invoke the smoke service manually against developer or hosted data.

The smoke client does not execute JavaScript or download CDN assets; no visual/mobile/accessibility or production MySQL result is claimed. Database-failure tests verify a 503 without leaking connection details while liveness stays healthy. CI runs the same build/check command on PRs and main, with read-only repository permissions and build cache. Dependency review checks PR changes only; weekly dependency update PRs require human review. Do not infer that the entire older dependency graph has been audited.

## Native editing

Install JDK17, then use `./mvnw spring-boot:run` (default local port 8080) and `./mvnw --batch-mode verify`. No system Maven is required. Native H2 lives under ignored `data/`; Docker H2 lives in its named volume, so the two databases are separate. The `mysql` profile requires DB_URL/DB_USERNAME/DB_PASSWORD and a reviewed schema; do not select it for unattended verification or point automatic `ddl-auto` updates at real data.

## Persistence and reset

Use `docker compose down` and the root launch command to stop/restart without deleting data. Do not share an H2 volume between app instances. Changing `.env` changes the host port only. An intentional `docker compose down --volumes` permanently deletes **this developer project's** database; use it only when its contents are disposable. `make check` removes its own separately named test volume automatically and never resets the developer project. If a check is forcibly killed before cleanup, identify only that `gather-check-*` project's resources before removing it; never prune Docker globally.

## Remaining release boundaries

No production database, credentials, deployment or external identity service is exercised. The Spring dependency support baseline, full vulnerability inventory, MySQL compatibility, real hosting/HTTPS and visual review remain separate work. Existing tracker/issues remain authoritative; do not recreate work in active PRs.
