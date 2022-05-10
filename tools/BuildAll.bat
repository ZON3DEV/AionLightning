@echo off
title Aion Europe  Emulator BuildAll Console -- by Ares!Dallas

CLS
:MENU
ECHO.
ECHO.
ECHO.
ECHO                                  ''~``
ECHO                                 ( o o )
ECHO    ------------------------.oooO--(_)--Oooo.------------------------
ECHO    .             1 - Build All server                              .
ECHO    .             2 - Build Commons server                          .
ECHO    .             3 - Build GameServer server                       .
ECHO    .             4 - Build LoginServer server                      .
ECHO    .             5 - Build ChatServer server                       .
ECHO    .             6 - Build AE-Manager server                       .
ECHO    .             7 - Quit                                          .
ECHO    .                         .oooO                                 .
ECHO    .                         (   )   Oooo.                         .
ECHO    ---------------------------\ (----(   )--------------------------
ECHO                                \_)    ) /
ECHO                                      (_/
ECHO.
ECHO.
SET /P Ares=Type 1, 2 ,3, 4, 5, 6 or 7 to QUIT, then press ENTER:
IF %Ares%==1 GOTO FULL
IF %Ares%==2 GOTO Commons
IF %Ares%==3 GOTO GameServer
IF %Ares%==4 GOTO LoginServer
IF %Ares%==5 GOTO ChatServer
IF %Ares%==6 GOTO AE-Manager
IF %Ares%==7 GOTO QUIT
:FULL

cd ..\Commons 
start /WAIT /B ..\Tools\Ant\bin\ant clean dist

cd ..\GameServer
start /WAIT /B ..\Tools\Ant\bin\ant clean dist

cd ..\LoginServer
start /WAIT /B ..\Tools\Ant\bin\ant clean dist

cd ..\ChatServer
start /WAIT /B ..\Tools\Ant\bin\ant clean dist

cd ..\Tools\AE-Manager
start /WAIT /B ..\Ant\bin\ant clean dist
GOTO :QUIT

:Commons
cd ..\Commons 
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:GameServer
cd ..\GameServer
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:LoginServer
cd ..\LoginServer
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:ChatServer
cd ..\ChatServer
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:AE-Manager
cd ..\Tools\AE-Manager
start /WAIT /B ..\Ant\bin\ant clean dist
:QUIT
exit
