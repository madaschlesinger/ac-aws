#!/bin/bash

EXTERNAL_IP=`curl http://169.254.169.254/latest/meta-data/public-ipv4`
ZOOKEEPER=`curl http://169.254.169.254/latest/user-data | grep ZOOKEEPER | awk -F= '{print $2}'`

sed -i 's/EXTERNAL_IP/'"${EXTERNAL_IP}"'/g' /etc/marathon/conf/hostname
##sed -i 's/HOSTNAME/'"${HOSTNAME}"'/g' /usr/lib/systemd/system/consul.service
##sed -i 's/HOSTNAME/'"${HOSTNAME}"'/g' /etc/zookeeper/conf/zoo.cfg
sed -i 's/ZOOKEEPER/'"${ZOOKEEPER}"'/g' /etc/mesos/zk
systemctl restart marathon
