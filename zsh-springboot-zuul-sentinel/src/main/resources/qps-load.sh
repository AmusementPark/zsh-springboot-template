#!/bin/bash

while(true)
do
 curl http://localhost:9800/get/produce;
 r1=$RANDOM;
 usleep ${r1};

 curl http://localhost:9800/ano/another;
 r2=$RANDOM;
 usleep ${r2};

 curl http://localhost:9800/ano/another/v2;
 r3=$RANDOM;
 usleep ${r3};

 curl http://localhost:9800/adm/config?c=${r2};
 r4=$RANDOM;
 usleep ${r4};
done