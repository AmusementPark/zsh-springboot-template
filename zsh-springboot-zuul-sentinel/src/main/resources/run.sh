#dashboard
nohup java -Dserver.port=9600 -Dcsp.sentinel.dashboard.server=localhost:9600 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.7.0.jar > /dev/null 2>&1 &
nohup java -jar sentinel-consumer.jar > /dev/null 2>&1 &
nohup java -jar sentinel-producer.jar > /dev/null 2>&1 &
#load-mock
nohup ./qps-load.sh > /dev/null 2>&1 &