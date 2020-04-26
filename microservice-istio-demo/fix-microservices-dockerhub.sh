#!/bin/sh
sed -i "s,image: ,image: docker.io/ewolff/," microservices.yaml
sed -i "s,image: ,image: docker.io/ewolff/," infrastructure.yaml
sed -i "s,image: ,image: docker.io/ewolff/," failing-order-service.yaml
sed -i "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," infrastructure.yaml
sed -i "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," microservices.yaml
sed -i "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," failing-order-service.yaml
