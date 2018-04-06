FROM java:8-jre
MAINTAINER Davide Piona <davide.piona@studenti.unipd.it>
ARG JAR_FILE
EXPOSE 8010
COPY target/${JAR_FILE} /registry-microservice.jar
CMD ["java", "-Dspring.profiles.active=docker", "-jar","/registry-microservice.jar"]