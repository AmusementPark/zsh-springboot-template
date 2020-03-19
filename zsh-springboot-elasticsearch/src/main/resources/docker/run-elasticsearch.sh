docker network create es_network

docker run -d --name elasticsearch --network es_network \
-p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms512m -Xms512m" elasticsearch:6.8.2 \
-v /mine-folder/elasticsearch/data:/usr/share/elasticsearch/data \
elasticsearch:6.8.2


docker run -d --name elasticsearch --network es_network \
-p 6200:9200 -p 6300:9300 \
-e "discovery.type=single-node" \
172.22.122.21:5000/elasticsearch-6.7.0
