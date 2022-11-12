FROM openjdk:8-jre-alpine
ENV SERVER_HOST=localhost
COPY target/doggydrivers-*-jar-with-dependencies.jar /doggydrivers-0.0.1.jar
CMD /usr/bin/java -jar /doggydrivers-0.0.1.jar $SERVER_HOST