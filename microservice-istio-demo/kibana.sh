#!/bin/sh
echo Open Kibana at http://localhost:5601/
kubectl -n logging port-forward deployment/kibana 5601:5601
