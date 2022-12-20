#!/bin/sh
mvn clean spring-boot:build-image --file pom-native.xml
docker tag microservice-istio-bonus:0.0.1-SNAPSHOT microservice-istio-bonus-native:1
