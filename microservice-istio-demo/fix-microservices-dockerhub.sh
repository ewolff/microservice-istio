#!/bin/sh
sed -i "s,image: ,image: docker.pkg.github.com/ewolff/microservice-istio/," microservices.yaml
sed -i "s,image: ,image: docker.pkg.github.com/ewolff/microservice-istio/," infrastructure.yaml
sed -i "s,image: ,image: docker.pkg.github.com/ewolff/microservice-istio/," failing-order-service.yaml
sed -i "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," infrastructure.yaml
sed -i "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," microservices.yaml
sed -i "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," failing-order-service.yaml
