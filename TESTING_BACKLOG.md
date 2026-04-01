# Testing Backlog

This backlog translates the current testing and testability plan into concrete repository work that can be picked up step by step by a human or an AI agent.

The goal is to improve the system in two dimensions at the same time:
- make the monorepo more testable
- add the right tests at the right levels

## How to use this backlog

Rules:
- pick tasks in order unless there is a clear reason not to
- prefer finishing enabling work before adding dependent tests
- when a task changes runtime behavior, update the relevant markdown documents as part of the same work
- when a task adds a new test surface, also define how it runs in CI

## Phase 1: Make the system testable

### 1. Introduce Flyway in profile-service

Status: pending

Tasks:
- add Flyway to `profile-service`
- create baseline and follow-up migration scripts for the existing schema
- remove reliance on `hibernate.ddl-auto: create-drop`
- make local, component, and E2E environments start from the migrated schema
- update documentation to reflect that database schema is migration-driven

Definition of done:
- `profile-service` starts successfully using Flyway migrations
- schema creation is no longer done implicitly by Hibernate in normal test flows

### 2. Make matching triggerable on demand

Status: pending

Tasks:
- fix [`MatchingController.java`](C:\MyProjects\BlindDatingMonoRepo\matching-service\src\main\java\io\praegus\bda\matchingservice\adapter\web\MatchingController.java) so `POST /matching` returns a real HTTP response
- keep or remove the scheduler based on product preference, but do not require the scheduler for deterministic tests
- document how tests and developers should trigger matching

Definition of done:
- tests can trigger matching without waiting for the 10-second scheduler
- the matching API returns a valid response

### 3. Make location-service deterministic in testable environments

Status: pending

Tasks:
- make postcode lookup configurable by environment
- make restaurant lookup configurable by environment
- add controlled stub or fake implementations for test environments
- define known test addresses and known expected restaurant responses
- ensure the fallback path can also be triggered deterministically

Definition of done:
- component and E2E tests do not depend on uncontrolled live responses from external providers
- the happy path and fallback path can both be reproduced on demand

### 4. Add readiness strategy for all application services

Status: pending

Tasks:
- define how each service is considered ready for tests
- add readiness or health endpoints if needed
- add healthchecks in `docker-compose.yaml` where useful
- document which readiness checks the test harness must wait for

Definition of done:
- tests wait for service readiness instead of only container startup
- startup failures are easier to diagnose

### 5. Define and improve cleanup/reset surfaces

Status: pending

Tasks:
- keep using `DELETE /profiles` for profile cleanup
- keep using `DELETE /clear-match-statuses` for websocket in-memory cleanup
- review whether any additional reset hooks are needed
- document the standard cleanup order for E2E scenarios

Definition of done:
- each E2E scenario can start from a known clean state

## Phase 2: Protect interfaces and contracts

### 6. Enforce OpenAPI-based HTTP contract checks

Status: pending

Tasks:
- validate service behavior against the specs in `specs`
- add breaking-change checks for the OpenAPI files
- define how spec updates and implementation updates must move together

Definition of done:
- HTTP contracts are checked automatically in CI

### 7. Enforce Avro and schema-registry checks for Kafka

Status: pending

Tasks:
- define compatibility mode and subject naming expectations
- add schema compatibility checks in CI
- ensure producers and consumers use the registered schemas consistently

Definition of done:
- incompatible Kafka schema changes fail automatically before merge

### 8. Define WebSocket contract expectations

Status: pending

Tasks:
- document the real destinations and payload shapes for the current STOMP flow
- define what counts as a breaking change for the WebSocket contract
- add a lightweight contract-style test for the current destinations

Definition of done:
- the WebSocket interface is explicit enough to protect against accidental drift

## Phase 3: Add lower-level automated tests

### 9. Expand unit tests around core logic

Status: pending

Targets:
- matching score calculation
- date fallback behavior
- address validity branching
- websocket acceptance-state transitions

Tasks:
- identify existing unit test coverage per service
- add tests for important branches and edge cases
- prefer deterministic and isolated tests over broad mocks

Definition of done:
- core business logic is covered without depending on the full system

### 10. Add component tests for profile-service

Status: pending

Tasks:
- run the real service with real Postgres via Testcontainers
- include Flyway migrations
- stub outbound location HTTP calls
- verify profile creation, update, retrieval, and deletion
- verify dates are persisted when `ScheduledDate` is consumed

Definition of done:
- the service can be tested realistically without the full system

### 11. Add component tests for matching-service

Status: pending

