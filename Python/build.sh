#!/bin/bash
set -o errexit

echo "Running migrations..."
python manage.py migrate

echo "Seeding demo data..."
python seed_data.py

echo "Collecting static files..."
python manage.py collectstatic --no-input

echo "Build complete!"
