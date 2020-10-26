#!/bin/sh
sed "s,image: ,image: docker.io/ewolff/,g" bonus.yaml | sed "s,imagePullPolicy: Never,imagePullPolicy: IfNotPresent," > bonus-dockerhub.yaml
