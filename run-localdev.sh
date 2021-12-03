#!/usr/bin/env sh

# expected environment variables
REGISTRATION_SERVICE_MONGODB_URL=mongodb://user:pwd@host:port
REGISTRATION_SERVICE_MONGODB_NAME=dbname
DEVICE_REGISTRATION_API_KEY_NAME_VALUE=someKey
OAUTH2_RESOURCE_SERVER_JWT_JWK_SET_URI=http://localhost:8080/auth/realms/master/protocol/openid-connect/certs
OAUTH2_RESOURCE_SERVER_JWT_ISSUER_URI=http://localhost:8080/auth/realms/master

# start local dev with Maven
mvn clean spring-boot:run
