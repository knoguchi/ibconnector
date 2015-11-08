#!/bin/bash
set -e
rm -rf IBJts
TWSAPI=twsapi_macunix.970.01.jar
wget -O ${TWSAPI} http://interactivebrokers.github.io/downloads/${TWSAPI}
jar xf ${TWSAPI}

mv IBJts/source/javaclient/com src/main/java
rm ${TWSAPI}
rm -rf IBJts