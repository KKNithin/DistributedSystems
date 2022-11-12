FROM openjdk:8-jre-alpine
ENV ARGS="-m jmdns"
COPY target/broker-*-jar-with-dependencies.jar /broker-0.1.jar
CMD /usr/bin/java -jar /broker-0.1.jar $ARGS