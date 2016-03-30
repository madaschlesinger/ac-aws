FROM java:alpine

ADD target/services-indexed-rest-1.0-SNAPSHOT-spring-boot.jar /data/services-indexed-rest.jar
RUN sh -c 'touch /data/services-indexed-rest.jar'
ENTRYPOINT ["java", "-jar", "/data/services-indexed-rest.jar"]
