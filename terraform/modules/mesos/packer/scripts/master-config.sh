#cloud-boothook
#!/bin/bash

EXTERNAL_IP=`curl http://169.254.169.254/latest/meta-data/public-ipv4`

sed -i 's/EXTERNAL_IP/'"${EXTERNAL_IP}"'/g' /etc/marathon/conf/hostname
sed -i 's/HOSTNAME/'"${HOSTNAME}"'/g' /usr/lib/systemd/system/consul.service
sed -i 's/HOSTNAME/'"${HOSTNAME}"'/g' /etc/zookeeper/conf/zoo.cfg
