# Blind Dating App MonoRepo

## Starting the System

### 1. Start all
Now run the command `docker compose up` for the rest of the services.

### 2. Opening the application
- Navigate to http://localhost:8080 to open the frontend
- Navigate to http://localhost:7777 to open the kafka ui
- Navigate to http://localhost:8888 to open the pgadmin ui (user-name@domain-name.com/strong-password, see https://stackoverflow.com/questions/25540711/docker-postgres-pgadmin-local-connection for how to connect)

## Run application locally
springboot application can be run locally by executing the Application class (i.e. MatchingServiceApplication).
If you want to make changes to one of the applications you can stop it in the docker compose setup and run it locally.
Be aware that some dockerfiles depend on other services as well (matching service and dating service depend on the profile service). 
So if you want to work on the profile service, you need to either run those services locally as well, or reconfigure those docker images to connect to your localhost using something like this:
https://forums.docker.com/t/how-to-reach-localhost-on-host-from-docker-container/113321

## Update application
Once you are satisfied with the changes of an application, you can update the docker image by running the command:
- `mvn compile jib:build` from within the module (frontend is a bit different, check createDockerImages.bat for commands)
- After that, you can update the docker image in the compose setup using `docker compose pull`
- Finally you need to start the service again with the pulled docker file, using `docker compose up -d --force-recreate date` (for the date service)

## CapRover memory limits (4 GB VM)

The following per-container limits were verified in the deployed environment with `docker stats --no-stream`. These are development settings for the 4 GB Hetzner VM (approximately 3.73 GiB usable RAM), not load-tested production minimums. They are configured in CapRover; the local `docker-compose.yaml` does not apply these limits.

| CapRover app | Memory limit | `MemoryBytes` |
| --- | ---: | ---: |
| keycloak | 768 MiB | 805306368 |
| profile-service | 384 MiB | 402653184 |
| location-service | 256 MiB | 268435456 |
| matching-service | 288 MiB | 301989888 |
| date-service | 256 MiB | 268435456 |
| websocket-service | 288 MiB | 301989888 |
| kafka | 512 MiB | 536870912 |
| schema-registry | 256 MiB | 268435456 |
| postgres | 192 MiB | 201326592 |
| frontend | 32 MiB | 33554432 |
| pgweb | 32 MiB | 33554432 |
| kafka-ui | 320 MiB | 335544320 |

Kafka UI was running with one replica in the latest verified snapshot. The application limits total **3,584 MiB with Kafka UI running**, or **3,264 MiB with it stopped**, assuming one replica per app. Limits are ceilings, not reserved memory. Linux, Docker, CapRover, and monitoring also need memory, so running every app near its limit would leave insufficient host headroom. Stop Kafka UI when unused by setting its instance count to **0** in CapRover; restore it to **1** when needed. CapRover's own containers retain their existing settings.

### Update a limit in CapRover

1. Open **Apps → the app → App Config → Service Update Override**.
2. Merge the YAML below into any existing override, preserving unrelated settings. Replace `MemoryBytes` with the value from the table. To calculate another value, multiply MiB by `1048576`.
3. Save/update one app at a time and wait for it to become healthy before changing the next app.

Example for profile-service (384 MiB):

```yaml
TaskTemplate:
  Resources:
    Limits:
      MemoryBytes: 402653184
UpdateConfig:
  Parallelism: 1
  Order: stop-first
RollbackConfig:
  Parallelism: 1
  Order: stop-first
```

The update and rollback settings stop the old replica before starting its replacement, avoiding overlapping replicas of that service at the cost of brief downtime. They do not serialize updates across different apps. Memory limits were verified from the live snapshot; update/rollback order must be checked separately. Keep the override in CapRover so it is applied on future deployments. Removing an override does not necessarily reset a Docker setting; explicitly update the value when changing it.

The five application images use Java 25 and have no fixed heap options in their repository image configurations. A container limit covers the entire process, including heap and native memory; adding `JAVA_TOOL_OPTIONS` is not required just to enable container awareness. Existing deployment heap overrides can change this behavior. Kafka's recommended companion setting for the Bitnami image is `KAFKA_HEAP_OPTS=-Xms128m -Xmx256m`, entered under **Environment Variables**, separately from the YAML. Its presence was not verified by the memory snapshot; check it before relying on the 512 MiB limit. The repository uses Apicurio for Schema Registry, so do not assume Confluent-specific heap variables apply.

### Verify after updating

Run these commands on the VM:

```bash
free -h
docker service ls
docker stats --no-stream
```

Obtain the actual service name from `docker service ls`, then inspect its configuration and task history (profile-service example):

```bash
docker service inspect srv-captain--profile-service --pretty
docker service ps srv-captain--profile-service --no-trunc
```

Exercise login, profile creation, matching, acceptance, and date creation, then check memory and task history again. Old shutdown tasks from configuration updates are expected; repeated new failures need investigation. In the verified snapshot, Schema Registry used 221/256 MiB, profile-service 315/384 MiB, and date-service 208/256 MiB, so watch these particularly closely. Do not infer sustained stability from an idle snapshot or raise limits without checking total host headroom. Swap and Kafka heap settings were not verified by that snapshot.

References: [CapRover Service Update Override](https://caprover.com/docs/service-update-override.html), [Docker service update](https://docs.docker.com/reference/cli/docker/service/update/), and [Keycloak container memory settings](https://www.keycloak.org/server/containers).

## Configure AI skills
We use https://skills.sh to configure the AI skills for this project.
To install new skills, see https://skills.sh/docs/cli

## OpenSpec AI
We use OpenSpec to work according to work spec driven. This to keep our documentation in sync with our code.
https://openspec.dev/#

## Git workflow traceability
See [GIT_TRACEABILITY.md](GIT_TRACEABILITY.md) for the required branch naming, commit message, and pull request rules that link work back to GitHub issues and the GitHub Project.
