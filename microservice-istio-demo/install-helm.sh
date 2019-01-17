#!/bin/sh
helm install --set name=order ../spring-boot-microservice/
helm install --set name=invoicing ../spring-boot-microservice/
helm install --set name=shipping ../spring-boot-microservice/
