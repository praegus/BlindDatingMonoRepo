cd ./date-service
CALL mvn clean compile jib:build
cd ../matching-service
CALL mvn clean compile jib:build
cd ../profile-service
CALL mvn clean compile jib:build
cd ../websocket-service
CALL mvn clean compile jib:build
cd ..
CALL docker build -f blinddatingapp-frontend/Dockerfile -t docker.io/finkingma/blinddatingapp-frontend:latest .
CALL docker push docker.io/finkingma/blinddatingapp-frontend:latest