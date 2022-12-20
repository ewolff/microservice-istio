#!/bin/sh
./mvnw clean spring-boot:build-image -Pnative
docker tag microservice-istio-bonus:0.0.1-SNAPSHOT microservice-istio-bonus-native:1
