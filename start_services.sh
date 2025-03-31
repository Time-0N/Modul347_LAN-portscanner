#!/bin/bash

# Initialize DB
./init_db.sh

# Start PostgreSQL
su postgres -c "pg_ctl start -D $PGDATA"

# Wait for PostgreSQL
until su postgres -c "pg_isready"; do sleep 1; done

# Start Python API first (Spring Boot depends on it)
echo "Starting Python API..."
cd /app/api
python3 app.py &  # Assuming your file is app.py

# Start Spring Boot (will call Python API)
echo "Starting Spring Boot..."
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/$POSTGRES_DB"
export SPRING_DATASOURCE_USERNAME="$POSTGRES_USER"
export SPRING_DATASOURCE_PASSWORD="$POSTGRES_PASSWORD"
export PYTHON_API_URL="http://localhost:8081"  # For Spring Boot to use
java -jar /app/backend/app.jar &

# Start NGINX last
nginx -g "daemon off;"