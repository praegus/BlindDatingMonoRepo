# Use Node.js for building
FROM timbru31/java-node:21-jdk-20 AS builder

# Set working directory
WORKDIR /app
RUN mkdir ../specs
COPY specs/ ../specs

# Copy package.json and install dependencies
COPY blinddatingapp-frontend/package.json blinddatingapp-frontend/package-lock.json ./
RUN npm install

# Copy the entire project and build
COPY blinddatingapp-frontend/ .
RUN npm run build --configuration=production

# Use Nginx for serving the built Angular app
FROM nginx:latest AS server

# Copy built Angular files to Nginx directory
COPY --from=builder /app/dist/blinddatingapp/browser /usr/share/nginx/html

# Copy custom Nginx config (optional)
COPY blinddatingapp-frontend/nginx.conf /etc/nginx/conf.d/default.conf
COPY blinddatingapp-frontend/public/runtime-config.js /usr/share/nginx/html/runtime-config.template.js

# Expose port 80 and start Nginx
EXPOSE 80
CMD ["/bin/sh", "-c", "export PROFILE_API_URL=\"${PROFILE_API_URL:-http://localhost:9080}\" WEBSOCKET_URL=\"${WEBSOCKET_URL:-http://localhost:9082/ws}\"; envsubst '${PROFILE_API_URL} ${WEBSOCKET_URL}' < /usr/share/nginx/html/runtime-config.template.js > /usr/share/nginx/html/runtime-config.js && exec nginx -g 'daemon off;'"]
