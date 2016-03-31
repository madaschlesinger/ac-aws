FROM java:alpine

ADD target/services-zeromq-server-1.0-SNAPSHOT-spring-boot.jar /data/services-zeromq-server.jar
RUN sh -c 'touch /data/services-zeromq-server.jar'
ENTRYPOINT ["java", "-jar", "/data/services-zeromq-server.jar"]
