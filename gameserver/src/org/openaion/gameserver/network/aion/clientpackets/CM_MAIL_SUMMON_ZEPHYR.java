package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.spawn.SpawnEngine;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;

/**
 * 
 * @author Sylar
 * 
 */
public class CM_MAIL_SUMMON_ZEPHYR extends AionClientPacket
{	
	
	private int value;
	
	public CM_MAIL_SUMMON_ZEPHYR(int opcode)
	{
		super(opcode);
	}

	
	@Override
	protected void readImpl()
	{
		value = readC();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		if(player != null && value == 1)
		{
			int zephyrNpcId = 0;
			
			switch(player.getCommonData().getRace())
			{
				case ELYOS: zephyrNpcId = 798044; break;
				case ASMODIANS: zephyrNpcId = 798101; break;
			}
			
			if(zephyrNpcId == 0)
				return;
			
			// POSTMAN_ALREADY_SUMMONED
			if(player.getZephyrObjectId() != 0)
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300877));
				return;
			}
			
			// POSTMAN_COOLDOWN (30mn)
			if(player.getLastZephyrInvokationSeconds() > (System.currentTimeMillis() / 1000) - 1800)
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300878));
				return;
			}
			
			if(player.isInState(CreatureState.FLYING) || player.isInState(CreatureState.FLIGHT_TELEPORT) || player.isInState(CreatureState.GLIDING))
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300879));
				return;
			}
			
			SpawnTemplate zst = SpawnEngine.getInstance().addNewSpawn(player.getWorldId(), player.getInstanceId(), zephyrNpcId, player.getX(), player.getY(), player.getZ(), player.getHeading(), 0, 0, true, true);
			VisibleObject zvo = SpawnEngine.getInstance().spawnObject(zst, player.getInstanceId());
			
			if(zvo != null && zvo instanceof Creature)
			{
				final Creature zc = (Creature)zvo;
				player.setZephyrObjectId(zc.getObjectId());
				player.setLastZephyrInvokationSeconds(System.currentTimeMillis() / 1000);
				zc.setTarget(player);
				zc.getMoveController().followTarget(4);
				if(!zc.getMoveController().isScheduled())
					zc.getMoveController().schedule();
				
				// Despawn Zephyr after 5 minutes if not already despawned
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					
					@Override
					public void run()
					{
						int zid = zc.getObjectId();
						AionObject obj = World.getInstance().findAionObject(zid);
						if(obj != null && obj instanceof Creature)
						{
							Creature zephyr = (Creature)obj;
							DataManager.SPAWNS_DATA.removeSpawn(zephyr.getSpawn());
							zephyr.getMoveController().stop();
							zephyr.getController().delete();
						}
						player.setZephyrObjectId(0);
					}
				}, 300000);
			}			
		}
	}
}