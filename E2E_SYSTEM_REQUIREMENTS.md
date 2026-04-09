# End-to-End System Requirements

This document describes the requirements for the end-to-end test system of this monorepo, based on the current codebase and runtime topology.

The purpose of the E2E system is to validate the real business flow across the full deployed stack, not to replace unit, component, or contract tests.

## Scope

The E2E system must test the composed system defined in `docker-compose.yaml`:
- frontend
- profile-service
- matching-service
- websocket-service
- date-service
- location-service
- kafka
- schema-registry
- postgres

Support tools that may be used for debugging but are not the primary test target:
- kafka-ui
- pgadmin

## System under test

### Entry points

The E2E system must treat these interfaces as the externally relevant surfaces:
- frontend at `http://localhost:8080`
- profile API at `http://localhost:9080`
- matching API at `http://localhost:9081`
- websocket/STOMP endpoint at `http://localhost:9082/ws`
- date-service at `http://localhost:9083`
- location API at `http://localhost:9084`
- schema registry at `http://localhost:8081`

### Main runtime dependencies

The current code shows these cross-service dependencies:
- frontend talks to profile-service over HTTP and websocket-service over SockJS/STOMP
- profile-service persists data in Postgres and calls location-service over HTTP
- matching-service reads profiles from profile-service and publishes matches to Kafka topic `matchings`
- websocket-service consumes `matchings`, pushes notifications over WebSockets, and publishes approved matches to Kafka topic `date-approvals`
- date-service consumes `date-approvals`, reads both profiles from profile-service, asks location-service for a date location, and publishes scheduled dates to Kafka topic `dates`
- profile-service consumes `dates` and writes the resulting date into both involved profiles

### Kafka contracts

The current event contracts are:
- `matchings` topic with Avro schema `Match`
- `date-approvals` topic with Avro schema `Match`
- `dates` topic with Avro schema `ScheduledDate`

The E2E system must assume Kafka and the schema registry are part of the real runtime path and not bypass them.

## What the E2E system must prove

The E2E system must prove that the whole business flow works across real process boundaries:

1. a user can create or load a profile through the frontend
2. a profile can be enriched and persisted
3. profile address validation flows through location-service
4. matching-service detects a valid match from persisted profile data
5. the match event reaches websocket-service through Kafka
6. both users receive their match notification through WebSockets
7. both users can accept the match through the WebSocket/STOMP flow
8. websocket-service publishes a date approval event to Kafka when both sides accepted
9. date-service consumes the approval, resolves the two profiles, determines a date location, and emits a scheduled date event
10. profile-service consumes the scheduled date event and stores the date on both profiles
11. the frontend can show the resulting scheduled date after reload

## Required end-to-end journeys

The E2E system should focus on full business flows, not fragmented subflows.

### Journey 1: Full successful dating flow

This is the primary end-to-end journey and must prove the complete system works from start to finish.

It must prove:
- two users can be created or loaded through the frontend
- both profiles can be updated with deterministic matching data
- profile address validation flows through location-service
- matching-service detects the match
- websocket-service delivers match notifications to both users
- both users accept the match through the STOMP/WebSocket flow
- websocket-service publishes the approval event
- date-service generates the date
- the generated date location matches the normal location-generation path from the code
- profile-service stores the generated date on both profiles
- the frontend can show the resulting date

Important current behavior:
- matching runs automatically on a scheduler every 10 seconds
- there is a `POST /matching` endpoint in the spec and controller, but the current controller returns `null`
- the frontend does not currently call the matching API

The E2E system must therefore support scheduled matching as the primary trigger unless the application code changes.

For Journey 1, the date-location assertion must follow the actual code path:
- date-service loads both profiles and reads their validated coordinates
- location-service calculates the geographic midpoint of those two coordinates
- location-service calls Overpass and selects the nearest restaurant within a 5 km search radius around that midpoint
- date-service uses that returned address as the scheduled date location

This means Journey 1 should not assert one hardcoded venue from repository code.
It should instead assert that:
- the final scheduled date location equals the address returned by the normal date-location generation path for those two validated profiles
- the final scheduled date location is not the fallback address

### Journey 2: Full flow with date-location fallback

This journey is a variant of the main successful flow and must prove the system still completes the business flow when date-location generation cannot return a generated venue.

It must prove:
- the same full business flow succeeds end to end
- date-service falls back to the hardcoded default address when date-location generation fails or when the profile addresses are invalid
- both profiles still receive the scheduled date

The fallback address is defined in code as:
- street: `De Wellenkamp`
- streetNumber: `1141`
- postalCode: `6545NB`
- city: `Nijmegen`

### Journey 3: Negative flow without a date

This journey exists to prove that the system does not create a date when the business flow is incomplete.

It must prove at least one meaningful stop condition, for example:
- only one matched user accepts, so no approval event is published and no date is created
- two users are not compatible enough to match, so no match notification and no date are created

Negative journeys should stop before date creation by design. They exist to prove that dates are created only for valid completed flows.

## Test data requirements

The E2E system must use deterministic, isolated test data.

Rules:
- every test run must create unique usernames
- tests must not assume pre-existing profiles
- compatibility data must be explicit so matching is deterministic
- data must resemble real profile payloads, not minimal placeholders

### Deterministic match data

