FROM openjdk:8-jre-alpine
ENV ARGS="-h localhost -p 9000"
COPY target/client-*-jar-with-dependencies.jar /client-0.1.jar
CMD /usr/bin/java -jar /client-0.1.jar $ARGS