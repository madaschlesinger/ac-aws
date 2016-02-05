#!/bin/bash

echo "Setting environment variable to login into AWS Account"

export AWS_ACCESS_KEY_ID="___YOUR_AWS_ACCESS_KEY___"
export AWS_SECRET_ACCESS_KEY="___YOUR_AWS_SECRET_KEY___"
export PATH=$PATH:$PWD
export EC2_INI_PATH=ec2.ini
export EC2_INI_PATH=$PWD/ec2.ini
export ANSIBLE_HOSTS=$PWD/ec2.py

echo "Setting authentication key into the agent to allow to authenticate with: ssh ec2-user@aws-server-external-ip"

eval "ssh-add /home/${USER}/.ssh/${USER}.pem"

echo "Agent is loaded with this key: `ssh-add -L`"
echo "If you don't see any keys listed above you may want to manually execute: ssh-agent bash"
