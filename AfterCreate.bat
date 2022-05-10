@ECHO OFF
TITLE
COLOR 0B
CLS

ECHO.
ECHO	Kopiere erstellte Server Dateien in das Core Verzeichnis...
ECHO.
ECHO.
copy /b AL-Login\build\AL-Login.zip ..\..\Server\AL-Login.zip
copy /b AL-Chat\build\AL-Chat.zip ..\..\Server\AL-Chat.zip
copy /b AL-Game\build\AL-Game.zip ..\..\Server\AL-Game.zip
copy /b AL-Commons\build\AL-Commons.zip ..\..\Server\AL-Commons.zip
GOTO EXTRACT

:EXTRACT
copy /b "Tools\Helper\7z.rn" 7z.exe
CLS

ECHO.
ECHO	Entpacke Server Dateien in das Core Verzeichnis...
ECHO.
ECHO.
7z x ..\..\Server\AL-Login.zip -o..\..\Server\AuthServer\
7z x ..\..\Server\AL-Chat.zip -o..\..\Server\ChatServer\
7z x ..\..\Server\AL-Game.zip -o..\..\Server\WorldServer\
7z x ..\..\Server\AL-Commons.zip -o..\..\Server\WorldServer\
GOTO CLEANUP

:CLEANUP
CLS

ECHO.
ECHO	Entferne nicht mehr gebrauchte Dateien und Verzeichnisse...
ECHO.
ECHO.
rmdir AL-Login\build\ /q /s
rmdir AL-Chat\build\ /q /s
rmdir AL-Game\build\ /q /s
rmdir AL-Commons\build\ /q /s
del 7z.exe