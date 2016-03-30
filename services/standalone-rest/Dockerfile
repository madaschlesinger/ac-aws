FROM java:alpine

ADD target/services-standalone-rest--1.0-SNAPSHOT-spring-boot.jar /data/services-standalone-rest.jar
RUN sh -c 'touch /data/services-standalone-rest.jar'
ENTRYPOINT ["java", "-jar", "/data/services-standalone-rest.jar"]

