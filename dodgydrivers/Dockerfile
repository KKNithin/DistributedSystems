FROM openjdk:8-jre-alpine
ENV ARGS="-m jmdns"
COPY target/dodgydrivers-*-jar-with-dependencies.jar /dodgydrivers-0.1.jar
CMD /usr/bin/java -jar /dodgydrivers-0.1.jar $ARGS