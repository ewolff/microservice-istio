#!/bin/sh
helm install order --set name=order ../spring-boot-microservice/
helm install invoicing --set name=invoicing ../spring-boot-microservice/
helm install shipping --set name=shipping ../spring-boot-microservice/
