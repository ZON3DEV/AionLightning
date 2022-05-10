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
package org.openaion.gameserver.model.group;

import java.util.Collection;

import org.openaion.commons.objects.filter.ObjectFilter;
import org.openaion.gameserver.configs.main.GroupConfig;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_GROUP_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_GROUP_MEMBER_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_INSTANCE_COOLDOWN;
import org.openaion.gameserver.network.aion.serverpackets.SM_LEAVE_GROUP_MEMBER;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;


/**
 * @author ATracer, Lyahim, Simple
 * @author Jego
 */
public class PlayerGroup extends AionObject
{
	private LootGroupRules			lootGroupRules		= new LootGroupRules();

	private Player				groupLeader;

	private FastMap<Integer, Player>	groupMembers		= new FastMap<Integer, Player>().shared();

	private int				RoundRobinNr		= 0;
	
	private int				instancePoints		= 0;
	private int				instanceKills		= 0;
	private long 				instanceStartTime	= 0;
	private boolean				instanceDisplaycounter	= true;

	/**
	 * Instantiates new player group with unique groupId
	 * 
	 * @param groupId
	 */
	public PlayerGroup(int groupId, Player groupleader)
	{
		super(groupId);
		this.groupMembers.put(groupleader.getObjectId(), groupleader);
		this.setGroupLeader(groupleader);
		groupleader.setPlayerGroup(this);
		PacketSendUtility.sendPacket(groupLeader, new SM_GROUP_INFO(this));
	}

	/**
	 * @return the groupId
	 */
	public int getGroupId()
	{
		return this.getObjectId();
	}

	/**
	 * @return the groupLeader
	 */
	public Player getGroupLeader()
	{
		return groupLeader;
	}

	/**
	 * Used to set group leader
	 * 
	 * @param groupLeader
	 *            the groupLeader to set
	 */
	public void setGroupLeader(Player groupLeader)
	{
		this.groupLeader = groupLeader;
	}

	/**
	 * Adds player to group
	 * 
	 * @param newComer
	 */
	public void addPlayerToGroup(Player newComer)
	{
		groupMembers.put(newComer.getObjectId(), newComer);
		newComer.setPlayerGroup(this);
		PacketSendUtility.sendPacket(newComer, new SM_GROUP_INFO(this));
		updateGroupUIToEvent(newComer, GroupEvent.ENTER);
	}

	/**
	 * This method will return a round robin group member.
	 * 
	 * @param npc
	 *            The killed Npc
	 * @return memberObjId or 0 if the selected player isn't in range.
	 */
	public int getRoundRobinMember(Npc npc)
	{
		if(size() == 0)
			return 0;

		RoundRobinNr = ++RoundRobinNr % size();
		int i = 0;
		for(Player player : getMembers())
		{
			if(i == RoundRobinNr)
			{
				if(MathUtil.isIn3dRange(player, npc, GroupConfig.GROUP_MAX_DISTANCE))
				{ // the player is in range of the killed NPC.
					return player.getObjectId();
				}
				else
				{
					return 0;
				}
			}
			i++;
		}
		return 0;
	}

	/**
	 * Removes player from group
	 * 
	 * @param player
	 */
	public void removePlayerFromGroup(Player player)
	{
		this.groupMembers.remove(player.getObjectId());
		player.setPlayerGroup(null);
		updateGroupUIToEvent(player, GroupEvent.LEAVE);

		/**
		 * Inform all group members player has left the group
		 */
		PacketSendUtility.broadcastPacket(player, new SM_LEAVE_GROUP_MEMBER(), true, new ObjectFilter<Player>(){
			@Override
			public boolean acceptObject(Player object)
			{
				return object.getPlayerGroup() == null ? true : false;
			}
		});
	}

	public void disband() {
		this.groupMembers.clear();
	}

	public void onGroupMemberLogIn(Player player)
	{
		groupMembers.remove(player.getObjectId());
		groupMembers.put(player.getObjectId(), player);
	}

