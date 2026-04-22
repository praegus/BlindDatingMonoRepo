## ADDED Requirements

### Requirement: Matching runs automatically against persisted profiles
The system SHALL attempt to create matches from persisted profiles on a recurring schedule and SHALL create a match when two profiles reach the configured compatibility threshold.

#### Scenario: Skip matching when too few profiles exist
- **WHEN** the matching process runs and fewer than two profiles exist
- **THEN** the system produces no match events

#### Scenario: Emit a match for compatible profiles
- **WHEN** the matching process evaluates two profiles whose computed matching score is at least 70
- **THEN** the system publishes a match event for that pair

#### Scenario: Scheduled matching remains the primary trigger
- **WHEN** the system is running with persisted profiles
- **THEN** match creation is driven by the recurring matching schedule every 10 seconds

#### Scenario: Matching evaluates each unique profile pair once per run
- **WHEN** the matching process loads the persisted profile set for a matching run
- **THEN** it compares unique profile pairs and only publishes match events for pairs that meet the threshold

### Requirement: Matched users receive websocket notifications
The system SHALL deliver match notifications to both matched users through the websocket service when a match event is consumed.

#### Scenario: Notify both matched users
- **WHEN** the websocket service consumes a new match event
- **THEN** it stores acceptance state for that match and sends notifications to `/topic/matchings/{username}` for both matched usernames

#### Scenario: Avoid duplicate rematching after approval
- **WHEN** the websocket service receives a match event for a pair that already reached the fully accepted state
- **THEN** it suppresses duplicate match notifications for that pair

### Requirement: Date approval requires acceptance from both users
The system SHALL wait for both matched users to accept the match before publishing a date approval event.

#### Scenario: Record the first acceptance
- **WHEN** one matched user sends an acceptance message to `/app/accept/{username}`
- **THEN** the websocket service records that acceptance and does not yet publish a date approval event

#### Scenario: Publish approval after both accept
- **WHEN** both matched users have accepted the same match
- **THEN** the websocket service sends approval notifications to `/topic/approved/{username}` for both users and publishes a date approval event to Kafka

#### Scenario: Reject acceptance for an unknown match
- **WHEN** an acceptance message is received for a match that is not tracked in the websocket service state
- **THEN** the acceptance is rejected and no date approval is published

#### Scenario: Ignore repeated approval after a completed match
- **WHEN** an acceptance arrives for a match that is already in the fully accepted state
- **THEN** the websocket service does not start a second date-approval flow for that pair
