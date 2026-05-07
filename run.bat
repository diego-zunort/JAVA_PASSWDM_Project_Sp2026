@echo off
cd /d "%~dp0"
if not exist out mkdir out
for /r src %%f in (*.java) do echo %%f >> sources.txt
javac -cp lib\sqlite-jdbc.jar -d out @sources.txt
del sources.txt
java -cp out;lib\sqlite-jdbc.jar Main
