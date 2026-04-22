---
name: repo-testing-strategy
description: Apply the BlindDatingMonoRepo testing guidelines when adding, reviewing, or restructuring tests across the frontend, Spring services, Kafka, database, WebSockets, CI, or end-to-end flows. Use this whenever the task is about choosing the right test level, deciding between real Spring context collaborators versus Mockito or WireMock, reducing flakiness, enforcing realistic test data, aligning contracts, or making tests follow the repository's cross-cutting testing conventions. Use it together with framework-specific testing skills when implementation details are needed.
---

# Repo Testing Strategy

Use this skill for repository-wide testing decisions that are broader than a single framework or test library.

Do not duplicate framework-specific mechanics that are already covered by existing skills:

- Use `angular-testing` for Angular unit and component test implementation details.
- Use `spring-boot-testing` for Spring Boot slice, integration, and assertion patterns.
- Use `api-contract-testing` for HTTP contract tooling and provider/consumer verification details.
- Use `playwright` only when the task needs real browser automation from the terminal.

If generic framework guidance conflicts with this repository's testing conventions, follow this skill for policy and use the framework-specific skill only for implementation mechanics.

Your job here is to decide **what should be tested, at which level, with which real vs mocked dependencies, and with which anti-flakiness rules** for this monorepo.

## Primary goals

When writing or reviewing tests, optimize for:

1. Fast feedback to the team that can fix the defect.
2. The lowest test level that credibly covers the risk.
3. Realistic system behavior without unnecessary flakiness.
4. Explicit contract protection for cross-service interfaces.
5. CI results that are actionable and trustworthy.

## Decision workflow

Follow this sequence before writing or changing tests:

1. State the risk being covered in one or two sentences.
2. Identify who owns the likely fix if the behavior breaks.
3. Choose the lowest reliable test level that exposes the risk to that owner.
4. Decide which collaborators should be real and which should be mocked.
5. Choose realistic fixtures and unique mutable data.
6. Define synchronization and isolation so the test remains deterministic.
7. If an external interface is part of the risk, add explicit contract protection.

## Test level selection

Use this default order:

1. Pure unit test
2. Component or slice test
3. Contract test
4. End-to-end test

Move upward only if a lower level cannot cover the risk credibly.

Prefer failures to appear where the owning team can fix them:

- Mapping bugs belong in the service or component that owns the mapping.
- Frontend rendering and state bugs belong in frontend tests.
- Kafka consumer behavior belongs in consumer or service tests, not only in end-to-end flows.

If a proposed end-to-end test is mainly covering logic that could be shown more clearly in a lower-level test, recommend moving that coverage down and keeping only the highest-value business flow in end-to-end.

## Cross-cutting rules

### Realistic data

Use fixtures and mocks that resemble production payloads:

- Prefer sanitized or representative production examples.
- Preserve realistic field names, nullability, enums, nesting, and optional fields.
- Include production-relevant edge cases, not only minimal happy-path data.

### Unique mutable data

Any data created or mutated by a test must be unique per test:

- Use unique ids, emails, correlation ids, topic keys, and database values.
- Never rely on execution order.
- Do not share mutable records across tests.

### Reliability

Keep tests deterministic:

- Do not use arbitrary sleeps as the primary synchronization mechanism.
- Wait on observable conditions instead.
- Keep time controllable when possible.
- Minimize reliance on wall-clock timing.
- Avoid real external network calls in standard test runs.
- Reset or isolate state between tests.

If a test is known to be flaky, treat it as a defect to fix, not as acceptable noise.

## Repository conventions by area

### Frontend

Use frontend tests for:

- rendering logic
- validation and error states
- state transitions
- component interaction
- handling API responses and edge cases

Avoid broad mocks that hide actual behavior.
Avoid pushing pure presentation logic into end-to-end tests.

Implementation details should be delegated to `angular-testing`.

### Spring services and microservices

Cover these mainly in unit or component tests:

- business rules
- mappings between API, domain, messaging, and persistence
- transaction logic
- error handling
- retries, timeouts, and idempotency where relevant

Avoid mocking your own code when a real internal collaboration can be tested cheaply and reliably.
Avoid relying on controller-only tests when the real risk is deeper in service or persistence behavior.

For this repository, prefer real in-context Spring collaborators when testing behavior inside one deployable unit. If the subject under test is a Spring-managed service and the risk includes wiring, persistence, transactions, mapping, or configuration, keep the application context real and avoid replacing in-process beans with Mockito unless there is a specific isolation reason.

Implementation details should be delegated to `spring-boot-testing`.

### Component or slice tests

