#!/bin/bash

cd config; mvn clean package -Dmaven.test.skip=true; cd ..
cd registry; mvn clean package -Dmaven.test.skip=true; cd ..
cd gateway; mvn clean package -Dmaven.test.skip=true; cd  ..
cd auth-service; mvn clean package -Dmaven.test.skip=true; cd  ..