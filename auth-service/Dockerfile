FROM java:8

MAINTAINER Qing Wang <qing.wang@61topbaby.com>

ENV APP_HOME /app
ENV APP_JAR_NAME auth-service
ENV APP_JAR_VERSION 0.0.1-SNAPSHOT

RUN mkdir ${APP_HOME}

ADD target/${APP_JAR_NAME}-${APP_JAR_VERSION}.jar ${APP_HOME}/
ADD run.sh ${APP_HOME}/

COPY ./jce  /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/

RUN chmod +x ${APP_HOME}/run.sh

WORKDIR ${APP_HOME}

EXPOSE 8083
ENTRYPOINT ["/bin/bash", "-c"]
CMD ["${APP_HOME}/run.sh"]
