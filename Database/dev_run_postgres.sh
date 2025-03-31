#!/bin/bash

# Setze Pfad zur .env eine Ebene höher
ENV_PATH="$(dirname "$0")/../.env"

# Prüfe ob .env existiert
if [ ! -f "$ENV_PATH" ]; then
  echo "❌ .env file not found at $ENV_PATH"
  exit 1
fi

# Lade Variablen aus .env
set -a
source "$ENV_PATH"
set +a

# Erstelle Volume-Verzeichnis
mkdir -p "$PG_VOLUME"

# Check ob Container läuft
if [ "$(docker ps -q -f name=$POSTGRES_CONTAINER_NAME)" ]; then
    echo "⏳ PostgreSQL container is already running."
    exit 0
fi

# Entferne alten Container falls vorhanden
if [ "$(docker ps -aq -f name=$POSTGRES_CONTAINER_NAME)" ]; then
    echo "🗑️ Removing old PostgreSQL container..."
    docker rm -f "$POSTGRES_CONTAINER_NAME"
fi

# Starte neuen PostgreSQL-Container
echo "🚀 Starting new PostgreSQL container..."
docker run -d \
    --name "$POSTGRES_CONTAINER_NAME" \
    -p 5432:5432 \
    -e "POSTGRES_USER=$POSTGRES_USER" \
    -e "POSTGRES_PASSWORD=$POSTGRES_PASSWORD" \
    -e "POSTGRES_DB=$POSTGRES_DB" \
    -v "$PG_VOLUME:/var/lib/postgresql/data" \
    "$POSTGRES_IMAGE"

# Statusmeldung
if [ "$(docker ps -q -f name=$POSTGRES_CONTAINER_NAME)" ]; then
    echo "✅ PostgreSQL started successfully on host network (port 5432)"
else
    echo "❌ PostgreSQL failed to start."
fi
