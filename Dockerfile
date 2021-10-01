FROM openjdk:11.0-jre
RUN DEBIAN_FRONTEND="noninteractive" apt-get -y install tzdata
# ENV TZ=America/Los_Angeles
# RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN adduser --system --group spring

USER spring:spring

COPY --chown=spring:spring target/clock-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
