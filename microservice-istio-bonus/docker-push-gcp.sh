#!/bin/sh
docker tag microservice-istio-demo gcr.io/${PROJECT_ID}/microservice-istio-demo
docker push gcr.io/${PROJECT_ID}/microservice-istio-demo
