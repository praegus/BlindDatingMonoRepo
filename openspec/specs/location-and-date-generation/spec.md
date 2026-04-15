## ADDED Requirements

### Requirement: Profile addresses are validated through the location service
The system SHALL validate profile addresses through the location service and SHALL persist the returned validation result on the profile.

#### Scenario: Validate an address during profile creation or update
- **WHEN** a profile is created or its address fields change
- **THEN** the profile service calls the location service and stores the returned address validity and coordinates

#### Scenario: Return not found for an unresolvable address lookup
- **WHEN** the location service cannot resolve a submitted address
- **THEN** the address lookup endpoint returns not found instead of a successful address payload

### Requirement: Date generation uses the normal location pipeline when both addresses are valid
The system SHALL generate a date from an approved match by loading both profiles, resolving a date location from both validated addresses, and publishing the scheduled date.

#### Scenario: Generate a venue from two valid profile addresses
- **WHEN** the date service receives an approved match for two profiles whose addresses are valid
- **THEN** it requests a generated date location from the location service using both profile addresses

#### Scenario: Calculate the date location from the geographic midpoint
- **WHEN** the location service receives exactly two addresses with coordinates
- **THEN** it calculates the midpoint of those addresses and returns a nearby restaurant for that midpoint when one is found

#### Scenario: Refuse generated date lookup for incomplete address input
- **WHEN** the location service receives anything other than exactly two addresses with usable coordinates
- **THEN** the generated date-location request does not produce a generated venue

#### Scenario: Publish the scheduled date event
- **WHEN** the date service has loaded both profiles and resolved a date address
- **THEN** it publishes a scheduled date event containing both usernames, the chosen address, a time five days in the future, and the item to bring

### Requirement: Date generation falls back when a generated venue is unavailable
The system SHALL still schedule a date when generated venue lookup cannot be completed and SHALL use the configured fallback address.

#### Scenario: Use fallback for invalid profile addresses
- **WHEN** either matched profile lacks a valid address
- **THEN** the date service schedules the date using the fallback address `De Wellenkamp 1141, 6545NB Nijmegen`

#### Scenario: Use fallback when generated venue lookup fails
- **WHEN** date-location generation throws an error or returns no location
- **THEN** the date service schedules the date using the fallback address `De Wellenkamp 1141, 6545NB Nijmegen`

#### Scenario: Choose the item to bring from favorite color preferences
- **WHEN** the date service generates a date
- **THEN** the item to bring is `Sok (<color>)`, using the first available favorite color from the two profiles' preferences and `Geel` when neither profile provides one
