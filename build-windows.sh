#!/bin/sh

java -jar packr.jar packr-config-windows.json

cp CREDITS.md LICENSE.txt README.md out-windows/
zip -r cruciform-windows out-windows
