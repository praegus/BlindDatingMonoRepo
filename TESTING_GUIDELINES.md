# Test Writing Guidelines

This document describes how we write valuable, reliable automated tests in this monorepo with a frontend, multiple microservices, Kafka, a database, and WebSockets.

Goals:
- make risks visible early to the team that can fix them
- keep tests reliable and minimize flakiness
- give realistic feedback about actual system behavior
- make the rules explicit enough that humans and AI agents apply them consistently

## How to use this document

Use these rules as the default when:
- adding new tests
- reviewing existing tests
- deciding at which level a risk should be covered
- designing mocks, fixtures, test data, contract checks, and CI coverage

When in doubt:
1. choose the test that makes the risk most visible to the person who can fix it
2. then choose the context where you have the most control and the least flakiness
3. only move to a higher integration level if a lower level cannot cover the risk credibly

## Core principles

### 1. Cover risks where the solver will see them

Write the test as close as possible to the part of the system where the problem should be fixed.

Practical application:
- test a mapping bug in the service that owns the mapping, not only through an end-to-end test
- test frontend rendering and state bugs in frontend tests
- test Kafka consumer behavior in the consumer or service tests, not only in a full-system flow

Why:
- failures are easier to understand
- the owning team gets actionable feedback immediately
- regressions are localized faster

### 2. Cover risks in the context where you have the most control

Use the lowest test level that can credibly cover the risk.

Preferred order:
1. pure unit test
2. component or slice test
3. contract test
4. end-to-end test

Move to a higher level only when needed, for example when:
- the collaboration between processes is itself the risk
- serialization, transport, or configuration is part of the risk
- infrastructure behavior must be part of the test

Why:
- lower levels are faster
- lower levels are more stable
- failures are more specific and cheaper to fix

### 3. Use realistic data for mocks and fixtures

Mocks and fixtures should look like real production data.

Guidelines:
- base example responses on sanitized or representative production examples where possible
- include edge cases that actually happen in production
- do not use unrealistically minimal JSON when the real contract is much richer
- keep field names, nullability, enums, nesting, and optional fields aligned with the real interface

Why:
- tests protect better against real regressions
- parsing, mapping, and validation issues show up earlier
- contract drift becomes visible sooner

### 4. Make mutable test data unique per test

Data that a test creates or modifies must be unique to that test.

Guidelines:
- use unique ids, email addresses, correlation ids, topic keys, and database values per test
- do not share mutable records between tests
- do not let tests depend on execution order
- avoid reusing fixed identifiers if that can cause collisions or state leakage

Why:
- tests can run in parallel
- tests become repeatable
- cross-test contamination is reduced

### 5. Communicate actively with dependencies

When another team or service provides a dependency, it must be clear:
- which endpoints or events are used
- why they are used
- which fields or semantics are critical

Practical application:
- document the external interfaces that are actually used
- make contract expectations explicit
- align on breaking changes before they ship
- tie test coverage to the interfaces and fields that matter in practice

Why:
- fewer surprises between teams
- faster understanding of change impact
- less risk of implicit dependencies

### 6. Manage external interfaces against accidental breaking changes

Protect integrations explicitly with contract tests, compatibility checks, or schema validation.

Examples:
- schema validation for Kafka events
- compatibility checks for external payloads
- explicit versioning strategy for events and endpoints

Why:
- breaking changes become visible early
- dependencies remain explicitly managed
- releases become more predictable

## Test level conventions

These are the default conventions for this repository.

### Unit tests

Use unit tests mainly for:
- advanced edge cases
- technical cases
- pure business rules
- parsing, mapping, validation, and transformation logic
- error handling branches that are hard to trigger from higher levels

Unit tests should:
- run fast
- isolate the unit under test
- focus on logic, not infrastructure wiring

Do not rely on unit tests alone for end-to-end business confidence.

### Component or slice tests

Functional requirements should mainly be covered by component or slice tests.

Use them for:
- realistic behavior inside one application or bounded slice
- collaboration between controllers, services, persistence, messaging adapters, and configuration within one deployable unit
- Flyway-backed database verification
- verifying that important user or API behavior works with real internal wiring

Component or slice tests are the default choice when:
- unit tests are too narrow to give confidence
- end-to-end tests would be too slow or flaky
- the risk sits inside one service or application boundary

In this repository, component tests should generally be:
- out of process
- configured as realistically as possible
- built to minimize outside influence while keeping maximum control

Default component test setup:
- mock outbound HTTP dependencies with WireMock
- use a realistic PostgreSQL database through Testcontainers
- use embedded Kafka where Kafka behavior is part of the test
- include Flyway migrations as part of test startup

### End-to-end tests

End-to-end tests should cover business processes across the system.

Use them for:
- the most important user journeys
- cross-service flows
- full integrations that only become meaningful when multiple deployables interact together

Rules:
- each important integration should be covered at least once in end-to-end tests
- end-to-end tests should prove that the main business process works, not replace lower-level coverage
- do not push detailed edge-case coverage into end-to-end tests when a lower level can cover it better

### Contract tests

All interfaces should be protected by explicit contracts.

Rules:
- contract tests should be backed by schemas for all interfaces
- breaking change checkers must run against those schemas
- contract protection is required because interface changes can break other services without changing local logic

Interface-specific conventions:
- HTTP contracts should use the OpenAPI specs that already exist in the `specs` directory
- Kafka contracts should use Avro schemas only
- WebSocket contract conventions are still open and should be defined later

