#!/bin/sh
echo Open Ingress at http://localhost:31380/
kubectl -n istio-system port-forward deployment/istio-ingressgateway 31380:8080

