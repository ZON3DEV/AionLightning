@echo off
title Aion Extreme Emulator Closed Source [Build AE Manager Console] by Dallas

CLS
:MENU
ECHO.
ECHO.
ECHO.
ECHO                                  ''~``
ECHO                                 ( o o )
ECHO    ------------------------.oooO--(_)--Oooo.------------------------
ECHO    .                                                               .
ECHO    .                                                               .
ECHO    .                                                               .
ECHO    .                                                               .
ECHO    .                                                               .
ECHO    .             1 - Build AE-Manager server                       .
ECHO    .             2 - Build PacketSamurai                           .
ECHO    .             3 - Quit                                          .
ECHO    .                         .oooO                                 .
ECHO    .                         (   )   Oooo.                         .
ECHO    ---------------------------\ (----(   )--------------------------
ECHO                                \_)    ) /
ECHO                                      (_/
ECHO.
ECHO.
SET /P Ares=Type 1, 2  or 3 to QUIT, then press ENTER:

IF %Ares%==1 GOTO AE-Manager
IF %Ares%==2 GOTO PacketSamurai
IF %Ares%==3 GOTO QUIT
:FULL

cd ..\Tools\AE-Manager
start /WAIT /B ..\Ant\bin\ant clean dist
GOTO :QUIT

cd ..\Tools\PacketSamurai
start /WAIT /B ..\Ant\bin\ant clean dist
GOTO :QUIT

:AE-Manager
cd ..\Tools\AE-Manager
start /WAIT /B ..\Ant\bin\ant clean dist
GOTO :QUIT

:PacketSamurai
cd ..\Tools\PacketSamurai
start /WAIT /B ..\Ant\bin\ant clean dist
:QUIT
exit
