## 查看 ZooKeeper版本
echo stat|nc localhost 2181
#Zookeeper version: 3.4.14-4c25d480e66aadd371de8bd2fd8da255ac140bcf, built on 03/06/2019 16:18 GMT
#Clients:
# /0:0:0:0:0:0:0:1:58236[0](queued=0,recved=1,sent=0)
# /127.0.0.1:55276[1](queued=0,recved=21639,sent=21642)
#
#Latency min/avg/max: 0/0/33
#Received: 22108
#Sent: 22119
#Connections: 2
#Outstanding: 0
#Zxid: 0xcc
#Mode: standalone
#Node count: 144

## zookeeper客户端连接到zookeeper
./zookeeper-shell localhost:2181