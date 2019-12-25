FROM java:8
MAINTAINER simon.zhu.chn@hotmail.com
WORKDIR /
# RESOURCES
ADD target/sentinel-consumer.jar application.jar
# ENVIRONMENTS
ENV JVM_OPTS="-Xmx1g -Xms1g"
# COMMAND
EXPOSE 9800
ENTRYPOINT ["java","-jar","application.jar", "${JVM_OPTS}"]