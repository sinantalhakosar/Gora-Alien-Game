#!/bin/bash
mkdir executables
cd server && mvn clean install -DskipTests && cd ..
cp ./server/target/server_program13.jar executables/
cd client && mvn clean install -DskipTests && cd ..
cp ./client/target/client_program13.jar executables/
cd socketserver && mvn clean install -DskipTests && cd ..
cp ./socketserver/target/socketserver_program13.jar executables/