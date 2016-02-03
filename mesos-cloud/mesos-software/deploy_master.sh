#! /bin/bash

echo "Deploying a basic Mesos Cluster and create one environment"

FLAVOUR=$1

case $FLAVOUR in

plain)
	ansible-playbook -i ec2.py --extra-vars="flavour=plain" --limit "tag_Name_Mesos_Master_`echo $(whoami)`_plain" master.yml	
	;;

*)
  echo "Please select one possible values among [plain|weave|calico|consul]"
  ;;
esac

