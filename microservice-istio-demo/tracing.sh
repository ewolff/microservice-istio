#!/bin/sh
echo Open Jaeger at http://localhost:16686/
kubectl -n istio-system port-forward $(kubectl -n istio-system get pod -l app=jaeger -o jsonpath='{.items[0].metadata.name}') 16686:16686

