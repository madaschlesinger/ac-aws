FROM java:alpine

ADD target/services-s3-rest-1.0-SNAPSHOT-spring-boot.jar /data/services-s3-rest.jar
RUN sh -c 'touch /data/services-s3-rest.jar'
ENTRYPOINT ["java", "-jar", "/data/services-s3-rest.jar"]

