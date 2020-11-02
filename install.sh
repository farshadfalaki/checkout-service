#!/bin/bash
echo '******Building the entire project and run the unit tests******'
mvn clean package

echo '******Building docker files******'
echo '**User should have permission to run docker**'
sudo docker build -t checkout .

echo '******Going to run network via docker-compose******'
echo '**User should have permission to run docker-compose**' 
sudo docker-compose -f docker-compose.yml up

