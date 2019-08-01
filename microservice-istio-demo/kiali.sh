#!/bin/sh
echo Open Kiali at http://localhost:20001/
kubectl -n istio-system port-forward deployment/kiali 20001:20001
