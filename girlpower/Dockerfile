FROM openjdk:8-jre-alpine
ENV SERVER_HOST=localhost
COPY target/girlpower-*-jar-with-dependencies.jar /girlpower-0.0.1.jar
CMD /usr/bin/java -jar /girlpower-0.0.1.jar $SERVER_HOST