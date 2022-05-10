#!/bin/bash

case $1 in
noloop)
  [ -d log/ ] || mkdir log/
  [ -f log/console.log ] && mv log/console.log "log/console/`date +%Y-%m-%d_%H-%M-%S`_console.log"
  java -Xms128m -Xmx1536m -Xbootclasspath/p:./libs/jsr166.jar -cp ./libs/*:open-aion-game.jar org.openaion.gameserver.GameServer > log/console.log 2>&1
  echo $! > gameserver.pid
  echo "Server started!"
  ;;
*)
  ./StartGS_loop.sh &
  ;;
esac
