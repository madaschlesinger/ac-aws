FROM java:alpine

ADD target/services-dependent-rest-1.0-SNAPSHOT-spring-boot.jar /data/services-dependent-rest.jar
RUN sh -c 'touch /data/services-dependent-rest.jar'
ENTRYPOINT ["java", "-jar", "/data/services-dependent-rest.jar"]