Contract tests exist to protect interfaces, not to replace component or end-to-end tests.

## Decision rules by risk type

### Frontend

Frontend guidance is intentionally light for now and should be expanded later.

Test in frontend tests:
- rendering logic
- validation and error states
- state transitions
- component interaction
- handling of API responses and edge cases

Avoid:
- unnecessary end-to-end tests for pure presentation logic
- broad mocks that hide actual behavior

### Microservices

Test in unit or component tests:
- business rules
- mappings between API, domain, messaging, and persistence
- transaction logic
- error handling
- retries, timeouts, and idempotency where relevant

Avoid:
- relying only on controller tests when the real risk is in service or persistence behavior
- mocking your own code when a real collaboration can be tested cheaply and reliably

### Kafka

Kafka is the only place in this repo where Avro schemas should be used.

Required rules:
- Kafka event contracts must be defined with Avro schemas
- a schema registry must be in place
- breaking change checks must run against Kafka schemas
- schema evolution must be controlled explicitly

Test explicitly:
- event schema validity and required fields
- serialization and deserialization
- consumer behavior when fields are added, removed, or missing
- compatibility behavior enforced through the schema registry
- idempotency under duplicate delivery
- retry and dead-letter behavior where applicable
- ordering assumptions only if ordering is functionally required

Avoid:
- tests that assume exact timing
- sleeps as a synchronization strategy when polling or condition-based waiting is possible

### Database

Database migrations should be managed with Flyway.

Required rules:
- component tests that use the database must include Flyway scripts
- database behavior must be tested against migrated schema, not against hand-built test schema setup

Test explicitly:
- repository queries
- constraints and uniqueness rules
- mappings to tables
- migrations that can break existing behavior
- transaction and rollback behavior where functionally relevant

Avoid:
- dependence on shared database state
- assertions on irrelevant details that change often during refactoring

### WebSockets

Test explicitly:
- connection lifecycle
- subscriptions and routing to the correct client or room
- payload structure
- reconnect or disconnect behavior when product-relevant
- invalid messages and authorization failures

Avoid:
- tests that only check that "something" was sent without validating content and destination

## Reliability and flakiness

Valuable tests are deterministic.

Guidelines:
- do not use arbitrary sleeps as the primary synchronization mechanism
- wait on observable conditions
- keep time controllable where possible
- minimize reliance on wall clock time
- avoid network calls to real external systems in standard test runs
- reset or isolate state between tests

Test data rule:
- prefer isolated environments and unique test data over cleanup-heavy test design
- when full isolation is not practical, cleanup is acceptable, but it must be explicit, reliable, and part of the test design
- this is especially relevant for end-to-end tests in this system, where clearing data may be necessary

If a test is regularly flaky, treat that as a quality problem, not a cosmetic problem.

## CI policy

These rules apply in CI:
- all tests must run in CI on every pull request
- flaky tests must be fixed immediately
- flaky tests must not be accepted as normal background noise

The goal is that a failing pipeline is actionable and trustworthy.

## End-to-end environment

End-to-end tests in this repository should run against the whole system.

Rules:
- all services should be started inside the environment defined by `docker-compose.yml`
- end-to-end tests should validate business flows against the real composed system
- end-to-end tests may require explicit data clearing because this environment is not trivially isolated per test
- cleanup in end-to-end tests is acceptable when required, but it should stay deliberate and reliable

## What every important test should make clear

Every important test should make clear:
- which risk it covers
- at which level it covers that risk
- which dependencies are real and which are mocked
- why this test is more valuable at this level than at another level

## Review checklist for new tests

Use this checklist when writing or reviewing tests:

- does the test cover a real risk?
- does it fail where the owning team can fix the problem?
- is this the lowest reliable test level for the risk?
- does it use realistic data?
- is all mutable test data unique per test?
- does it avoid timing dependence and shared state?
- does it verify relevant behavior instead of implementation details?
- are external contracts explicitly protected?
- does the test suite remain reliable in CI for every pull request?

## Common anti-patterns

Avoid these patterns unless there is a clear and defensible reason:

- covering a service risk only through end-to-end tests when the owning service could have caught it earlier
- using end-to-end tests for edge cases that belong in unit or component tests
- overmocking internal collaborators so heavily that wiring and configuration bugs disappear
- using unrealistic fixtures that do not resemble production data
- reusing mutable shared test data across tests
- relying on fixed sleeps instead of waiting for observable conditions
- asserting implementation details when business behavior is what matters
- building test database schema by hand instead of applying Flyway migrations
- letting interface changes ship without schema or contract compatibility checks
- accepting flaky tests as normal instead of treating them as defects

## Instructions for AI agents

When an AI agent adds or changes tests in this repository:
- state briefly which risk is being covered
- choose the lowest test level that can cover the risk reliably
- use realistic payloads and domain values
- introduce unique test data per test case
- add contract protection when an external interface is part of the risk
- avoid flakiness through condition-based waiting and controlled test state
- explain why the test is valuable at the chosen level
- follow the repository conventions for component, end-to-end, contract, Kafka, and Flyway-backed database testing

## Open points to refine later

This document should still be expanded with repo-specific rules for:
- frontend testing strategy
- naming conventions for tests
- the exact setup for schema registry and compatibility enforcement
- whether specific categories should be parallelized or serialized in CI
