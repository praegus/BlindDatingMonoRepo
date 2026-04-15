---
name: openspec-test-traceability
description: Document and apply bidirectional traceability between OpenSpec scenarios and automated tests in BlindDatingMonoRepo. Use when adding or updating OpenSpec scenarios, writing or refactoring tests, or reviewing whether a requirement is explicitly linked to its verifying test through a stable scenario ID and file reference.
---

# OpenSpec Test Traceability

Use this skill to keep OpenSpec requirements and automated tests linked in both directions without turning the spec into implementation noise.

## Convention

For each scenario that has direct automated coverage:

1. Add a stable HTML comment immediately below the scenario heading:
   `<!-- openspec-id: <capability>.<scenario-slug> -->`
2. Add a `**Verification**` section under the scenario with one or more markdown links to the verifying tests.
3. Add the same scenario ID inside the test:
   - Prefer `@DisplayName("<capability>.<scenario-slug>")` when the test maps cleanly to one scenario.
   - Add a comment `// OpenSpec: <capability>.<scenario-slug>` in the test body as the grep-friendly backlink.

Example OpenSpec snippet:

```md
#### Scenario: Create a new profile
<!-- openspec-id: profile-lifecycle.create-new-profile -->
- **WHEN** a client submits a new username that does not already exist
- **THEN** the profile service creates a persisted profile record with that username and default profile substructures

**Verification**
- [ProfileServiceCreateProfileTest.shouldCreateAndPersistNewProfileWithInitializedSubstructures](../../../profile-service/src/test/java/io/praegus/bda/profileservice/business/ProfileServiceCreateProfileTest.java)
```

Example test snippet:

```java
@Test
@DisplayName("profile-lifecycle.create-new-profile")
void shouldCreateAndPersistNewProfileWithInitializedSubstructures() {
    // OpenSpec: profile-lifecycle.create-new-profile
}
```

## Rules

- Use IDs that stay stable if the scenario title is reworded.
- Keep IDs lowercase and hyphenated after the capability prefix.
- Link to the concrete test file, not to generated reports or IDE metadata.
- Allow multiple verification links when one scenario needs several tests.
- Allow one test to mention multiple scenario IDs only when it truly covers multiple scenarios; otherwise split the test.
- Do not embed line numbers in repo markdown links because they drift too easily.

## Workflow

When adding or updating a scenario:

1. Check whether automated coverage already exists.
2. If coverage exists, add or update the `openspec-id` comment and `**Verification**` links.
3. Patch the test so the same ID appears in `@DisplayName` or a comment.
4. Keep the scenario text requirement-focused. Put implementation detail in the verification block, not in the requirement bullets.

When adding or updating a test:

1. Identify the scenario it verifies.
2. Reuse an existing scenario ID if one exists.
3. If no stable ID exists yet, add it to the OpenSpec scenario before linking the test.
4. Update both sides in the same change so traceability does not drift.

## Review Checklist

- Does the scenario contain exactly one stable `openspec-id`?
- Does the scenario link to the verifying test file?
- Does the test contain the same scenario ID?
- Is the requirement still readable without knowing the test implementation?
- Would a repository search for the scenario ID find both the spec and the test?
