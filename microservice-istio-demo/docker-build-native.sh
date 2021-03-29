#!/bin/sh
docker build --tag=microservice-istio-apache apache
docker build --tag=microservice-istio-postgres postgres
mvn clean spring-boot:build-image
docker tag microservice-istio-order:0.0.1-SNAPSHOT microservice-istio-order-native:1
docker tag microservice-istio-shipping:0.0.1-SNAPSHOT microservice-istio-shipping-native:1
docker tag microservice-istio-invoicing:0.0.1-SNAPSHOT microservice-istio-invoicing-native:1
