#!/bin/sh

# Pack all jars with jdk 8 for users without jdk 8 installed.
java -jar packr.jar packr-config-linux64.json

# Copy some files to each folder.
cp CREDITS.md LICENSE.txt README.md out-linux64/
cp CREDITS.md LICENSE.txt README.md out-linux32/
cp CREDITS.md LICENSE.txt README.md out-mac/
cp CREDITS.md LICENSE.txt README.md out-windows/
cp CREDITS.md LICENSE.txt README.md out-jar/

# Zip all builds
zip -r cruciform-linux64 out-linux64
zip -r cruciform-jar out-jar
