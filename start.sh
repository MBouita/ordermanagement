#!/bin/bash

docker-compose down -v

if [[ $# -eq 1 ]]; then
    GOOGLE_API_KEY=$1
else
    GOOGLE_API_KEY=""
fi

if [[ -n $GOOGLE_API_KEY ]]; then
    sed -i "s/^google.api.key=.*/google.api.key=$GOOGLE_API_KEY/g" ./src/main/resources/application.properties
fi

docker-compose build

docker-compose up -d

sleep 30

docker-compose logs