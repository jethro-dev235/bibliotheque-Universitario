@echo off
chcp 65001 >nul
title Bibliotheque Universitaire

REM === Java 11 (obligatoire pour ce projet) ===
REM Check JAVA_HOME environment variable first
if not "%JAVA_HOME%"=="" (
    if exist "%JAVA_HOME%\bin\java.exe" (
        goto :java_found
    )
)

REM Check common installation paths
set "JAVA_HOME=C:\Users\GRAPPLER.DESKTOP-1MJB882\AppData\Local\Programs\Eclipse Adoptium\jdk-11.0.29.7-hotspot"
if exist "%JAVA_HOME%\bin\java.exe" (
    goto :java_found
)

set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-11.0.29.7-hotspot"
if exist "%JAVA_HOME%\bin\java.exe" (
    goto :java_found
)

set "JAVA_HOME=C:\Program Files\Java\jdk-11"
if exist "%JAVA_HOME%\bin\java.exe" (
    goto :java_found
)

echo ERREUR: JDK 11 introuvable.
echo Veuillez installer Java 11 depuis https://adoptium.net/temurin/releases/?version=11
echo Ou definissez la variable d'environnement JAVA_HOME.
pause
exit /b 1

:java_found
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Java utilise:
java -version
echo.

REM === Maven (PATH ou dossier temporaire) ===
where mvn >nul 2>&1
if errorlevel 1 (
    if exist "%TEMP%\apache-maven-3.9.6\bin\mvn.cmd" (
        set "PATH=%TEMP%\apache-maven-3.9.6\bin;%PATH%"
    ) else (
        echo ERREUR: Maven introuvable. Installez avec:
        echo   winget install Apache.Maven -e --source winget
        pause
        exit /b 1
    )
)

echo Demarrage de l'application...
echo URL: http://localhost:8080/bibliotheque
echo Login: admin@biblio.com / admin123
echo.
echo Appuyez sur Ctrl+C pour arreter le serveur.
echo.

cd /d "%~dp0"
call mvn clean package cargo:run

pause
