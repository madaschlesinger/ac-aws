#! /bin/bash

echo "Deploying a single host zookeeper"

ansible-playbook -i $ANSIBLE_HOSTS --limit "tag_Name_Zookeeper" playbooks/zoo-setup.yml
