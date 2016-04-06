#!/bin/bash

# Amazon Linux upstart file at /etc/init/zookeeper.conf
#!/bin/sh

#
# chkconfig: 35 89 99
# description: Init Script for Zookeeper
#

. /etc/rc.d/init.d/functions

USER="root"

do_start()
{
		echo "Configuring Zookeeper listening address for a single node"
                sed -i 's/HOSTNAME/'"${HOSTNAME}"'/g' /etc/zookeeper/conf/zoo.cfg
                RETVAL=$?
                echo
                [ $RETVAL -eq 0 ]
}
do_stop()
{
        echo "Nothing to do"
        RETVAL=$?
        echo
        [ $RETVAL -eq 0 ]
}

case "$1" in
        start)
                do_start
                ;;
        stop)
                do_stop
                ;;
        *)
                echo "Usage: $0 {start|stop}"
                RETVAL=1
esac

exit $RETVAL
