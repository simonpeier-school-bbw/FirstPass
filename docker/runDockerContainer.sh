#!/bin/bash

NAME="firstpass"
docker pull postgres:latest
docker stop $NAME
docker rm $NAME

docker run \
        --detach \
        --name $NAME \
        --publish 5432:5432 \
        --env POSTGRES_PASSWORD=root\
        --env POSTGRES_DB=firstpass \
        --env POSTGRES_USER=admin \
        --volume $(pwd)/database:/var/lib/postgresql/data \
postgres:latest