#!/bin/bash

sed 's/EXTERNAL_IP/'"${EXTERNAL_IP}"'/g' /etc/marathon/conf/hostname

