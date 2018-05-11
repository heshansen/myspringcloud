FROM java:8

MAINTAINER Qing Wang <qing.wang@61topbaby.com>

ENV APP_HOME /app
ENV APP_JAR_NAME config
ENV APP_JAR_VERSION 0.0.1-SNAPSHOT
ENV CONFIG_HOME ${APP_HOME}/config

RUN mkdir ${APP_HOME}

VOLUME ${CONFIG_HOME}

ADD target/${APP_JAR_NAME}-${APP_JAR_VERSION}.jar ${APP_HOME}/
ADD run.sh ${APP_HOME}/

COPY ./jce  /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/

RUN chmod +x ${APP_HOME}/run.sh

WORKDIR ${APP_HOME}

EXPOSE 8080
ENTRYPOINT ["/bin/bash", "-c"]
CMD ["${APP_HOME}/run.sh"]
