#!/bin/bash

# Navigate to the project directory
cd /home/timeon/IdeaProjects/Modul347_LAN-portscanner/PythonAPI

# Activate the virtual environment
source .venv/bin/activate

# Run Uvicorn with sudo
sudo $(which uvicorn) main:app --host 0.0.0.0 --port 8000 --reload

