#!/bin/bash
for i in `seq 1 1000`;
do
    curl -s -o /dev/null -I -w "%{http_code}" $1
    echo
done
