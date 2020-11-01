FROM openjdk:8-jdk-alpine

LABEL maintainer="farshad.falaki@gmail.com"

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/checkout-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} checkout.jar

ENTRYPOINT ["java","-jar","/checkout.jar"]