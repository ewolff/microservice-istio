#!/bin/sh
mvn clean spring-boot:build-image
docker tag microservice-istio-bonus:0.0.1-SNAPSHOT microservice-istio-bonus-native:1
