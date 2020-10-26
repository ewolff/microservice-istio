#!/bin/sh
echo Open Jaeger at http://localhost:16686/
kubectl -n istio-system port-forward deployment/jaeger 16686:16686
