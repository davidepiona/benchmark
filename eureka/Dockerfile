FROM java:8-jre
MAINTAINER Davide Piona <davide.piona@studenti.unipd.it>
ARG JAR_FILE
EXPOSE 8761
COPY target/${JAR_FILE} /eureka-microservice.jar
CMD ["java", "-Dspring.profiles.active=docker", "-jar","/eureka-microservice.jar"]