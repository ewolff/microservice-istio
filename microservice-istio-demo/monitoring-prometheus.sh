#!/bin/sh
echo Open Prometheus at http://localhost:9090/
kubectl -n istio-system port-forward deployment/prometheus 9090:9090