Because matching requires a score of at least `70`, the E2E suite must use profile data that safely clears that threshold.

A robust strategy is:
- give each user personal information that matches the other user's preferences on the high-value fields
- especially align gender, because that is weighted heavily in the matching score
- avoid setting dislikes that cancel the score

## Environment requirements

The E2E system must run against the real composed environment from `docker-compose.yaml`.

Required runtime characteristics:
- all services are started from Docker Compose
- Kafka is available and healthy before dependent services start
- schema registry is available before Avro-based flows are exercised
- Postgres is running and reachable by profile-service
- the frontend is reachable on port `8080`

The E2E system should wait for readiness, not just container startup.

Readiness must include:
- frontend HTTP reachability
- profile-service HTTP reachability
- websocket-service WebSocket handshake reachability
- location-service HTTP reachability
- matching-service process availability
- date-service process availability
- Kafka topic path readiness for the event-driven flow

## Data lifecycle requirements

The E2E system must keep tests repeatable while acknowledging that the full system is stateful.

Rules:
- prefer unique data over global shared fixtures
- allow explicit cleanup when isolation is not practical
- cleanup must be reliable and deliberate, not best-effort

Known cleanup surfaces in the current code:
- profile-service supports deleting all profiles through `DELETE /profiles`
- websocket-service supports clearing in-memory match state through `DELETE /clear-match-statuses`

The E2E system should use those endpoints where needed, especially between scenarios that would otherwise leak state.

## Observability requirements

The E2E system must be able to assert behavior through externally visible evidence.

Preferred evidence:
- browser state and rendered UI
- HTTP API responses
- WebSocket messages received by the client
- persisted profile state after the flow completes

Debug evidence that may be used when diagnosing failures:
- Kafka topic inspection
- container logs
- Postgres inspection
- schema registry inspection

The E2E system should not depend primarily on logs for pass/fail decisions when a user-visible or API-visible assertion is available.

## Timing and synchronization requirements

The E2E system must be event-aware and timing-safe.

Rules:
- do not use fixed sleeps as the main synchronization mechanism
- wait for observable conditions
- account for the matching-service scheduler, which currently runs every 10 seconds
- allow asynchronous propagation across Kafka, WebSockets, and persistence

Examples of valid synchronization points:
- a profile can be fetched successfully
- a WebSocket message arrives for a subscribed user
- a profile contains a scheduled date

## Browser requirements

The E2E system should support at least one browser-based test path through the frontend because the frontend is part of the full business flow.

The browser tests must be able to:
- create a profile from the login page
- navigate to the profile page
- edit profile data
- remain connected to the WebSocket endpoint
- observe match and approval notifications
- reload or revisit the profile to confirm scheduled dates

## Interface requirements

### HTTP

HTTP behavior in E2E must align with the published OpenAPI specs under `specs`, with the current code behavior treated as the runtime truth when there is a mismatch.

Important current code-driven notes:
- the frontend already works around profile creation response quirks by treating HTTP `200` or `201` in the error path as success
- the matching API exists, but it is not currently the main business trigger for matching

### WebSockets

The current WebSocket flow uses:
- SockJS endpoint `/ws`
- application destination prefix `/app`
- acceptance destination `/app/accept/{username}`
- user notification topics `/topic/matchings/{username}` and `/topic/approved/{username}`

The E2E system must validate these real destinations rather than a hypothetical contract.

### Database

Postgres is part of the real E2E environment because profile-service persists profile state there.

Current implementation note:
- profile-service currently uses JPA `create-drop`

This means the E2E system must not assume Flyway-backed production-like migrations are already in place today.

### External providers

Location-service uses external lookup adapters in code.

The E2E system must decide explicitly how external network dependence is handled:
- either run only in an environment where those dependencies are reachable and stable
- or introduce controlled substitutes specifically for E2E

Because the current docker-compose setup does not define stubs for those providers, this is a current system-level risk for E2E stability.

## Failure reporting requirements

When an E2E scenario fails, the test system should make it clear which stage failed:
- profile creation
- profile update
- address validation
- matching emission
- websocket notification
- acceptance handling
- date approval publication
- date generation
- scheduled date persistence
- frontend display

The goal is to keep E2E failures diagnosable even though they cover a full distributed flow.

## Minimum recommended scenario set

The E2E suite should at least contain:
- full successful dating flow from profile creation to visible scheduled date
- full successful dating flow with fallback date location
- one negative scenario where the flow stops before date creation
- login attempt for a non-existing profile if that behavior is considered product-relevant

## Current implementation constraints and risks

These are not generic requirements. They are current codebase facts that the E2E system must account for:

- matching is currently scheduler-driven every 10 seconds
- the `POST /matching` controller currently returns `null`
- frontend profile creation already contains a workaround for response handling quirks
- websocket match status is kept in memory inside websocket-service and may need explicit clearing between tests
- profile persistence currently relies on JPA schema creation instead of Flyway
- location-service may depend on external providers not represented in Docker Compose

These should either be accepted as current constraints or addressed in the application before expecting a highly reliable E2E suite.

## Out of scope

The E2E system is not the right place to exhaustively test:
- detailed matching score edge cases
- every invalid payload permutation
- every schema compatibility rule
- every repository constraint in isolation
- every frontend rendering branch

Those belong mainly in unit, component, and contract tests.
