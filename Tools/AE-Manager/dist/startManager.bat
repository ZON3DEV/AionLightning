:######################################################################## 
:# File name: startManager.bat
:# Edited Last By: vyaslav 
:# V 1.0 1
:######################################################################## 

@echo off
title Manager
:start
echo Aion Extreme Server Manager (Credits vyaslav@aionemu)
echo /\  _  \  __                    /\  ___\                                        
echo \ \ \ \ \/\_\    ___     ___    \ \ \____    ___      __   __    ___      __    
echo  \ \  __ \/\ \  / __`\ /' _ `\   \ \  ___\ /' _ `\  /'__`\/\_\ /' _ `\  /'__`\  
echo   \ \ \/\ \ \ \/\ \ \ \/\ \/\ \   \ \ \____/\ \/\ \/\ \ \ \/\ \/\ \/\ \/\  __/  
echo    \ \_\ \_\ \_\ \____/\ \_\ \_\   \ \_____\ \_\ \_\ \___, \ \ \ \_\ \_\ \____\ 
echo     \/_/\/_/\/_/\/___/  \/_/\/_/    \/_____/\/_/\/_/\/_____ \ \_\/_/\/_/\/____/ 
echo                                                        /\____\/_/               
echo                                                        \/____/                  
echo.
echo 
echo.
REM -------------------------------------
set JAVA6="auto"
set JAR=manager.jar

set X86="%ProgramFiles(x86)%"
if %JAVA6% == "auto" (
  echo Autodetecting java
  if %X86% NEQ "" (
    echo Testing x86 folder
    for /D %%j in ("%ProgramFiles(x86)%\jre7*" "%ProgramFiles(x86)%\jre1.7.*" "%ProgramFiles(x86)%\jdk1.7.*" "%ProgramFiles(x86)%\Java\jre7*" "%ProgramFiles(x86)%\Java\jre1.7.*" "%ProgramFiles(x86)%\Java\jdk1.7.*") do (
      echo Checking %%j folder
      if exist "%%j\bin\java.exe" (
        echo Found java in %%j folder
        set JAVA6="%%j\bin\java.exe"
      )
    )
  ) else (
    echo Testing default folder
    if defined ProgramFiles (
    for /D %%j in ("%ProgramFiles%\jre7*" "%ProgramFiles%\jre1.7.*" "%ProgramFiles%\jdk1.7.*" "%ProgramFiles%\Java\jre7*" "%ProgramFiles%\Java\jre1.7.*" "%ProgramFiles%\Java\jdk1.7.*") do (
        echo Checking %%j folder
        if exist "%%j\bin\java.exe" (
          echo Found java in %%j folder
          set JAVA6="%%j\bin\java.exe"
        )
      )
    )
  )
)

:rerun
if %JAVA6% == "auto" (
  echo ERROR: Java not found!
  echo Please download and install from java.com
  pause
  exit
) else (
  echo Starting java from %JAVA6%
  %JAVA6% -Xms8m -Xmx32m -ea -cp ./libs/*;%JAR% com.aionemu.manager.Manager

  if errorlevel 11 (
    if not exist %JAR% (
      echo ========================================
      echo Warning: %JAR% not not found.
      echo ========================================
    )

    echo ERROR: Failed to run %JAR%
    echo JAVA6=%JAVA6%
    pause
    exit
  )

  if errorlevel 10 (
    goto rerun
  )

  if errorlevel 1 (
    if not exist %JAR% (
      echo ========================================
      echo Warning: %JAR% not found.
      echo ========================================
    )

    echo ERROR: Failed to run %JAR%
    echo JAVA6=%JAVA6%
    pause
    exit
  )
)
exit