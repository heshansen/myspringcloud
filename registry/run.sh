#!/usr/bin/env bash

java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=${PROFILE_ACTIVE} -jar ${APP_JAR_NAME}-${APP_JAR_VERSION}.jar
