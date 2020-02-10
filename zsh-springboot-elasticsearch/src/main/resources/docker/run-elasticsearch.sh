docker run -d --name elasticsearch --network es_network \
-p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-v /mine-folder/elasticsearch/data:/usr/share/elasticsearch/data \
elasticsearch:6.7.0