	/**
	 * Checks whether group is full
	 * 
	 * @return true or false
	 */
	public boolean isFull()
	{
		return groupMembers.size() == 6;
	}

	public Collection<Player> getMembers()
	{
		return groupMembers.values();
	}

	public Collection<Integer> getMemberObjIds()
	{
		return groupMembers.keySet();
	}

	/**
	 * @return count of group members
	 */
	public int size()
	{
		return groupMembers.size();
	}

	/**
	 * @return the lootGroupRules
	 */
	public LootGroupRules getLootGroupRules()
	{
		return lootGroupRules;
	}

	public void setLootGroupRules(LootGroupRules lgr)
	{
		this.lootGroupRules = lgr;
		for(Player member : groupMembers.values())
			PacketSendUtility.sendPacket(member, new SM_GROUP_INFO(this));
	}

	/**
	 * Update the Client user interface with the newer data
	 */
	// TODO: Move to GroupService
	public void updateGroupUIToEvent(Player subjective, GroupEvent groupEvent)
	{
		switch(groupEvent)
		{
			case CHANGELEADER:
			{
				for(Player member : this.getMembers())
				{
					PacketSendUtility.sendPacket(member, new SM_GROUP_INFO(this));
					if(subjective.equals(member))
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.CHANGE_GROUP_LEADER());
					PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(this, subjective, groupEvent));
				}
			}
				break;
			case LEAVE:
			{
				boolean changeleader = false;
				if(subjective == this.getGroupLeader())// change group leader
				{
					this.setGroupLeader(this.getMembers().iterator().next());
					changeleader = true;
				}
				for(Player member : this.getMembers())
				{
					if(changeleader)
					{
						PacketSendUtility.sendPacket(member, new SM_GROUP_INFO(this));
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.CHANGE_GROUP_LEADER());
					}
					if(!subjective.equals(member))
						PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(this, subjective, groupEvent));
					if(this.size() > 1)
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.MEMBER_LEFT_GROUP(subjective.getName()));
				}
				eventToSubjective(subjective, GroupEvent.LEAVE);
			}
				break;
			case ENTER:
			{
				eventToSubjective(subjective, GroupEvent.ENTER);
				for(Player member : this.getMembers())
				{
					if(!subjective.equals(member))
					{
						PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(this, subjective, groupEvent));
						PacketSendUtility.sendPacket(member, new SM_INSTANCE_COOLDOWN(subjective));
					}
					PacketSendUtility.sendPacket(subjective, new SM_INSTANCE_COOLDOWN(member));
				}
			}
				break;
			default:
			{
				for(Player member : this.getMembers())
				{
					if(!subjective.equals(member))
						PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(this, subjective, groupEvent));
				}
			}
				break;
		}
	}

	// TODO: Move to GroupService
	private void eventToSubjective(Player subjective, GroupEvent groupEvent)
	{
		for(Player member : getMembers())
		{
			if(!subjective.equals(member))
				PacketSendUtility.sendPacket(subjective, new SM_GROUP_MEMBER_INFO(this, member, groupEvent));
		}
		if(groupEvent == GroupEvent.LEAVE)
			PacketSendUtility.sendPacket(subjective, SM_SYSTEM_MESSAGE.YOU_LEFT_GROUP());
	}
	
	
	public void setGroupInstancePoints(int points)
	{
		instancePoints = points;
	}
	
	public int getGroupInstancePoints()
	{
		return instancePoints;
	}
	
	public void setInstanceStartTimeNow()
	{
		instanceStartTime = System.currentTimeMillis();
	}
	
	public long getInstanceStartTime()
	{
		return instanceStartTime;
	}

	public void setInstanceKills(int kills)
	{
		instanceKills = kills;
	}

	public int getInstanceKills()
	{
		return instanceKills;
	}

	public void setInstanceDisplaycounter(boolean display)
	{
		instanceDisplaycounter = display;
	}

	public boolean getInstanceDisplaycounter()
	{
		return instanceDisplaycounter;
	}

	@Override
	public String getName()
	{
		return "Player Group";
	}
}
