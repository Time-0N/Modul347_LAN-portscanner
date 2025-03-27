#!/bin/bash

POSTGRES_CONTAINER_NAME="dev_postgres"
POSTGRES_IMAGE="postgres:latest"
POSTGRES_USER="devuser"
POSTGRES_PASSWORD="devpass"
POSTGRES_DB="devdb"
PG_VOLUME="$HOME/docker_volumes/postgres"

mkdir -p "$PG_VOLUME"

if [ "$(docker ps -q -f name=$POSTGRES_CONTAINER_NAME)" ]; then
    echo "PostgreSQL container is already running."
    exit 0
fi

if [ "$(docker ps -aq -f name=$POSTGRES_CONTAINER_NAME)" ]; then
    echo "Removing old PostgreSQL container..."
    docker rm -f $POSTGRES_CONTAINER_NAME
fi

echo "Starting new PostgreSQL container..."
docker run -d \
    --name "$POSTGRES_CONTAINER_NAME" \
    --network host \
    -e "POSTGRES_USER=$POSTGRES_USER" \
    -e "POSTGRES_PASSWORD=$POSTGRES_PASSWORD" \
    -e "POSTGRES_DB=$POSTGRES_DB" \
    -v "$PG_VOLUME:/var/lib/postgresql/data" \
    "$POSTGRES_IMAGE"

if [ "$(docker ps -q -f name=$POSTGRES_CONTAINER_NAME)" ]; then
    echo "PostgreSQL started successfully on **host network** (port 5432)"
else
    echo "PostgreSQL failed to start."
fi

