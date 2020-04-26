#!/bin/sh
sed "s,image: ,image: gcr.io/$PROJECT_ID/," microservices.yaml > microservices-gcp.yaml
sed "s,image: ,image: gcr.io/$PROJECT_ID/," infrastructure.yaml > infrastructure-gcp.yaml 
sed "s,image: ,image: gcr.io/$PROJECT_ID/," failing-order-service.yaml > failing-order-service-gcp.yaml
sed -i "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," infrastructure-gcp.yaml
sed -i "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," microservices-gcp.yaml
sed -i "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," failing-order-service-gcp.yaml
