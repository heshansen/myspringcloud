#!/usr/bin/env bash

#####################################################################
# This shell rm all of the images with none repository
#####################################################################
docker rmi $(docker images --filter "dangling=true" -q --no-trunc)