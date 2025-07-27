#!/bin/bash

set -e

CSV_FOLDER="$HOME/Desktop/csv"

if [ ! -d "$CSV_FOLDER" ]; then
  echo "CSV-Verzeichnis nicht gefunden: $CSV_FOLDER"
  exit 1
fi

docker stop bistro >/dev/null 2>&1 || true
docker rm bistro >/dev/null 2>&1 || true

mvn clean package
docker build -t bistro:latest .

echo "Mounting: $CSV_FOLDER -> /data/input"

docker run -d \
  -p 8080:8080 \
  --name bistro \
  -v "$CSV_FOLDER:/data/input" \
  -e CSV_INPUT_DIR=/data/input \
  bistro:latest
