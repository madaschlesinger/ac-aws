# Amazon Linux upstart file at /etc/init/zookeeper.conf
#!/bin/sh

#
# chkconfig: 35 99 99
# description: Init Script for Zookeeper
#

. /etc/rc.d/init.d/functions

USER="root"

do_start()
{
                echo -n "Starting Zookeeper "
                exec /usr/lib/zookeeper/bin/zkServer.sh start-foreground
                RETVAL=$?
                echo
                [ $RETVAL -eq 0 ]
}
do_stop()
{
        pid=`ps -aefw | grep "zookeeper" | grep -v " grep " | awk '{print $2}'`
        kill -9 $pid > /dev/null 2>&1 && echo_success || echo_failure
        RETVAL=$?
        echo
        [ $RETVAL -eq 0 ]
}

do_status()
{
	number_instances=`ps -aefw | grep "zookeeper" | grep -v " grep " | wc -l`
	if [ $number_instances -gt 0 ]
	then
		echo "Service Zookeeeper Running"
	elif [ $number_instances -eq 0 ]
	then
		echo "Zookeeper not running"
	else
		echo "Unknown"
	fi
}

case "$1" in
        start)
                do_start
                ;;
        stop)
                do_stop
                ;;
        restart)
                do_stop
                do_start
                ;;
        *)
                echo "Usage: $0 {start|stop|restart}"
                RETVAL=1
esac

exit $RETVAL
