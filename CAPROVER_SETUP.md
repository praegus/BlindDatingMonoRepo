# CapRover setup for BlindDatingMonoRepo

This repository already publishes Docker images to Docker Hub, so the fastest CapRover setup is to deploy each service from an existing image instead of building on the server.

## 1. First-time CapRover server setup

Open your dashboard at `http://178.104.111.173:3000` and complete:

1. Set the CapRover root domain.
2. Enable HTTPS after the root domain resolves to the server.
3. Change the admin password if you have not done that yet.

Use subdomains such as:

- `app.your-domain.com` for the Angular frontend
- `profile.your-domain.com`
- `websocket.your-domain.com`
- `matching.your-domain.com`
- `date.your-domain.com`
- `location.your-domain.com`

Internal-only services do not need public domains:

- `postgres`
- `kafka`
- `schema-registry`

Optional internal-only admin apps:

- `kafka-ui`
- `pgadmin`

## 2. Create the data services first

Create these apps in CapRover and deploy them using the specified images.

### postgres

- Image: `postgres`
- Container HTTP port: leave empty, this is not an HTTP service
- Persistent directory: mount a CapRover volume to `/var/lib/postgresql/data`
- Environment variables:
  - `POSTGRES_USER=postgres`
  - `POSTGRES_PASSWORD=58c46d6659d515c2`

### kafka

- Image: `bitnamilegacy/kafka:latest`
- Environment variables:
  - `KAFKA_CFG_NODE_ID=1`
  - `KAFKA_KRAFT_CLUSTER_ID=kraft-cluster`
  - `KAFKA_CFG_PROCESS_ROLES=broker,controller`
  - `KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@localhost:9093`
  - `KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093`
  - `KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://srv-captain--kafka:9092`
  - `KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT`
  - `KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER`
  - `ALLOW_PLAINTEXT_LISTENER=yes`

### schema-registry

- Image: `apicurio/apicurio-registry:latest`
- Container HTTP port: `8081`
- Environment variables:
  - `REGISTRY_LOG_LEVEL=INFO`
  - `REGISTRY_UI_FEATURES_READONLY=false`
  - `REGISTRY_STORAGE=mem`
  - `QUARKUS_HTTP_PORT=8081`
  - `REGISTRY_COMPATIBILITY_CONFLUENT=true`
  - `REGISTRY_KAFKA_BOOTSTRAP_SERVERS=srv-captain--kafka:9092`

## 3. Deploy the backend apps

Create one CapRover app per backend service and deploy with "Deploy via ImageName".

### profile

- Image: `docker.io/finkingma/profile-service:latest`
- Container HTTP port: `9080`
- Environment variables:
  - `spring.kafka.bootstrap-servers=srv-captain--kafka:9092`
  - `spring.kafka.properties.schema.registry.url=http://srv-captain--schema-registry:8081/apis/ccompat/v7`
  - `spring.datasource.url=jdbc:postgresql://srv-captain--postgres:5432/postgres?useSSL=false`
  - `spring.datasource.username=postgres`
  - `spring.datasource.password=229e198b5297432f`
  - `endpoints.locationservice=http://srv-captain--location:9084`

### location

- Image: `docker.io/finkingma/location-service:latest`
- Container HTTP port: `9084`

### matching

- Image: `docker.io/finkingma/matching-service:latest`
- Container HTTP port: `9081`
- Environment variables:
  - `spring.kafka.bootstrap-servers=srv-captain--kafka:9092`
  - `spring.kafka.properties.schema.registry.url=http://srv-captain--schema-registry:8081/apis/ccompat/v7`
  - `endpoints.profileservice=http://srv-captain--profile:9080`

### date

- Image: `docker.io/finkingma/date-service:latest`
- Container HTTP port: `9083`
- Environment variables:
  - `spring.kafka.bootstrap-servers=srv-captain--kafka:9092`
  - `spring.kafka.properties.schema.registry.url=http://srv-captain--schema-registry:8081/apis/ccompat/v7`
  - `endpoints.profileservice=http://srv-captain--profile:9080`
  - `endpoints.locationservice=http://srv-captain--location:9084`

### websocket

- Image: `docker.io/finkingma/websocket-service:latest`
- Container HTTP port: `9082`
- Environment variables:
  - `spring.kafka.bootstrap-servers=srv-captain--kafka:9092`
  - `spring.kafka.properties.schema.registry.url=http://srv-captain--schema-registry:8081/apis/ccompat/v7`

## 4. Deploy the frontend

Create app `frontend` and deploy either from this repository or from a pushed image.

If you build from this repository, set these environment variables on the app:

- `PROFILE_API_URL=https://profile.your-domain.com`
- `WEBSOCKET_URL=https://websocket.your-domain.com/ws`

If you deploy a Docker image, rebuild and push the frontend image after the changes in this commit so the runtime config support is included.

Frontend container settings:

- Image: `docker.io/finkingma/blinddatingapp-frontend:latest`
- Container HTTP port: `80`

## 5. Optional admin apps

### kafka-ui

- Image: `provectuslabs/kafka-ui:latest`
- Container HTTP port: `8080`
- Environment variables:
  - `KAFKA_CLUSTERS_0_NAME=BlindDating`
  - `KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=srv-captain--kafka:9092`
  - `KAFKA_CLUSTERS_0_SCHEMAREGISTRY=http://srv-captain--schema-registry:8081/apis/ccompat/v7`

### pgadmin

- Image: `dpage/pgadmin4`
- Container HTTP port: `80`
- Persistent directory: mount a CapRover volume to `/var/lib/pgadmin`
- Environment variables:
  - `PGADMIN_DEFAULT_EMAIL=user-name@domain-name.com`
  - `PGADMIN_DEFAULT_PASSWORD=strong-password`

## 6. Important notes

- CapRover apps can talk to each other using the internal app address format `srv-captain--<app-name>`.
- Do not use `localhost` inside CapRover between containers.
- Expose only the frontend and the APIs you actually want public.
- The frontend was updated to read `PROFILE_API_URL` and `WEBSOCKET_URL` at container startup, which is required for production deployment.
- The backend services already override their local `application.yaml` values through environment variables, so no Java code changes were required for service-to-service networking.
