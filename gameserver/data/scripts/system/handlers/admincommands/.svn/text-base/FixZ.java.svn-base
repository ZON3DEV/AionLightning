package admincommands;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.dao.SpawnDAO;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.templates.spawn.SpawnGroup;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.spawn.SpawnEngine;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;


/**
 * @author kecimis
 *
 */
public class FixZ extends AdminCommand
{
	private static final Logger	log			= Logger.getLogger(FixZ.class);
	
	private Npc npc = null;
	private int numofspawns = 0;
	private int spawned = 0;
	
	public FixZ()
	{
		super("fixz");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_FIXZ)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to use this command!");
			return;
		}

		if (params.length == 0 && admin.getTarget() != null)
		{
			if(admin.getTarget() instanceof Npc)
			{
				Npc target = (Npc) admin.getTarget();
				final SpawnTemplate temp = target.getSpawn();
				final float adminZ = admin.getZ();
				int spawnId = DAOManager.getDAO(SpawnDAO.class).isInDB(target.getNpcId(),temp.getX(),temp.getY());
				
				PacketSendUtility.sendMessage(admin, "spawnId: "+spawnId);
				if (spawnId != 0)
					DAOManager.getDAO(SpawnDAO.class).deleteSpawn(spawnId);//delete from db
					
						//delete spawn,npc
						DataManager.SPAWNS_DATA.removeSpawn(temp);
						target.getController().delete();
											
						
						//create groupname
						//ex: Aetherogenetics Lab Entrance (Object Normal lvl:1)
						StringBuilder comment = new StringBuilder();
						comment.append(target.getObjectTemplate().getName()+" (");
						int isObject = target.getSpawn().getStaticid();
						if (isObject > 0)
							comment.append("Object");
						else
							comment.append("NPC");
						comment.append(" "+target.getObjectTemplate().getRank().name()+" ");
						comment.append("lvl:"+target.getLevel()+")");
									
						//save to db
						DAOManager.getDAO(SpawnDAO.class).addSpawn(temp.getSpawnGroup().getNpcid(), admin.getObjectId(), comment.toString(), false, temp.getWorldId(), temp.getX(), temp.getY(), admin.getZ(), temp.getHeading(), target.getObjectId(),isObject);
						//spawn npc
						int time = 1000;
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
							public void run()
							{
								SpawnTemplate spawn = SpawnEngine.getInstance().addNewSpawn(temp.getWorldId(), 1, temp.getSpawnGroup().getNpcid(), temp.getX(), temp.getY(), adminZ, temp.getHeading(), temp.getWalkerId(), temp.getRandomWalkNr(), false, true);
								SpawnEngine.getInstance().spawnObject(spawn, 1, false);
							}
						}, time);
						PacketSendUtility.sendMessage(admin, comment.toString()+" spawned");
			}
			else 
			{
				PacketSendUtility.sendMessage(admin, "Only instances of NPC are allowed as target!");
				return;
			}
						
		}		
		else if ((params.length == 1 || params.length == 2) && "start".equalsIgnoreCase(params[0]))
		{	
			int stop = 0;
			if (params.length == 1)
				stop = -1;
			else if (params.length == 2 && "start".equalsIgnoreCase(params[0]))
				stop = Integer.parseInt(params[1]);
			
			final Player admin2 = admin;
			List<SpawnGroup> spawngroups = DataManager.SPAWNS_DATA.getSpawnsForWorld(admin2.getWorldId());
			List<SpawnTemplate> templates = new ArrayList<SpawnTemplate>();
		
			PacketSendUtility.sendMessage(admin2, "Fix z coord will start in 10 seconds.");
			
			//load spawns
			for(final SpawnGroup spawngroup : spawngroups)
			{
				templates.addAll(spawngroup.getObjects());
				numofspawns += spawngroup.getObjects().size(); 
			}
			
			PacketSendUtility.sendMessage(admin2, "Aprox time: "+((numofspawns*3.6)/60)+" minutes.");
			
			//execute
			int time = 9000;//time before start
			int counter = 0;
			for (final SpawnTemplate template : templates)
			{
				if (counter >= stop && stop >= 0)
				{
					counter = 0;
					break;
				}
									
				++counter;
				
				time +=800;
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{	
						TeleportService.teleportTo(admin2, template.getWorldId(), template.getX(), template.getY(), template.getZ(), 0);
						
						admin2.getKnownList().doOnAllNpcs(new Executor<Npc>(){
							@Override 
						 	public boolean run(Npc n)
							{
								if( MathUtil.getDistance( (int) n.getX(), (int) n.getY(), (int) admin2.getX(), (int) admin2.getY() ) < 0.1)
								{
									npc = n;
									return true;
								}
								return false;
							}
						}, true);
						
						//delete spawn
						DataManager.SPAWNS_DATA.removeSpawn(template);
					}
				}, time);	
				
				time+=800;
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						if (npc != null)
						{	
							PacketSendUtility.broadcastPacketAndReceive(admin2, new SM_FORCED_MOVE(npc, admin2));
							npc.getController().delete();
						}	
					}
				}, time);
			
				
				time +=2000;
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						if (npc != null)
						{
							//create groupname
							//ex: Aetherogenetics Lab Entrance (Object Normal lvl:1)
							StringBuilder comment = new StringBuilder();
							comment.append(npc.getObjectTemplate().getName()+" (");
							int isObject = npc.getSpawn().getStaticid();
							if (isObject != 0)
								comment.append("Object");
							else
								comment.append("NPC");
							comment.append(" "+npc.getObjectTemplate().getRank().name()+" ");
							comment.append("lvl:"+npc.getLevel()+")");
						
							//check for similar entry
							int spawnId = DAOManager.getDAO(SpawnDAO.class).isInDB(template.getSpawnGroup().getNpcid(), template.getX(), template.getY());
							if (spawnId != 0)
							{
								log.info("[AUDIT]Deleted npc id="+template.getSpawnGroup().getNpcid()+": //moveto "+template.getWorldId()+" "+template.getX()+" "+template.getY()+" "+template.getZ());
								DAOManager.getDAO(SpawnDAO.class).deleteSpawn(spawnId);
							}
																					
							//save to db, if its object dont change Z coord
							if (isObject != 0)
								DAOManager.getDAO(SpawnDAO.class).addSpawn(template.getSpawnGroup().getNpcid(), admin2.getObjectId(), comment.toString(), false, template.getWorldId(), template.getX(), template.getY(), template.getZ(), template.getHeading(), npc.getObjectId(),isObject);
							else	
								DAOManager.getDAO(SpawnDAO.class).addSpawn(template.getSpawnGroup().getNpcid(), admin2.getObjectId(), comment.toString(), false, template.getWorldId(), template.getX(), template.getY(), admin2.getZ(), template.getHeading(), npc.getObjectId(),isObject);
							
							++spawned;
							PacketSendUtility.sendMessage(admin2,spawned+". "+comment.toString()+" spawned");
							
							//reset npc
							npc = null;
						}
						else
						{
							if (template != null)
							log.info("[AUDIT]Missing npc id="+template.getSpawnGroup().getNpcid()+": //moveto "+template.getWorldId()+" "+template.getX()+" "+template.getY()+" "+template.getZ());
						}
					}
				}, time);
						
			}
			templates = null;
			spawngroups = null;
		}
		else
			PacketSendUtility.sendMessage(admin, "Syntax: //fixz <start> <counter>");
		
		PacketSendUtility.sendMessage(admin, "Number of spawns: "+numofspawns);
		
	}
			
			
		
}
		
		
		
			
				
			
			

		
			
		

