/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.services;

import org.openaion.gameserver.controllers.SummonController.UnsummonType;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.zone.ZoneName;

/**
 * @author HellBoy
 *
 */
public class ArenaService
{
	public static final ArenaService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public boolean isEnemy(Player effector, Player effected)
	{
		if(effector == effected)
			return false;
		if(!isInSameGroup(effector, effected) && !isInSameAlliance(effector, effected) && effector.getInArena() && effected.getInArena())
			return true;
			
		return false;
	}
	
	public void onDie(final Player player, Creature lastAttacker)
	{
		player.getEffectController().removeAllEffects();
		player.getController().cancelCurrentSkill();
		
		Summon summon = player.getSummon();
		if(summon != null)
			summon.getController().release(UnsummonType.UNSPECIFIED);

		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()), true);
		
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.DIE);
		player.getObserveController().notifyDeath(player);
		
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				if(isInTrinielArena(player))
					TeleportService.teleportTo(player, 120010000, 1, 1005.1f, 1528.9f, 222.1f, 0);
				if(isInSanctumArena(player))
					TeleportService.teleportTo(player, 110010000, 1, 1470.3f, 1343.5f, 563.7f, 0);
			}
		}, 5000);
	}
	
	public boolean isInArena(Player player)
	{
		if(isInTrinielArena(player) || isInSanctumArena(player))
			return true;
		return false;
	}
	
	private boolean isInTrinielArena(Player player)
	{
		int world = player.getWorldId();
		if(world == 120010000 && ZoneService.getInstance().isInsideZone(player, ZoneName.TRINIEL_PVP_ZONE))
			return true;		
		return false;
	}
	
	private boolean isInSanctumArena(Player player)
	{
		int world = player.getWorldId();
		if(world == 110010000 && ZoneService.getInstance().isInsideZone(player, ZoneName.COLISEUM_PVP_ZONE))
			return true;
		return false;
	}
	
	public boolean isInSameGroup(Player player1, Player player2)
	{
		if(player1.isInGroup() && player2.isInGroup())
		{
			if(player1.getPlayerGroup().getGroupId() == player2.getPlayerGroup().getGroupId())
				return true;
		}
		return false;
	}
	
	public boolean isInSameAlliance(Player player1, Player player2)
	{
		if(player1.isInAlliance() && player2.isInAlliance())
		{
			if(player1.getPlayerAlliance().getAllianceIdFor(player1.getObjectId()) == player2.getPlayerAlliance().getAllianceIdFor(player2.getObjectId()))
				return true;
		}
		return false;
	}
	
	@SuppressWarnings("synthetic-access")
	public static class SingletonHolder
	{
		protected static final ArenaService instance = new ArenaService();
	}
}
