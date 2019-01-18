#!/bin/sh
IP=$(minikube ip) 
PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')
echo http://$IP:$PORT/
