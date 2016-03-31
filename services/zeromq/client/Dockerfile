FROM java:alpine

ADD target/services-zeromq-client-1.0-SNAPSHOT-spring-boot.jar /data/services-zeromq-client.jar
RUN sh -c 'touch /data/services-zeromq-client.jar'
ENTRYPOINT ["java", "-jar", "/data/services-zeromq-client.jar"]

