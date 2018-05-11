FROM java:8

MAINTAINER <Qing Wang>qing.wang@61topbaby.com

VOLUME /tmp

ENV USER_NAME registry
ENV APP_HOME /home/$USER_NAME/app

RUN useradd -ms /bin/bash $USER_NAME
RUN mkdir $APP_HOME

ADD target/turbine-0.0.1-SNAPSHOT.jar $APP_HOME/turbine-0.0.1-SNAPSHOT.jar
COPY ./jce  /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/

RUN chown $USER_NAME $APP_HOME/turbine-0.0.1-SNAPSHOT.jar

USER $USER_NAME

WORKDIR $APP_HOME

RUN bash -c 'touch turbine-0.0.1-SNAPSHOT.jar'

EXPOSE 8084
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","turbine-0.0.1-SNAPSHOT.jar"]
