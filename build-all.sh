#!/bin/sh

# Pack all jars with jdk 8 for users without jdk 8 installed.
java -jar packr.jar packr-config-linux64.json

# Copy credits file to each folder.
cp CREDITS.md out-linux64/
cp CREDITS.md out-linux32/
cp CREDITS.md out-mac/
cp CREDITS.md out-windows/
cp CREDITS.md out-jar/

# Zip all builds
zip -r cruciform-linux64 out-linux64
zip -r cruciform-jar out-jar
