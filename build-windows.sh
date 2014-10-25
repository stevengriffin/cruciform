#!/bin/sh

java -jar packr.jar packr-config-windows.json

cp CREDITS.md LICENSE.txt README.md out-windows/

# Windows .exe doesn't seem to work, so use a .bat file.
cp cruciform.bat out-windows/
rm out-windows/cruciform.exe

zip -r cruciform-windows out-windows
