FROM openjdk:8-jre-alpine
ENV SERVER_HOST=localhost
COPY target/auldfellas-*-jar-with-dependencies.jar /auldfellas-0.0.1.jar
CMD /usr/bin/java -jar auldfellas-0.0.1.jar $SERVER_HOST