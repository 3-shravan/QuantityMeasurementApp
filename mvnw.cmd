@REM Licensed to the Apache Software Foundation (ASF)
@REM Licensed under the Apache License, Version 2.0

@echo off
setlocal enabledelayedexpansion

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME:~0,-1%

for /F "usebackq tokens=1* delims==" %%A in ("%APP_HOME%\.mvn\jvm.config") do set JVM_CONFIG_MAVEN_PROPS=!JVM_CONFIG_MAVEN_PROPS! %%B

:endInit

@REM Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >nul 2>&1
if "%ERRORLEVEL%"=="0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%\bin\java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@REM Find the project base dir, i.e. the directory that contains the folder ".mvn".
@REM Fallback to current working directory if not found.

set MAVEN_PROJECTBASEDIR=%APP_HOME%
if not "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir

cd /d %~dp0
if not errorlevel 0 goto endDetectBaseDir

set "MAVEN_PROJECTBASEDIR=%CD%"

:endDetectBaseDir
if "%VERBOSE%"=="true" (
    echo %MAVEN_PROJECTBASEDIR%
)
setlocal

:wigglemore
if not "%JAVA_EXE%"=="" goto skipjavacheck
for /F %%j in ('where java.exe 2^>nul') do (
    set "JAVA_EXE=%%j"
    goto skipjavacheck
)

:skipjavacheck
REM Append MAVEN_PROJECTBASEDIR to help Windows find Maven using setlocal
if not "%MAVEN_PROJECTBASEDIR%"=="" (
    set "CLASSPATH=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar;%CLASSPATH%"
)

REM "-Dmaven.home" is not set in this script, but Apache Maven in the `repository` has
REM "maven.home" defined in CMD.exe script as well. Unfortunately, "mdep.bat" that Maven provides
REM has as input name arguments %1 %2 %3 %4 %5 %6 %7 %8 %9 plus %*
REM which enables you to pass command line arguments to Maven.

if "%VERBOSE%"=="true" (
    echo CLASSPATH=%CLASSPATH%
)

%JAVA_EXE% %JVM_CONFIG_MAVEN_PROPS% ^
  -classpath %CLASSPATH% ^
  "-Dbasedir=%MAVEN_PROJECTBASEDIR%" ^
  "-Dfile.encoding=UTF-8" ^
  "-Duser.country=US" ^
  "-Duser.language=en" ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  org.apache.maven.wrapper.MavenWrapperMain %*

if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

if not "%FORK_MODE%"=="true" (
  exit /b %ERROR_CODE%
)

exit /b %ERROR_CODE%
