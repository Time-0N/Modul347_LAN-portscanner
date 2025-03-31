# Stage 1: Build
FROM node:20 as builder
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

RUN npm run build -- --configuration production --output-path=dist/frontend

# Stage 2: Serve
FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
# Copy from browser subdirectory
COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 4200
