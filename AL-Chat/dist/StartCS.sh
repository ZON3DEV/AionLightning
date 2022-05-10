#!/bin/sh

err=1
until [ $err == 0 ];
do
	java -Xms128m -Xmx128m -ea -Xbootclasspath/p:./libs/jsr166-1.7.0.jar -cp ./libs/*:AL-Chat.jar com.aionemu.chatserver.ChatServer
	err=$?
done
