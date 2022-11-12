FROM openjdk:8-jre-alpine
ENV SERVER_HOST=localhost
COPY target/broker-*-jar-with-dependencies.jar /broker-0.0.1.jar
CMD /usr/bin/java -jar /broker-0.0.1.jar $SERVER_HOST