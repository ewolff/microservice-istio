#!/bin/sh
sed "s,image: ,image: gcr.io/$PROJECT_ID/,g" microservices.yaml | sed -i "" "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," > microservices-gcp.yaml
sed "s,image: ,image: gcr.io/$PROJECT_ID/,g" infrastructure.yaml | sed -i "" "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," > infrastructure-gcp.yaml 
sed "s,image: ,image: gcr.io/$PROJECT_ID/,g" failing-order-service.yaml | sed -i "" "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," > failing-order-service-gcp.yaml
