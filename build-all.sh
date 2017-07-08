#!/bin/sh

# Note: make a new jar with Eclipse -> Export -> Runnable Jar on Cruciform-desktop folder.

# Pack all jars with jdk 8 for users without jdk 8 installed.
java -jar packr.jar packr-config-linux64.json
java -jar packr.jar packr-config-linux32.json
#java -jar packr.jar packr-config-mac.json
java -jar packr.jar packr-config-windows.json

# Copy some files to each folder.
cp CREDITS.md LICENSE.txt README.md out-linux64/
cp CREDITS.md LICENSE.txt README.md out-linux32/
#cp CREDITS.md LICENSE.txt README.md out-mac/
cp CREDITS.md LICENSE.txt README.md out-windows/
cp CREDITS.md LICENSE.txt README.md out-jar/

# Windows .exe doesn't seem to work, so use a .bat file.
cp cruciform.bat out-windows/
rm out-windows/cruciform.exe

# Zip all builds
zip -r cruciform-linux64 out-linux64
zip -r cruciform-linux32 out-linux32
#zip -r cruciform-mac out-mac
zip -r cruciform-windows out-windows
zip -r cruciform-jar out-jar
