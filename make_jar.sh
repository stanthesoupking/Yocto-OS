#!/bin/bash

./make.sh

if [ -f "yocto.jar" ]; then 
    rm yocto.jar
fi

jar -cf yocto.jar .