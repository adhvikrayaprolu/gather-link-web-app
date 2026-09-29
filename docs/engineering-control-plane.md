# Engineering control plane

Discover, join and participate in interest groups through a browser.

## Setup and validation
Use JDK17. ./mvnw spring-boot:run -Dspring-boot.run.profiles=local uses existing ephemeral H2 without MySQL. ./mvnw verify currently builds without behavior tests. Persistent/env-driven local setup remains an issue.

## Verified state
Published main audit SHA: `44388f720523214a592b6d1be8587a83b4a007b0`. No root AGENTS.md, issues or PRs existed at this audit. No existing Actions pipeline or meaningful behavior test suite in published main.

## Unmerged work
Earlier local branch `codex/gatherlink-web-security-foundation` at `1be79d25d0a5f19f86083721cbdd8639d26a84fb` has tested improvements, but is not hosted or merged. Review/reuse it before reimplementing. Its reported checks are not checks of this control-plane branch.

## Backlog and stop rule
Use GitHub issues after publication; local draft identifiers must never be treated as GitHub issue numbers. Portfolio tracking covers core flows, safe configuration, meaningful tests, green PR CI, reproducible setup and concise demo documentation. Stop after the tracker is complete; no speculative features.

## Queue
`is:issue is:open label:"automation:ready" sort:updated-asc` scoped to this repository. Apply priority and dependency checks from AGENTS.md.
