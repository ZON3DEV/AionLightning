@ECHO ############################################
@ECHO ########## Cleaning all files ... ##########
@ECHO ########## Execute from ./trunk/  ##########
@ECHO ############################################


@cd AL-Chat
@call ..\Tools\Ant\bin\ant clean

@cd ..

@cd AL-Commons
@call ..\Tools\Ant\bin\ant clean

@cd ..

@cd AL-Login
@call ..\Tools\Ant\bin\ant clean

@cd ..

@cd AL-Game
@call ..\Tools\Ant\bin\ant clean

@cd ..

@ECHO ############################################
@ECHO ################# Completed ################
@ECHO ############################################

@PAUSE