This repository treats component or slice tests as the default level for many functional requirements inside one deployable unit.

Default setup when the risk sits inside one service boundary:

- mock outbound HTTP with WireMock
- use a realistic PostgreSQL database via Testcontainers
- use embedded Kafka when Kafka behavior is part of the risk
- include Flyway migrations during test startup

Favor realistic out-of-process setups when they increase confidence without losing control.
Treat `@MockBean` or similar bean replacement inside `@SpringBootTest` as an exception, not the default.

### Contract protection

All important interfaces should be protected explicitly.

Rules:

- contract tests should be backed by schemas for all interfaces
- breaking change checks must run against those schemas
- contract tests protect interfaces; they do not replace component or end-to-end tests

Repository conventions:

- HTTP contracts should use the OpenAPI specs in `specs/`
- Kafka contracts should use Avro schemas only
- WebSocket conventions are not finalized yet, so call out assumptions explicitly

When collaboration with another team or service matters, make these points explicit:

- which endpoints or events are used
- why they are used
- which fields and semantics are critical

Implementation details for HTTP contract tooling should be delegated to `api-contract-testing`.

### Kafka

Kafka-specific required rules:

- define event contracts with Avro schemas
- keep a schema registry in place
- run breaking change checks against Kafka schemas
- control schema evolution explicitly

Test explicitly:

- schema validity and required fields
- serialization and deserialization
- consumer behavior when fields are added, removed, or missing
- compatibility behavior enforced through the schema registry
- idempotency under duplicate delivery
- retry and dead-letter behavior where applicable
- ordering assumptions only when ordering is functionally required

Never rely on exact timing in Kafka tests.
Use polling or condition-based waiting instead of sleeps.

### Database

Database rules:

- use Flyway-managed schemas
- component tests that use the database must include Flyway scripts
- test against the migrated schema, not a hand-built test schema

Test explicitly:

- repository queries
- constraints and uniqueness rules
- table mappings
- migrations that can break behavior
- transaction and rollback behavior when functionally relevant

Avoid dependence on shared database state.
Avoid assertions on unstable implementation details that do not matter to behavior.

### WebSockets

Test explicitly:

- connection lifecycle
- subscriptions and routing to the correct client or room
- payload structure
- reconnect or disconnect behavior when product-relevant
- invalid messages and authorization failures

Do not stop at asserting that "something" was sent; validate content and destination.

### End-to-end

Use end-to-end tests for:

- the most important user journeys
- cross-service flows
- integrations that become meaningful only when multiple deployables interact

Rules:

- each important integration should be covered at least once end-to-end
- end-to-end tests prove the main business process works
- they do not replace lower-level edge-case coverage

Repository environment rules:

- run end-to-end tests against the whole system defined by `docker-compose.yml`
- validate business flows against the real composed system
- explicit data clearing is acceptable when isolation is impractical, but it must be deliberate and reliable

If browser automation is required, delegate mechanics to `playwright`.

## CI policy

Apply these defaults in CI:

- all tests run on every pull request
- flaky tests must be fixed immediately
- do not normalize flaky failures as background noise

The goal is that a failing pipeline is trustworthy and actionable.

## Output requirements for test work

When this skill is used for a test addition or test review, explicitly state:

- which risk the test covers
- which level covers it
- which dependencies are real
- which dependencies are mocked
- why this level is more valuable than lower or higher alternatives

Keep this explanation brief but concrete.

## Review checklist

Use this checklist when reviewing or proposing tests:

- does the test cover a real risk?
- does it fail where the owning team can fix it?
- is this the lowest reliable test level?
- does it use realistic data?
- is mutable test data unique per test?
- does it avoid timing dependence and shared state?
- does it verify relevant behavior rather than implementation details?
- are external contracts explicitly protected?
- will the suite remain reliable in CI on every pull request?

## Anti-patterns to call out

Push back on these unless there is a strong reason:

- covering a service risk only through end-to-end tests
- using end-to-end tests for edge cases better covered lower down
- overmocking internal collaborators until wiring bugs disappear
- using unrealistic fixtures
- reusing mutable shared test data
- relying on fixed sleeps
- asserting implementation details instead of behavior
- hand-building database schemas instead of applying Flyway migrations
- shipping interface changes without schema or compatibility checks
- accepting flaky tests as normal

## Open points

If a task touches one of these unresolved conventions, say so explicitly and proceed with the safest local convention:

- frontend testing strategy still needs deeper repo-specific refinement
- WebSocket contract conventions still need to be defined
- the exact schema registry and compatibility enforcement setup may need repo-specific clarification
- some CI parallelization or serialization rules are still open
