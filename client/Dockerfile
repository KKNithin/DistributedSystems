FROM openjdk:8-jre-alpine
ENV SERVER_HOST=localhost
COPY target/client-*-jar-with-dependencies.jar /client-0.0.1.jar
CMD /usr/bin/java -jar /client-0.0.1.jar $SERVER_HOST