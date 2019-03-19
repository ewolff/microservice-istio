#!/bin/sh
sed -i "s,image: ,image: gcr.io/$PROJECT_ID/," microservices.yaml
sed -i "s,image: ,image: gcr.io/$PROJECT_ID/," infrastructure.yaml
sed -i "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," infrastructure.yaml
sed -i "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," microservices.yaml
sed -i "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," failing-order-service.yaml
