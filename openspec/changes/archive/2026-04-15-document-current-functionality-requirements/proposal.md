## Why

The repository has working code, API contracts, and end-to-end notes, but it does not yet have OpenSpec capability requirements that describe the current product behavior in one place. Writing those requirements now creates a baseline for future changes, reviews, and implementation work.

## What Changes

- Add OpenSpec capability specifications that document the current user-visible and integration-visible behavior of the system.
- Capture the current profile lifecycle, including profile creation, retrieval, updates, address validation, and scheduled date persistence.
- Capture the current matching workflow, including scheduled match generation, websocket notifications, dual acceptance, and Kafka-driven date approval.
- Capture the current location and date generation behavior, including address lookup, midpoint-based venue generation, and fallback date location behavior.

## Capabilities

### New Capabilities
- `profile-lifecycle`: Covers profile creation, login, retrieval, editing, address validation, and viewing scheduled dates.
- `matching-workflow`: Covers automated match generation, websocket delivery, acceptance handling, and event propagation toward date creation.
- `location-and-date-generation`: Covers address validation, generated date-location lookup, and fallback date scheduling behavior.

### Modified Capabilities
- None.

## Impact

Affected areas include the Angular frontend, `profile-service`, `matching-service`, `websocket-service`, `date-service`, `location-service`, the root OpenAPI/Avro contracts under `specs/`, and future E2E or regression work that needs a requirements baseline.
