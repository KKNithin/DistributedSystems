FROM openjdk:8-jre-alpine
ENV ARGS="-m jmdns"
COPY target/girlpower-*-jar-with-dependencies.jar /girlpower-0.1.jar
CMD /usr/bin/java -jar /girlpower-0.1.jar $ARGS