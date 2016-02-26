#! /bin/bash
echo "Provisioning AWS infrastructure and building VPC, Security Groups and Hosts"

ENV=$1

ansible-playbook -i ec2.py -vvvv --extra-vars="flavour="$ENV"" playbooks/aws-infrastructure.yml
