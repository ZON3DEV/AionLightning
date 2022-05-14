* AE-Manager v0.0.1 modified by Voidstar
* 	Manager for login, (NO-game), chat servers.

IMPORTANT: PLEASE EXTRACT ae_manager-dist.zip ONLY IN ROOT FOLDER OF SERVERS FOLDER.
So, right folder structure should looks like following:

<some path>Aion-Lightning\AL-Chat
<some path>Aion-Lightning\AL-Game
<some path>Aion-Lightning\AL-Login
<some path>Aion-Lightning\startManager.sh
<some path>Aion-Lightning\manager.jar

IMPORTANT: Please build only using ant with script ../Build-AE-Manager.bat
To run manager just double click on manager.jar or user startManager scripts

NOTE:

The AE-Manager memory settings are in the source;src\com\aionemu\manager\ui\ManagerMainView.java. You can edit and experience how you can run
it with maximum memory settings.

The Gameserver is not compatible and anyway is not recomendet to use it for the gameserver, because of time based sql task commmand to restart
the GS.

If you have no restart record in the al_gameserver, here the sql command example;

-- ----------------------------
-- Table structure for tasks
-- ----------------------------
DROP TABLE IF EXISTS `tasks`;
CREATE TABLE `tasks` (
  `id` int(5) NOT NULL,
  `task_type` enum('SHUTDOWN','RESTART') NOT NULL,
  `trigger_type` enum('FIXED_IN_TIME') NOT NULL,
  `trigger_param` text NOT NULL,
  `exec_param` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tasks
-- ----------------------------
INSERT INTO `tasks` VALUES ('1', 'RESTART', 'FIXED_IN_TIME', '04:20:00', '60 5 320');
