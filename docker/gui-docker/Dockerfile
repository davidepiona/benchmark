FROM alpine:latest
MAINTAINER Davide Piona <davide.piona@studenti.unipd.it>
RUN apk add --no-cache nodejs && apk add --no-cache git
RUN npm i -g http-server
EXPOSE 8080
COPY build /home
WORKDIR /home
ENTRYPOINT ["http-server"]