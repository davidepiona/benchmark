FROM java:8-jre
MAINTAINER Davide Piona <davide.piona@studenti.unipd.it>
ARG JAR_FILE
EXPOSE 8010
EXPOSE 7010
COPY target/${JAR_FILE} /registry-microservice.jar
CMD ["java", "-Dspring.profiles.active=docker", "-Dcom.sun.management.jmxremote", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.port=7010", "-Dcom.sun.management.jmxremote.rmi.port=7010", "-Dcom.sun.management.jmxremote.local.only=false","-Djava.rmi.server.hostname=0.0.0.0" ,"-Dcom.sun.management.jmxremote.host=0.0.0.0","-jar","/registry-microservice.jar"]