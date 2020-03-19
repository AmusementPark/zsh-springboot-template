#查看所有的index
curl 'localhost:9200/_cat/indices?v'
curl 'localhost:9200/_cat/segments/test-glpie-translator-topic?v'
# 检查节点占用内存
curl 'localhost:9200/_cat/nodes?v&h=name,port,sm'
curl 'localhost:9200/_cat/segments/e3u-service-logs-topic?v&h=index,shard,segment,size,size.memory'