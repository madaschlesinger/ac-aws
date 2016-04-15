#! /bin/bash

echo "Deploying a basice Mesos Slave"

ENV=$1

ansible-playbook -i ec2.py --extra-vars="flavour="$ENV" hosts=tag_Name_Mesos_Slave_`echo $(whoami)`_"$ENV" master=tag_Name_Mesos_Master_`echo $(whoami)`_"$ENV"" playbooks/slave.yml	
