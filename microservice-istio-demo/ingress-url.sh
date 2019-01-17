#!/bin/sh
echo ip adress
minikube ip -p istio2
echo port for ingress
kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}'
echo
