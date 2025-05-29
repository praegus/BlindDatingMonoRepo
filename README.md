# Blind Dating App MonoRepo

## Starting the System

### 1. Start docker registry
Run the command `docker compose up registry` which will start the registry.

### 2. Create initial application docker images
Run `createDockerImages.bat` or `createDockerImages.sh` to build all the applications and create docker images for each in our local repository
Attempt the docker compose up once more, which should now be able to start all.
We use [JIB](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin) in our springboot applications to create docker images by running `mvn compile jib:build`.

### 3. Start all
Now run the command `docker compose up` for the rest of the services.


## Run application locally
springboot application can be run locally by executing the Application class (i.e. MatchingServiceApplication)
If you want to make changes to one of the applications you can stop it in the docker compose setup and run it locally.
Be aware that some dockerfiles depend on other services as well (matching service and dating service depend on the profile service). 
So if you want to work on the profile service, you need to either run those services locally as well, or reconfigure those docker images to connect to your localhost using something like this:
https://forums.docker.com/t/how-to-reach-localhost-on-host-from-docker-container/113321

## Update application
Once you are satisfied with the changes of an application, you can update the docker image by running the command:
`mvn compile jib:build` from within the module.
After that, you can update the docker image in the compose setup using `docker compose pull`
Finally you need to start the service again with the pulled docker file, using `docker compose up -d --force-recreate date` (for the date service)