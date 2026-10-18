#!/bin/sh
# All synthetic writes and cleanup target a new, explicitly named Compose project.
set -eu
repository=$(CDPATH= cd -- "$(dirname -- "$0")/../.." && pwd)
cd "$repository"
project="gather-check-$(date +%s)-$$"
export APP_PORT=0

cleanup() {
    result=$?
    trap - EXIT
    if [ "$result" -ne 0 ]; then
        docker compose -p "$project" logs --tail 80 app >&2 || true
    fi
    docker compose -p "$project" down --volumes --remove-orphans >/dev/null 2>&1 || true
    exit "$result"
}
trap cleanup EXIT
trap 'exit 130' INT
trap 'exit 143' TERM

docker compose config --quiet
docker compose -p "$project" up --build --wait app
runtime_uid=$(docker compose -p "$project" exec -T app id -u)
[ "$runtime_uid" = 10001 ] || { echo 'Application must run as UID 10001.' >&2; exit 1; }
docker compose -p "$project" --profile checks run --no-deps --rm \
    -e "SMOKE_DISPOSABLE_PROJECT=$project" smoke seed
# Replace the container, not its database volume. Browser sessions may expire;
# the second phase must log in again using the account persisted by the first.
docker compose -p "$project" up --no-deps --force-recreate --wait app
docker compose -p "$project" --profile checks run --no-deps --rm \
    -e "SMOKE_DISPOSABLE_PROJECT=$project" smoke persistence
printf '%s\n' 'Container checks passed; removing only the disposable verification project.'
