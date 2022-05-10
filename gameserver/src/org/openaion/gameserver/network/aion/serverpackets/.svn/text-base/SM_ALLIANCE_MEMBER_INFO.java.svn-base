/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.List;

import org.openaion.gameserver.model.alliance.PlayerAllianceEvent;
import org.openaion.gameserver.model.alliance.PlayerAllianceMember;
import org.openaion.gameserver.model.gameobjects.player.PlayerCommonData;
import org.openaion.gameserver.model.gameobjects.stats.PlayerLifeStats;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.world.WorldPosition;


/**
 * @author Sarynth (Thx Rhys2002 for Packets)
 *
 */
public class SM_ALLIANCE_MEMBER_INFO extends AionServerPacket
{
	private PlayerAllianceMember member;
	private PlayerAllianceEvent event;
	
	public SM_ALLIANCE_MEMBER_INFO(PlayerAllianceMember member, PlayerAllianceEvent event)
	{
		this.member = member;
		this.event = event;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		PlayerCommonData pcd = member.getCommonData();
		WorldPosition wp = pcd.getPosition();
		
		/**
		 * Required so that when member is disconnected, and his
		 * playerAllianceGroup slot is changed, he will continue
		 * to appear as disconnected to the alliance.
		 */
		if (!member.isOnline())
			event = PlayerAllianceEvent.DISCONNECTED;
		
		writeD(buf, member.getAllianceId());
		writeD(buf, member.getObjectId());
		if (member.isOnline())
		{
			PlayerLifeStats pls = member.getPlayer().getLifeStats();
			writeD(buf, pls.getMaxHp());
			writeD(buf, pls.getCurrentHp());
			writeD(buf, pls.getMaxMp());
			writeD(buf, pls.getCurrentMp());
			writeD(buf, pls.getMaxFp());
			writeD(buf, pls.getCurrentFp());
		}
		else
		{
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
		}
		writeD(buf, wp.getMapId());
		writeD(buf, wp.getMapId());
		writeF(buf, wp.getX());
		writeF(buf, wp.getY());
		writeF(buf, wp.getZ());
		writeC(buf, pcd.getPlayerClass().getClassId());
		writeC(buf, pcd.getGender().getGenderId());
		writeC(buf, pcd.getLevel());
		writeC(buf, this.event.getId());
		writeH(buf, 0x01); //channel 0x01?
		writeC(buf, 0x00); //wtf is this? 
		switch(this.event)
		{
			case LEAVE:
			case LEAVE_TIMEOUT:
			case BANNED:
			case MOVEMENT:
			case DISCONNECTED:
				break;
				
			case ENTER:
			case UPDATE:
			case RECONNECT:
			case MEMBER_GROUP_CHANGE:
				
			case APPOINT_VICE_CAPTAIN: // Unused maybe...
			case DEMOTE_VICE_CAPTAIN:
			case APPOINT_CAPTAIN:
				writeS(buf, pcd.getName());
				writeD(buf, 0x00); //unk
				
				if (member.isOnline())
				{
					List<Effect> abnormalEffects = member.getPlayer().getEffectController().getAbnormalEffects();
					writeH(buf, abnormalEffects.size());
					for(Effect effect : abnormalEffects)
					{
						writeD(buf, effect.getEffectorId());
						writeH(buf, effect.getSkillId());
						writeC(buf, effect.getSkillLevel());
						writeC(buf, effect.getTargetSlot());
						writeD(buf, effect.getElapsedTime());
					}
				}
				else
				{
					writeH(buf, 0);
				}
				break;
			default:
				break;
		}
	}
	
}
