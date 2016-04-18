[Unit]
Description=MasterConfig
After=network.target
Wants=network.target
Before=zookeeper.service marathon.service consul.service

[Service]
ExecStart=/var/lib/cloud/scripts/per-boot/master-config.sh
Restart=always
RestartSec=20

[Install]
WantedBy=multi-user.target
