/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;

import java.util.Map;
import java.util.Map.Entry;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.dao.SpawnDAO;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.spawn.SpawnEngine;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;
import org.openaion.gameserver.world.World;
import org.openaion.gameserver.world.exceptions.AlreadySpawnedException;


/**
 * @author Luno
 * @author blakawk
 */

public class Spawn extends AdminCommand 
{
	public Spawn()
	{
		super("spawn");
	}

	@Override
	public void executeCommand(Player admin, String[] params) {
		String syntax = "usage: //spawn (<group name> <npc id> | all) | <npc id> | <spawn id> <norespawn>\nor\nsyntax: //spawn list <group name>";

		if (admin.getAccessLevel() < AdminConfig.COMMAND_SPAWN)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params.length < 1 || params.length > 3)
		{
			PacketSendUtility.sendMessage(admin, syntax);
			return;
		}

		String groupName = null;
		int npcId = -1;
		int spawnId = -1;
		int objectId = -1;
		boolean noRespawn = false;
		boolean listSpawns = false;

		try 
		{
			try 
			{
				if (params[0].startsWith("#"))
				{
					spawnId = Integer.parseInt(params[0].substring(1));
				}
				else
				{
					npcId = Integer.parseInt(params[0]);
				}
				if (params.length == 2 && "norespawn".equalsIgnoreCase(params[1]))
				{
					noRespawn = true;
				}
			}
			catch (NumberFormatException e)
			{
				groupName = params[0];

				if (params.length == 1 && "list".equalsIgnoreCase(params[0]))
				{
					groupName = null;
					listSpawns = true;
				}
				else if (params.length >= 2)
				{
					if ("all".equalsIgnoreCase(params[1]))
					{
						npcId = 0;
					}
					else
					{
						if (params.length == 2 && "list".equalsIgnoreCase(params[0]))
						{
							groupName = params[1];
							listSpawns = true;
						}
						else
						{
							npcId = Integer.parseInt(params[1]);
							if (params.length == 3 && "norespawn".equalsIgnoreCase(params[2]))
							{
								noRespawn = true;
							}
						}
					}
				}
				else
				{
					PacketSendUtility.sendMessage(admin, syntax);
					return;
				}
			}
		}
		catch (NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, syntax);
			return;
		}

		float x = admin.getX();
		float y = admin.getY();
		float z = admin.getZ();
		byte h = admin.getHeading();
		int worldId = admin.getWorldId();

		if (listSpawns)
		{
			if (groupName != null)
			{
				Map<Integer, SpawnTemplate> spawns = DAOManager.getDAO(SpawnDAO.class).listSpawns(admin.getObjectId(), groupName, SpawnDAO.SpawnType.ALL);
				
				if (spawns == null)
				{
					PacketSendUtility.sendMessage(admin, "Group "+groupName+" does not exists");
					return;
				}
				
				if (spawns.size() == 0)
				{
					PacketSendUtility.sendMessage(admin, "Group "+groupName+" is empty");
					return;
				}

				PacketSendUtility.sendMessage(admin, ">>> SPAWNS FOR "+groupName.toUpperCase());
				for (Entry<Integer, SpawnTemplate> spawn : spawns.entrySet())
				{
					SpawnTemplate t = spawn.getValue();
					String toSend = "Spawn #"+t.getSpawnId()+" (npc #"+t.getSpawnGroup().getNpcid()+")";
					if (spawn.getKey() == t.getSpawnId() || !t.isSpawned(1))
					{
						toSend += " not spawned"+(t.isNoRespawn(1)?", norespawn":", autorespawn");
					}
					else
					{
						toSend += " pos=["+t.getWorldId()+" "+t.getX()+" "+t.getY()+" "+t.getZ()+" "+t.getHeading()+"], oid="+spawn.getKey()+(t.isNoRespawn(1)?", norespawn":"");
					}
					PacketSendUtility.sendMessage(admin, toSend);
				}
				PacketSendUtility.sendMessage(admin, ">>> END OF SPAWN LIST");
			}
			else
			{
				Map<String, Integer> groups = DAOManager.getDAO(SpawnDAO.class).listSpawnGroups(admin.getObjectId());

				if (groups == null || groups.size() == 0)
				{
					PacketSendUtility.sendMessage(admin, "You don't have any spawn group");
					return;
				}

				PacketSendUtility.sendMessage(admin, ">>> SPAWN GROUPS FOR "+admin.getName().toUpperCase());
				for (Entry<String, Integer> group : groups.entrySet())
				{
					boolean isSpawned = DAOManager.getDAO(SpawnDAO.class).isSpawned(admin.getObjectId(), group.getKey());
					PacketSendUtility.sendMessage(admin, "Group \""+group.getKey()+"\" contains "+group.getValue()+" spawns and "+(isSpawned?"is":"isn't")+" spawned");
				}
				PacketSendUtility.sendMessage(admin, ">>> END OF SPAWN LIST");
			}
			return;
		}

		if (npcId == 0 && spawnId == -1)
		{
			Map<Integer, SpawnTemplate> spawns = DAOManager.getDAO(SpawnDAO.class).listSpawns(admin.getObjectId(), groupName, SpawnDAO.SpawnType.REMOVED);

			if (spawns == null)
			{
				PacketSendUtility.sendMessage(admin, "Group "+groupName+" does not exists");
				return;
			}

			if (DAOManager.getDAO(SpawnDAO.class).isSpawned(admin.getObjectId(), groupName))
			{
				PacketSendUtility.sendMessage(admin, "Group "+groupName+" is already spawned");
				return;
			}

			for (Entry<Integer, SpawnTemplate> spawn : spawns.entrySet())
			{
				VisibleObject visibleObject = SpawnEngine.getInstance().spawnObject(spawn.getValue(), 1, true);
				String objectName = visibleObject.getObjectTemplate().getName();
				String className = visibleObject.getClass().getSimpleName();
				DAOManager.getDAO(SpawnDAO.class).setSpawned(spawn.getValue().getSpawnId(), visibleObject.getObjectId(), true);
				PacketSendUtility.sendMessage(admin, className+" #"+spawn.getValue().getSpawnGroup().getNpcid()+ " \""+objectName + "\" spawned");
			}

			DAOManager.getDAO(SpawnDAO.class).setGroupSpawned(admin.getObjectId(), groupName, true);
			PacketSendUtility.sendMessage(admin, "Group "+groupName+" spawned with success");
			return;
		}

		SpawnTemplate spawn = null;

		if (spawnId > 0)
		{
			objectId = DAOManager.getDAO(SpawnDAO.class).getSpawnObjectId(spawnId, true);
		}

		if (objectId > 0)
		{
			AionObject object = World.getInstance().findAionObject(objectId);
			if (object == null || !(object instanceof VisibleObject))
			{
				PacketSendUtility.sendMessage(admin, "Cannot find object for spawn #"+spawnId);
				return;
			}

			try
			{
				if (object instanceof Creature)
				{
					((Creature)object).getController().cancelAllTasks();
				}

				VisibleObject vObject = (VisibleObject)object;
				vObject.getSpawn().getSpawnGroup().exchangeSpawn(vObject);
				World.getInstance().setPosition(vObject, worldId, vObject.getSpawn().getX(), vObject.getSpawn().getY(), vObject.getSpawn().getZ(), vObject.getSpawn().getHeading());
				vObject.getController().onRespawn();
				World.getInstance().spawn(vObject);
			}
			catch (AlreadySpawnedException e)
			{
				PacketSendUtility.sendMessage(admin, "Spawn #"+spawnId+" has already respawned");
			}

			PacketSendUtility.sendMessage(admin, "Spawn #"+spawnId+" (\""+ object.getName()+"\") has been respawned with success");
			return;
		}

		if (spawnId > 0)
		{
			spawn = DAOManager.getDAO(SpawnDAO.class).getSpawnTemplate(spawnId);
		}
		else
		{
			spawn = SpawnEngine.getInstance().addNewSpawn(worldId, 1, npcId, x, y, z, h, 0, 0, noRespawn, true);
		}

		if (spawn == null)
		{
			PacketSendUtility.sendMessage(admin, "There is no template with id " + npcId);
			return;
		}

		VisibleObject visibleObject = SpawnEngine.getInstance().spawnObject(spawn, 1, true);
		String objectName = visibleObject.getName();
		String className = visibleObject.getClass().getSimpleName();
		objectId = visibleObject.getObjectId();
		int staticid = 0;

		if (spawnId <= 0)
		{
			spawnId = DAOManager.getDAO(SpawnDAO.class).addSpawn(npcId, admin.getObjectId(), groupName, !noRespawn, worldId, x, y, z, h, objectId, staticid); 
			if (spawnId == 0)
			{
				PacketSendUtility.sendMessage(admin, "The spawn cannot be saved in database, so it will be removed on server restart");
				return;
			}
			spawn.setSpawnId(spawnId);
		}
		else
		{
			DAOManager.getDAO(SpawnDAO.class).setSpawned(spawnId, objectId, true);
		}
		PacketSendUtility.sendMessage(admin, className+" #"+spawn.getSpawnGroup().getNpcid()+ " \""+objectName + "\" spawned with spawn id #"+spawn.getSpawnId());
	}
}
