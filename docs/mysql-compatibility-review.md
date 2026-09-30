# Summary
Assess legacy MySQL account/data compatibility before production use.

## Problem
BCrypt and unique account/membership constraints differ from historical plaintext/duplicate/counter behavior. Existing production data has not been inspected.

## Why this matters
Accurate remaining work prevents duplicated implementation and keeps the portfolio safe and reproducible.

## Current behavior
Fresh persistent H2 local workflow and 10 tests pass. Optional mysql profile exists in https://github.com/adhvikrayaprolu/gather-link-web-app/pull/7; production database existence is unknown.

## Desired behavior
Owner records either no existing deployment or a reviewed migration plan; no automatic plaintext fallback or destructive schema updates.

## Proposed implementation
First confirm whether a deployed database exists. If so, take a protected backup and read-only schema/count inventory; identify duplicate normalized emails/usernames, duplicate memberships, orphaned references and password hash formats without printing values. Stage a sanitized copy, propose owner-approved reset/hash migration and deduplication, recompute counters, test constraints and rollback. Explicit approval required before production writes.

## Relevant files / modules
application-mysql.properties, Users.java, GroupMembership.java, UserService.java, docs/mysql-compatibility-review.md

## Acceptance criteria
- [ ] Existing deployment status confirmed
- [ ] If applicable, sanitized duplicate/reference/hash-format inventory completed
- [ ] Fixture migration/reset plan and rollback verified
- [ ] No plaintext fallback introduced
- [ ] Owner approves any production change or records no migration needed

## Testing requirements
Use H2 fixtures for duplicate identity/membership and invalid-reference cases; migration plan must preserve ownership and not leak credentials.

## Validation commands
`./mvnw verify`

## Dependencies
https://github.com/adhvikrayaprolu/gather-link-web-app/pull/7 review; owner confirmation of existing MySQL deployment.

## Agent execution notes
Human-review only; do not connect/write to a production DB, rotate credentials or use ddl-auto to repair live data automatically.

## Definition of done
All acceptance criteria verified, linked draft PR reviewed and merged by a human.

Tracking issue: https://github.com/adhvikrayaprolu/gather-link-web-app/issues/8
