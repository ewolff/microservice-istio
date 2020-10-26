#!/bin/sh
sed "s,image: ,image: gcr.io/$PROJECT_ID/,g" bonus.yaml | sed -i "" "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," > bonus-gcp.yaml
