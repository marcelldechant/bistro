#!/bin/bash

mvn clean package
docker build -t bistro:latest .
docker run -d -p 8080:8080 --name bistro bistro:latest
