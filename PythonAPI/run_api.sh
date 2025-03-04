#!/bin/bash

# Get the directory of the script (makes it work on any machine)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Navigate to the project directory
cd "$SCRIPT_DIR" || exit 1

# Check if virtual environment exists
if [ -d ".venv" ]; then
    source .venv/bin/activate
else
    echo "❌ Virtual environment (.venv) not found!"
    exit 1
fi

# Find Uvicorn inside the virtual environment
UVICORN_PATH="$(which uvicorn)"
if [ -z "$UVICORN_PATH" ]; then
    echo "❌ Uvicorn not found! Make sure it's installed in your virtual environment."
    exit 1
fi

# Run Uvicorn with sudo
echo "🚀 Starting FastAPI server..."
sudo "$UVICORN_PATH" main:app --host 0.0.0.0 --port 8000 --reload
