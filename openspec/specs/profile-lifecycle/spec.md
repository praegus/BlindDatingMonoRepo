## ADDED Requirements

### Requirement: User profiles can be created and loaded by username
The system SHALL allow a user to create a profile with a unique username and SHALL allow an existing profile to be loaded by that username for editing in the frontend.

#### Scenario: Create a new profile
<!-- openspec-id: profile-lifecycle.create-new-profile -->
- **WHEN** a client submits a new username that does not already exist
- **THEN** the profile service creates a persisted profile record with that username and default profile substructures

**Verification**
- [ProfileServiceCreateProfileTest.shouldCreateAndPersistNewProfileWithInitializedSubstructures](../../../profile-service/src/test/java/io/praegus/bda/profileservice/business/ProfileServiceCreateProfileTest.java)

#### Scenario: Frontend treats successful creation as navigation to the profile page
- **WHEN** the frontend submits a profile creation request and the backend responds with success semantics for creation
- **THEN** the user is routed to the created profile page for that username

#### Scenario: Reject duplicate profile creation
- **WHEN** a client submits a username that already exists
- **THEN** the profile service rejects the request as a conflict and leaves the existing profile unchanged

#### Scenario: Load an existing profile
- **WHEN** the frontend requests an existing profile by username
- **THEN** the system returns the profile and the frontend can route the user to the profile page for that username

#### Scenario: Handle a missing profile on login
- **WHEN** the frontend requests a profile by username that does not exist
- **THEN** the system reports the profile as not found and the user is not routed to the profile page

### Requirement: Profile updates persist personal data and validate address changes
The system SHALL persist submitted profile details and SHALL revalidate the address through the location service whenever the stored address fields change.

#### Scenario: Update non-address profile fields
- **WHEN** a client updates profile information such as names, additional information, characteristics, preferences, or dislikes
- **THEN** the profile service persists those values on the existing profile

#### Scenario: Revalidate a changed address
- **WHEN** a client changes the street, street number, postal code, or city of a profile
- **THEN** the profile service calls the location service and stores the returned validity, normalized address data, and coordinates on the profile

#### Scenario: Preserve address validation when the address did not change
- **WHEN** a client updates a profile without changing the stored address fields
- **THEN** the profile service persists the profile update without re-running address validation

### Requirement: Profile reads include scheduled dates
The system SHALL store scheduled dates on both matched profiles and SHALL return those dates as part of the profile representation.

#### Scenario: Persist a scheduled date onto both profiles
- **WHEN** the profile service consumes a scheduled date event for two matched users
- **THEN** it appends the scheduled date details to both involved profiles

#### Scenario: Return scheduled dates to clients
- **WHEN** a client retrieves a profile that has one or more scheduled dates
- **THEN** the response includes the date time, address, and item to bring for each scheduled date
