@echo off
REM Arcturus Morningstar Extended v4.1.0
REM Startup script for Windows
REM
REM Usage:
REM   emulator.cmd              - Start in console mode
REM   emulator.cmd --gui        - Start with JavaFX GUI

setlocal

if "%EMU_XMX%"=="" set EMU_XMX=4096m

REM Auto-detect JAR file
for %%f in (Habbo-*-jar-with-dependencies.jar) do set EMU_JAR=%%f

if "%EMU_JAR%"=="" (
    echo Error: Could not find Habbo JAR file in current directory.
    echo Set EMU_JAR environment variable or place the JAR in the same directory.
    pause
    exit /b 1
)

echo Starting Arcturus Morningstar Extended...
echo JAR: %EMU_JAR%
echo Max Memory: %EMU_XMX%
echo.

java -Dfile.encoding=UTF-8 -Xmx%EMU_XMX% -XX:+UseZGC -XX:+ZGenerational --enable-preview -jar %EMU_JAR% %*

if %ERRORLEVEL% neq 0 pause
