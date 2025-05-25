# Build Stage
FROM timbru31/java-node:21-jdk-20 AS build
WORKDIR /app
COPY blinddatingapp-frontend/package*.json ./
COPY specs/*.yaml ../specs/
RUN npm install
COPY blinddatingapp-frontend/ .
RUN npm run build

# Production Stage
FROM nginx:stable-alpine AS production
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]