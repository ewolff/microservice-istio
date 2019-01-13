#!/bin/sh
docker build --tag=microservice-istio-apache apache
docker build --tag=microservice-istio-postgres postgres
docker build --tag=microservice-istio-shipping:1 microservice-istio-shipping
docker build --tag=microservice-istio-invoicing:1 microservice-istio-invoicing
docker build --tag=microservice-istio-order:1 microservice-istio-order
