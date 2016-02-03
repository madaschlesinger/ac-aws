#! /bin/bash

echo "Deploying application on Mesos Cluster"

MASTER_IP=$(ec2.py --list | grep -A1 Mesos_Master | grep -A1 $(whoami) | tail -1 | awk -F\" '{print $2}')
curl -X POST http://${MASTER_IP}:8080/v2/apps -d @../applications/simple_docker_app.json -H "Content-type: application/json"
