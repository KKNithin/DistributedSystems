FROM openjdk:8-jre-alpine
ENV ARGS="localhost"
COPY target/broker-*-jar-with-dependencies.jar /broker-0.1.jar
CMD /usr/bin/java -jar /broker-0.1.jar $ARGS