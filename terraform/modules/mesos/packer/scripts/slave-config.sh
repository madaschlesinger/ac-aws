#!/bin/bash

EXTERNAL_IP=`curl http://169.254.169.254/latest/meta-data/public-ipv4`

sed -i 's/MASTER/$MASTER/g' /etc/mesos/zk
sed -i 's/MASTER/$MASTER/g' /usr/lib/systemd/system/weave.service
sed -i 's/MASTER/$MASTER/g' /usr/lib/systemd/system/consul-slave.service


sed -i 's/HOSTNAME/$EXTERNAL_IP/g' /etc/mesos-slave/hostname
sed -i 's/HOSTNAME/$HOSTNAME/g' /etc/mesos-slave/ip
