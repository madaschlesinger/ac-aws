FROM java:alpine

ADD target/services-sqs-server-1.0-SNAPSHOT-spring-boot.jar /data/services-sqs-server.jar
RUN sh -c 'touch /data/services-sqs-server.jar'
ENTRYPOINT ["java", "-jar", "/data/services-sqs-server.jar"]

