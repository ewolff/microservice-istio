#!/bin/sh
docker tag microservice-istio-apache gcr.io/${PROJECT_ID}/microservice-istio-apache
docker push gcr.io/${PROJECT_ID}/microservice-istio-apache
docker tag microservice-istio-postgres gcr.io/${PROJECT_ID}/microservice-istio-postgres
docker push gcr.io/${PROJECT_ID}/microservice-istio-postgres
docker tag microservice-istio-shipping:1 gcr.io/${PROJECT_ID}/microservice-istio-shipping:1
docker push gcr.io/${PROJECT_ID}/microservice-istio-shipping:1
docker tag microservice-istio-invoicing:1 gcr.io/${PROJECT_ID}/microservice-istio-invoicing:1
docker push gcr.io/${PROJECT_ID}/microservice-istio-invoicing:1
docker tag microservice-istio-order:1 gcr.io/${PROJECT_ID}/microservice-istio-order:1
docker push gcr.io/${PROJECT_ID}/microservice-istio-order:1
