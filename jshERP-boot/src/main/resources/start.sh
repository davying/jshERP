#!/bin/bash

# get service dir, name and log
#rename -v ksllt-homecaresupervise-1.0.0 ksllt-homecaresupervise-stage-1.0.0 ksllt-homecaresupervise-1.0.0.jar
APP_HOME=$(dirname $0)
echo $APP_HOME
FULL_JAR_NAME=`ls $APP_HOME/*.jar`
JAR_NAME=$(basename $FULL_JAR_NAME)

echo ${JAR_NAME}

SERVICE_NAME=`echo $JAR_NAME|sed  's/.jar//'`
LOG_DIR=$APP_HOME/logs
PIDFILE=$APP_HOME/$SERVICE_NAME.pid
MEMORY_OPTS="-Xms1G -Xmx2G -Xmn1G "
SPRING_OPTS="--spring.profiles.active=prod"
# get $SERVICE_NAME java process pid
PID=`ps -ef|grep $JAR_NAME|grep java|grep -v grep|awk '{print $2}'`

start(){
  if [ ! $PID ]; then
    #log $SERVICE_NAME.log
    echo $SERVICE_NAME " starting"
    nohup java $MEMORY_OPTS -jar $FULL_JAR_NAME $SPRING_OPTS >> nohup.out 2>&1  &
    #java $MEMORY_OPTS -jar $FULL_JAR_NAME $SPRING_OPTS &	    
    # put pid to PIDFILE
    #sleep 60
    #tail -1f nohup.out
  else
    echo $SERVICE_NAME " is running"
  fi
}

stop(){
  if [ $PID ]; then
    kill -9 $PID
    echo $SERVICE_NAME " closed"
    PID=`ps -ef|grep $JAR_NAME|grep java|grep -v grep|awk '{print $2}'`
  else  
    echo $SERVICE_NAME " not start"
  fi
}


status(){
  if [ $PID ]; then
    echo $SERVICE_NAME " pid is " $PID
  else   
    exit 1
  fi
}

case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  restart)
    stop
    start
    ;;
  status)
    status
    ;;
  *)
    echo "Usage: $0 {start|stop|restart|status}"
    ;;
esac
exit 0

