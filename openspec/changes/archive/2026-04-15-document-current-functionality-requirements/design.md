## Context

This change documents the current system behavior as OpenSpec requirements without changing runtime code. The current functionality is spread across the Angular frontend, five Spring services, root OpenAPI and Avro contract files, and an end-to-end requirements document. The design needs to translate that implementation reality into stable capability specs that future changes can reference.

The system is a distributed flow: users create and update profiles through the frontend and `profile-service`, addresses are validated through `location-service`, `matching-service` produces candidate matches on a 10-second schedule, `websocket-service` delivers match and approval notifications and publishes approvals to Kafka, and `date-service` creates dates that are written back into both profiles.

## Goals / Non-Goals

**Goals:**
- Establish a requirements baseline for the current product behavior.
- Separate that baseline into capability-oriented specs that can be modified independently later.
- Record important current constraints where they materially affect externally visible behavior.

**Non-Goals:**
- Changing existing implementation behavior.
- Resolving current inconsistencies between contracts and implementation.
- Designing new features, APIs, or migration steps beyond documenting the baseline.

## Decisions

### Decision: Organize the baseline into three capability specs
The change groups requirements into `profile-lifecycle`, `matching-workflow`, and `location-and-date-generation`.

Rationale:
- These align with the user-facing flow and service boundaries already present in the codebase.
- They keep future changes targeted instead of forcing all behavior into one oversized spec.

Alternatives considered:
- One monolithic "current-system" spec: rejected because it would be hard to evolve.
- One spec per service: rejected because requirements here are business-flow oriented and cross service boundaries.

### Decision: Describe current runtime truth, including known quirks
The specs document what the code currently does, even where that behavior is imperfect, such as scheduled matching as the primary trigger and fallback date generation.

Rationale:
- The user asked for requirements for current functionality.
- A baseline is only useful if it matches the running system that tests and future changes must preserve or intentionally change.

Alternatives considered:
- Describing only the intended ideal behavior from API contracts: rejected because parts of the implementation already differ.

### Decision: Keep design light and focus detail in the spec scenarios
The design explains grouping and source-of-truth choices, while the normative behavior lives in capability specs.

Rationale:
- This is a documentation-first change, not an implementation redesign.
- OpenSpec specs are the artifact that downstream changes and reviews will consume most directly.

Alternatives considered:
- Capturing all behavior in design.md: rejected because it would weaken the spec artifacts.

## Risks / Trade-offs

- Current code and published contracts are not perfectly aligned -> The specs use runtime behavior as the baseline where implementation is clear, reducing ambiguity for future work.
- A documentation baseline can become stale if code changes bypass OpenSpec -> Future changes must update the relevant capability specs as part of normal change management.
- Some behaviors are integration-specific and span multiple services -> The capability split is business-flow oriented so cross-service behavior remains testable without overfitting to class structure.

## Migration Plan

No runtime migration is required. Adoption consists of using these new capability specs as the baseline for future OpenSpec proposals and implementation work.

## Open Questions

- Whether the repository should later add separate specs for internal event contracts such as Kafka topics and Avro schemas.
- Whether current implementation quirks, such as the `POST /matching` response behavior, should be corrected in a follow-up change after the baseline is established.
