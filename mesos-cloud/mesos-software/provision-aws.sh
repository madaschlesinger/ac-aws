#! /bin/bash

echo "Provisioning AWS infrastructure and building VPC, Security Groups and Hosts"

FLAVOUR=$1

case $FLAVOUR in

plain)
	ansible-playbook -i ec2.py --extra-vars="flavour=plain" aws-infrastructure.yml
	;;

*)
  echo "Please select one possible values among [plain|weave|calico|consul]"
  ;;
esac
