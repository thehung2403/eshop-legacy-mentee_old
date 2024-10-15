# FROM node:16.18-alpine as build-stage
# WORKDIR /app
# #ENV VUE_APP_BASE_URL=http://<<운영 EC2 Public IP>>:80
# COPY package*.json ./
# RUN npm install
# COPY . .
# RUN npm run build

# # production stage
# FROM node:16.18-alpine as production-stage
# WORKDIR /app
# RUN npm install -g http-server
# COPY --from=build-stage /app/dist /app/dist
# EXPOSE 8080
# CMD ["http-server", "dist"]

#buildstage
FROM node:16.18-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
 
# production stage
FROM node:16.18-alpine as production-stage
WORKDIR /app
RUN npm install -g http-server
COPY --from=build-stage /app/dist /app/dist
EXPOSE 8080
CMD [ "http-server", "dist", "--proxy", "http://backend:8090" ]