Tasks:
- run the real service with controlled dependencies
- stub profile-service responses
- use embedded Kafka
- verify compatible profiles produce a `matchings` event
- verify incompatible profiles do not produce a `matchings` event

Definition of done:
- matching behavior is covered without needing full E2E

### 12. Add component tests for websocket-service

Status: pending

Tasks:
- run the real service with embedded Kafka
- connect a test WebSocket or STOMP client
- verify `matchings` input produces user-specific notifications
- verify one-sided acceptance does not produce `date-approvals`
- verify two-sided acceptance produces `date-approvals`

Definition of done:
- WebSocket notification and approval behavior are covered in isolation

### 13. Add component tests for date-service

Status: pending

Tasks:
- run the real service with embedded Kafka
- stub profile-service and location-service
- verify `date-approvals` input produces a `ScheduledDate`
- verify normal location generation path
- verify fallback location path

Definition of done:
- date generation behavior is covered without full E2E

### 14. Add component tests for location-service

Status: pending

Tasks:
- run the real service with controlled provider responses
- verify postcode lookup behavior
- verify midpoint calculation path
- verify nearest-restaurant selection path
- verify no-result scenarios

Definition of done:
- date-location generation can be validated deterministically

## Phase 4: Build the E2E system

### 15. Create the E2E test harness

Status: pending

Tasks:
- choose and set up the E2E test framework
- define how the test runner starts and stops the composed environment
- add readiness waiting
- add cleanup hooks before each test or scenario
- define how browser, HTTP, and WebSocket assertions are made

Definition of done:
- the repository has a runnable E2E test harness for the full stack

### 16. Implement the primary happy-path E2E flow

Status: pending

Tasks:
- create two unique users
- update both profiles with deterministic matching data
- validate the full flow through match, approval, and date creation
- assert the scheduled date is visible through externally observable behavior
- assert the date location follows the normal code path and is not the fallback address

Definition of done:
- one stable E2E test proves the full successful dating flow

### 17. Implement the fallback-location E2E flow

Status: pending

Tasks:
- force the system into the fallback date-location path
- run the full flow to completion
- assert the stored date location equals:
  - `De Wellenkamp`
  - `1141`
  - `6545NB`
  - `Nijmegen`

Definition of done:
- one stable E2E test proves the fallback location flow

### 18. Implement a negative E2E no-date flow

Status: pending

Tasks:
- choose one meaningful stop condition
- recommended first choice: one-sided acceptance only
- assert that no date is created when the flow is incomplete

Definition of done:
- one stable E2E test proves the system does not create dates for incomplete flows

### 19. Add optional product-relevant negative E2E scenarios

Status: pending

Candidates:
- login with non-existing profile
- incompatible profiles do not match
- invalid address causes fallback behavior if still product-relevant

Definition of done:
- only scenarios with clear user or business value are added

## Phase 5: CI integration

### 20. Run all test layers in CI for every PR

Status: pending

Tasks:
- define CI jobs for unit, component, contract, and E2E tests
- keep all jobs required for pull requests
- parallelize where possible to keep feedback time acceptable

Definition of done:
- every pull request runs the full required automated test suite

### 21. Publish useful failure artifacts

Status: pending

Tasks:
- capture browser screenshots on E2E failure
- capture relevant logs or container status
- capture enough evidence to diagnose WebSocket and Kafka-related failures

Definition of done:
- CI failures are actionable and diagnosable

### 22. Define flaky-test handling

Status: pending

Tasks:
- document that flaky tests must be fixed immediately
- do not normalize flaky tests with silent retries
- define how temporarily quarantined tests are tracked if ever needed

Definition of done:
- the pipeline remains trustworthy

## Recommended execution order

Use this order unless a specific task is intentionally pulled forward:

1. Introduce Flyway in `profile-service`
2. Make matching triggerable on demand
3. Make `location-service` deterministic in testable environments
4. Add readiness strategy for app services
5. Define cleanup/reset surfaces
6. Add HTTP and Kafka contract checks
7. Add unit tests for core logic
8. Add component tests per service
9. Create the E2E test harness
10. Implement the primary happy-path E2E flow
11. Implement the fallback E2E flow
12. Implement one negative no-date E2E flow
13. Integrate all test layers into CI

## Notes for humans and AI agents

When picking up a task from this backlog:
- state which task number is being worked on
- explain which risks are reduced by that task
- update related documentation when behavior or conventions change
- do not add broad E2E coverage before the underlying testability work is in place
- prefer making the system deterministic before compensating with retries or sleeps
