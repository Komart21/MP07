#!/bin/bash


echo "Limpiando el proyecto..."
mvn clean


echo "Compilando el proyecto..."
mvn compile


echo "Ejecutando el proyecto..."


mvn exec:java -Dexec.mainClass=com.example.Main

