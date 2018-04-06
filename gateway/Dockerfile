FROM java:8-jre
MAINTAINER Davide Piona <davide.piona@studenti.unipd.it>
ARG JAR_FILE
EXPOSE 9999
COPY target/${JAR_FILE} /gateway-microservice.jar
CMD ["java", "-Dspring.profiles.active=docker", "-jar","/gateway-microservice.jar"]