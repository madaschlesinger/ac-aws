[Unit]
Description=slaveConfig
After=cloud-init.service
Wants=network.target

[Service]
ExecStart=/var/lib/cloud/scripts/slave-config.sh
Restart=always
RestartSec=20

[Install]
WantedBy=multi-user.target
