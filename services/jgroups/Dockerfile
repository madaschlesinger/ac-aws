FROM java:alpine

ADD target/services-jgroups-rest-1.0-SNAPSHOT-spring-boot.jar /data/services-jgroups-rest.jar
RUN sh -c 'touch /data/services-jgroups-rest.jar'
ENTRYPOINT ["java", "-jar", "/data/services-jgroups-rest.jar"]

