FROM java:8-jre
MAINTAINER Davide Piona <davide.piona@studenti.unipd.it>
ARG JAR_FILE
EXPOSE 9999
EXPOSE 7999
COPY target/${JAR_FILE} /gateway-microservice.jar
CMD ["java", \
    "-Xmx384m", \
    "-Dspring.profiles.active=docker", \
    "-Dcom.sun.management.jmxremote", \
    "-Dcom.sun.management.jmxremote.authenticate=false", \
    "-Dcom.sun.management.jmxremote.ssl=false", \
    "-Dcom.sun.management.jmxremote.port=7999", \
    "-Dcom.sun.management.jmxremote.rmi.port=7999", \
    "-Dcom.sun.management.jmxremote.local.only=false", \
    "-Djava.rmi.server.hostname=0.0.0.0", \
    "-Dcom.sun.management.jmxremote.host=0.0.0.0", \
    "-jar","/gateway-microservice.jar"]