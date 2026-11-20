# Local authentication

The Angular welcome page offers Login and Register. Both open Keycloak's built-in forms. Registration requires a username (1–20 letters/digits), a password (at least 8 characters), and password confirmation. Email is not collected. There are no custom themes, password endpoints, or service-account clients.

The frontend uses the public `blinddating-frontend` client with authorization code flow and PKCE. Tokens stay in memory and are refreshed before profile API calls. The profile service validates signature, issuer, expiration, and the `profile-service` audience. Profiles are bound to the stable JWT `sub` and username; changing a URL or request body cannot grant write access to another profile.

On first authenticated access, `PUT /account/profile` creates the dating profile. Subsequent calls return the same profile. Old profiles are not claimed by matching their username. This change does not delete existing data.

## Start locally

1. Create a root `.env` file with a random `KEYCLOAK_ADMIN_PASSWORD`. `.env` is ignored by Git. For example, in PowerShell:

   ```powershell
   if (Test-Path .env) { throw '.env already exists; update it without overwriting other settings.' }
   $bytes = New-Object byte[] 32
   $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
   $rng.GetBytes($bytes)
   $rng.Dispose()
   'KEYCLOAK_ADMIN_PASSWORD=' + [Convert]::ToBase64String($bytes) | Set-Content .env
   ```

2. Start Keycloak:

   ```powershell
   docker compose up -d keycloak
   ```

   Keycloak is at http://localhost:8180. The admin username is `admin`; its password is the value in `.env`. The `blinddating` realm is imported automatically on first startup. Accounts persist in the `keycloak-data` volume.

3. Run the profile service with Java 25 and its usual PostgreSQL/Kafka dependencies, and start Angular:

   ```powershell
   # In profile-service
   .\mvnw.cmd spring-boot:run
   # In blinddatingapp-frontend, in another terminal
   npm ci
   npm start
   ```

   Open http://localhost:4200. Register, save profile details, log out, then log in again with the same username/password.

## Running application images in Compose

Compose uses local application images; rebuild changed services before starting them. From the repository root:

```powershell
docker build -f blinddatingapp-frontend/Dockerfile -t docker.io/finkingma/blinddatingapp-frontend .
.\profile-service\mvnw.cmd -f profile-service/pom.xml -DskipTests compile jib:dockerBuild
docker compose up -d
```

Open http://localhost:8080. Both port 4200 and port 8080 are registered redirect origins. Keycloak's browser-facing issuer stays `http://localhost:8180/realms/blinddating`; the profile container fetches signing keys through the internal `http://keycloak:8080` address.

The Angular image supports `KEYCLOAK_URL`, `KEYCLOAK_REALM`, `KEYCLOAK_CLIENT_ID`, and `PROFILE_API_URL`. The profile service supports `KEYCLOAK_ISSUER_URI`, `KEYCLOAK_JWK_SET_URI`, and `app.cors.allowed-origins`. Changing client IDs also requires adjusting the backend's allowed frontend client.

Startup imports do not overwrite an existing realm. After changing the JSON, apply the relevant settings through the Keycloak admin console. Changing the bootstrap password in `.env` also does not reset an existing admin password.

## Access rules

`GET /profiles` and `GET /profiles/{username}` are deliberately public for backend service reads. Matching and date services keep their existing direct calls without service credentials. Both endpoints are accessible to anyone who can reach the profile service, including callers outside the frontend. Angular still requires login before showing a profile. Profile writes require an authenticated owner; arbitrary profile creation and bulk deletion are disabled. WebSocket authentication is outside this change.

Email-based password recovery is unavailable because registration does not collect email. An administrator can reset a forgotten password in Keycloak.

## Verification

Run `./mvnw.cmd test` from the profile-service directory. The authentication tests exercise the real security chain, JWT decoder, services, Flyway, and PostgreSQL via Testcontainers. WireMock supplies signing keys instead of a remote identity provider. This covers backend ownership and token-validation failures without depending on a running Keycloak.

From the frontend directory:

```powershell
npm run build
npx ng test --watch=false --browsers=ChromeHeadless --include=src/app/auth/*.spec.ts --include=src/app/login/login.spec.ts
```

The focused frontend tests cover hosted-form buttons, protected navigation, token destination, and refresh failure. A real-browser smoke check covers registration, profile provisioning, saving, logout, and subsequent password login against local Keycloak and the profile service. Matching/date/WebSocket workflows are separate from this authentication check.


# Deploy

## Deploy to docker hub
```
docker build --no-cache -t docker.io/finkingma/blinddatingapp-keycloak:latest .
docker push docker.io/finkingma/blinddatingapp-keycloak:latest
```
that way the docker image can be used by caprover as well

## Setup in Caprover
the tabs do not seem to save by themselves, its best to first configure the app configs, then the deployment, and finish with http settings

### Http Settings
- Container Http Port: 8080
- Enable HTTPS: enabled

### App Configs
- Env vars
KC_BOOTSTRAP_ADMIN_USERNAME=admin
KC_BOOTSTRAP_ADMIN_PASSWORD=Pl2ygR0nd
KC_DB=dev-file
KC_HOSTNAME=https://keycloak.blinddatingapp.finkingma.com
KC_HTTP_ENABLED=true
KC_PROXY_HEADERS=xforwarded

- persistent directories
path: /opt/keycloak/data/
label: keycloak-h2-v2

### Deployment
Imagename: docker.io/finkingma/blinddatingapp-keycloak:latest

### Testing
Afterwards you should be able to login using https://keycloak.blinddatingapp.finkingma.com/admin/