#! /bin/bash

echo "Deploying application on Mesos Cluster"

curl -X POST http://54.229.91.188:8080/v2/apps -d @../applications/simple_docker_app.json -H "Content-type: application/json"
