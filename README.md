# Blind Dating App MonoRepo

## Starting the System

### 1. Start all
Now run the command `docker compose up` for the rest of the services.

### 2. Opening the application
- Navigate to http://localhost:3000 to open the frontend
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