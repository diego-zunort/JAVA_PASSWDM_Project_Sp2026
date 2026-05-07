#!/bin/bash
cd "$(dirname "$0")"
mkdir -p out
javac -cp lib/sqlite-jdbc.jar -d out $(find src -name "*.java")
java -cp out:lib/sqlite-jdbc.jar Main
