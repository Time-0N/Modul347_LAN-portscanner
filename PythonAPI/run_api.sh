#!/bin/bash

# Get the directory of the script (makes it work on any machine)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Navigate to the project directory
cd "$SCRIPT_DIR" || exit 1

# Check if virtual environment exists, if not, create it
if [ ! -d ".venv" ]; then
    echo "⚙️  Virtual environment not found. Setting up .venv..."
    python3 -m venv .venv
    source .venv/bin/activate

    # Install dependencies if requirements.txt exists
    if [ -f "requirements.txt" ]; then
        echo "📦 Installing dependencies from requirements.txt..."
        pip install -r requirements.txt
    else
        echo "⚠️ No requirements.txt found. Installing Uvicorn manually..."
        pip install fastapi uvicorn
    fi

    echo "✅ Virtual environment setup complete!"
else
    # Activate the virtual environment
    source .venv/bin/activate
fi

# Find Uvicorn inside the virtual environment
UVICORN_PATH="$(which uvicorn)"
if [ -z "$UVICORN_PATH" ]; then
    echo "❌ Uvicorn not found! Installing it..."
    pip install uvicorn
fi

# Run Uvicorn with sudo
echo "🚀 Starting FastAPI server..."
sudo "$UVICORN_PATH" main:app --host 0.0.0.0 --port 8000 --reload