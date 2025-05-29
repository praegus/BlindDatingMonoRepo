cd ./date-service
CALL mvn clean compile jib:build
cd ../matching-service
CALL mvn clean compile jib:build
cd ../profile-service
CALL mvn clean compile jib:build
cd ../websocket-service
CALL mvn clean compile jib:build
cd ../blinddatingapp-frontend
CALL docker build -f blinddatingapp-frontend/Dockerfile -t localhost:5000/blinddating/blinddatingapp-frontend .
CALL docker push localhost:5000/blinddating/blinddatingapp-frontend