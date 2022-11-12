FROM openjdk:8-jre-alpine
ENV ARGS="localhost"
COPY target/dodgydrivers-*-jar-with-dependencies.jar /dodgydrivers-0.1.jar
CMD /usr/bin/java -jar /dodgydrivers-0.1.jar $ARGS