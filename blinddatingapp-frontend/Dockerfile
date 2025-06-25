# Use Node.js for building
FROM timbru31/java-node:21-jdk-20 AS builder

# Set working directory
WORKDIR /app
RUN mkdir ../specs
COPY specs/ ../specs

# Copy package.json and install dependencies
COPY blinddatingapp-frontend-angular/package.json blinddatingapp-frontend-angular/package-lock.json ./
RUN npm install

# Copy the entire project and build
COPY blinddatingapp-frontend-angular/ .
RUN npm run build --configuration=production

# Use Nginx for serving the built Angular app
FROM nginx:latest AS server

# Copy built Angular files to Nginx directory
COPY --from=builder /app/dist/blinddatingapp/browser /usr/share/nginx/html

# Copy custom Nginx config (optional)
COPY blinddatingapp-frontend-angular/nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80 and start Nginx
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]