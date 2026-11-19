## Requirements

### Requirement: Use standard Keycloak authentication
The frontend SHALL offer Login and Register using the standard Keycloak forms. Registration SHALL require only a username and password, without email verification, custom themes, or application password handling. Users SHALL be able to log out and later log in using their password.

#### Scenario: Open hosted forms
<!-- openspec-id: authentication.hosted-forms -->
- **WHEN** an unauthenticated user chooses Login or Register
- **THEN** the frontend opens the corresponding Keycloak form.

**Verification**
- [Login component test](../../../blinddatingapp-frontend/src/app/login/login.spec.ts)

#### Scenario: Protect profile navigation
<!-- openspec-id: authentication.protected-navigation -->
- **WHEN** a user without a session opens the profile page
- **THEN** the frontend returns them to the welcome page.

**Verification**
- [Authentication guard test](../../../blinddatingapp-frontend/src/app/auth/auth.guard.spec.ts)

#### Scenario: Send tokens only to the profile API
<!-- openspec-id: authentication.token-destination -->
- **WHEN** the frontend requests the profile API
- **THEN** it refreshes the access token before sending it, stops the request if refresh fails, and never attaches the token to another origin.

**Verification**
- [Authentication interceptor tests](../../../blinddatingapp-frontend/src/app/auth/auth.interceptor.spec.ts)

### Requirement: Enforce ownership for authenticated profile operations
The profile service SHALL validate JWT signature, expiry, issuer, and audience. It SHALL derive ownership from the immutable Keycloak subject and username, rather than trusting a supplied username.

#### Scenario: Validate tokens
<!-- openspec-id: authentication.validate-tokens -->
- **WHEN** a protected endpoint receives a missing, malformed, expired, incorrectly signed, wrong-issuer, or wrong-audience token
- **THEN** it rejects the request with HTTP 401.

**Verification**
- [ProfileAuthenticationTest](../../../profile-service/src/test/java/io/praegus/bda/profileservice/ProfileAuthenticationTest.java)

#### Scenario: Provision and update an owned profile
<!-- openspec-id: authentication.own-profile -->
- **WHEN** an authenticated user first opens their profile
- **THEN** the backend provisions it from their JWT identity, returns the same profile on subsequent visits, and allows the owner to update it.

**Verification**
- [ProfileAuthenticationTest](../../../profile-service/src/test/java/io/praegus/bda/profileservice/ProfileAuthenticationTest.java)

#### Scenario: Prevent editing another account's profile
<!-- openspec-id: authentication.isolate-profiles -->
- **WHEN** a user changes a path/body username or registers a new account with a previously used username
- **THEN** the backend denies access to the protected profile operations unless the JWT subject owns the profile. Legacy arbitrary creation and bulk deletion are disabled.

**Verification**
- [ProfileAuthenticationTest](../../../profile-service/src/test/java/io/praegus/bda/profileservice/ProfileAuthenticationTest.java)

#### Scenario: Allow unauthenticated service reads
<!-- openspec-id: authentication.public-service-reads -->
- **WHEN** a backend service reads GET /profiles or GET /profiles/{username} without a token
- **THEN** it receives profiles, but unauthenticated profile writes remain prohibited. Both read endpoints are intentionally also accessible to other callers. Angular still requires login before displaying a profile.

**Verification**
- [ProfileAuthenticationTest](../../../profile-service/src/test/java/io/praegus/bda/profileservice/ProfileAuthenticationTest.java)

#### Scenario: Restrict browser origins
<!-- openspec-id: authentication.browser-origins -->
- **WHEN** a browser performs a cross-origin preflight
- **THEN** the profile API allows only configured frontend origins.

**Verification**
- [ProfileAuthenticationTest](../../../profile-service/src/test/java/io/praegus/bda/profileservice/ProfileAuthenticationTest.java)
