#!/bin/bash

# Initialize PostgreSQL if not already initialized
if [ ! -f "$PGDATA/PG_VERSION" ]; then
    echo "Initializing PostgreSQL database..."
    su postgres -c "initdb --username=postgres --pwfile=<(echo \"$POSTGRES_PASSWORD\")"

    # Start PostgreSQL temporarily to create user and database
    su postgres -c "pg_ctl start -D $PGDATA"

    # Create user and database
    su postgres -c "psql -c \"CREATE USER $POSTGRES_USER WITH PASSWORD '$POSTGRES_PASSWORD';\""
    su postgres -c "psql -c \"CREATE DATABASE $POSTGRES_DB WITH OWNER $POSTGRES_USER;\""

    # Stop PostgreSQL
    su postgres -c "pg_ctl stop -D $PGDATA"
fi