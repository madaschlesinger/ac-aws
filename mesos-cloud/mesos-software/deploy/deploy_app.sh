#! /bin/bash

echo "Deploying application on Mesos Cluster"

ENV=$1

MASTER_IP=$(ec2.py --list | grep -A1 Mesos_Master | grep -A1 $ENV | grep -A1 $(whoami) | tail -1 | awk -F\" '{print $2}')

MASTER_ADDRESS=$(ec2.py --host ${MASTER_IP} | grep ec2_public_dns_name | awk '{print $2}' | sed 's/,//g' | sed 's/"//g')

sed 's/##EXTERNAL_EC2_IP##/'"${MASTER_ADDRESS}"'/g' ../applications/nginx_haproxy.json.template >  ../applications/nginx_haproxy.json

curl -X POST http://${MASTER_IP}:8080/v2/apps -d @../applications/nginx_haproxy.json -H "Content-type: application/json"
