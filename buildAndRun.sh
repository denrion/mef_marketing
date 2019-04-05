#!/bin/sh
mvn clean package && docker build -t com.github.denrion.mef_marketing/mef_marketing .
docker rm -f mef_marketing || true && docker run -d -p 8080:8080 -p 4848:4848 --name mef_marketing com.github.denrion.mef_marketing/mef_marketing 
