#! /bin/bash

echo "Deploying a basice Mesos Slave"

FLAVOUR=$1

case $FLAVOUR in

plain)
	ansible-playbook -i ec2.py --extra-vars="flavour=plain hosts=tag_Name_Mesos_Slave_`echo $(whoami)`_plain master=tag_Name_Mesos_Master_`echo $(whoami)`_plain" slave.yml	
	;;

*)
  echo "Please select one possible values among [plain|weave|calico|consul]"
  ;;
esac

