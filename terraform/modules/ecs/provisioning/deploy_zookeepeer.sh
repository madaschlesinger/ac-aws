#! /bin/bash

echo "Deploying a single node Zookeepeer"

ansible-playbook -i ec2.py --limit "tag_Name_Zookeeper" master.yml	
