#! /bin/bash

echo "Deploying a basic Mesos Cluster and create one environment"

ENV=$1

ansible-playbook -i ec2.py --extra-vars="flavour="$ENV"" --limit "tag_Name_Mesos_Master_`echo $(whoami)`_"$ENV"" playbooks/master.yml	
