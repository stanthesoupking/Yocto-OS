#!/bin/bash

find ./ -name "*.java" > sources.txt
javac -cp .:classes:/opt/pi4j/lib/'*':lib/'*' @sources.txt
rm sources.txt