#!/bin/sh
echo Open Kibana at http://localhost:5601/
kubectl -n logging port-forward $(kubectl -n logging get pod -l app=kibana -o jsonpath='{.items[0].metadata.name}') 5601:5601
