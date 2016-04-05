FROM java:alpine

ADD target/services-sqs-client-1.0-SNAPSHOT-spring-boot.jar /data/services-sqs-client.jar
RUN sh -c 'touch /data/services-sqs-client.jar'
ENTRYPOINT ["java", "-jar", "/data/services-sqs-client.jar"]

