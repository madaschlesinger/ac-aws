#!/bin/bash

ZOOKEEPER=`curl http://169.254.169.254/latest/user-data | grep ZOOKEEPER | awk -F= '{print $2}'`
IP=`ifconfig eth0 | grep inet | head -1 | awk '{print $2}'`

sed -i 's/ZOOKEEPER/'"${ZOOKEEPER}"'/g' /etc/mesos/zk
#sed -i 's/MASTER/'"${MASTER}"'/g' /usr/lib/systemd/system/weave.service
#sed -i 's/MASTER/'"${MASTER}"'/g' /usr/lib/systemd/system/consul-slave.service


sed -i 's/EXTERNAL_IP/'"${HOSTNAME}"'/g' /etc/mesos-slave/hostname
sed -i 's/HOSTNAME/'"${IP}"'/g' /etc/mesos-slave/ip

systemctl restart mesos-slave
#systemctl restart weave 
#systemctl restart consul-slave
