#!/bin/sh
sed "s/image: /image: docker.io\/ewolff\//g" microservices.yaml > microservices-dockerhub.yaml
sed "s/image: /image: docker.io\/ewolff\//g" infrastructure.yaml > infrastructure-dockerhub.yaml
sed "s/image: /image: docker.io\/ewolff\//g" failing-order-service.yaml > failing-order-service-dockerhub.yaml
sed -i "" "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," infrastructure-dockerhub.yaml
sed -i "" "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," microservices-dockerhub.yaml
sed -i "" "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," failing-order-service-dockerhub.yaml
