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
package org.openaion.gameserver.model.alliance;

import java.util.Collection;

import javolution.util.FastMap;

/**
 * @author Sarynth
 *
 */
public class PlayerAllianceGroup
{
	private FastMap<Integer, PlayerAllianceMember> groupMembers;
	private int allianceId;
	private PlayerAlliance owner;
	
	public PlayerAllianceGroup(PlayerAlliance alliance)
	{
		groupMembers = new FastMap<Integer, PlayerAllianceMember>().shared();
		this.owner = alliance;
	}
	
	public PlayerAlliance getParent()
	{
		return owner;
	}
	
	public void setAllianceId(int allianceId)
	{
		this.allianceId = allianceId;
	}
	
	public int getAllianceId()
	{
		return this.allianceId;
	}
	
	public void addMember(PlayerAllianceMember member)
	{
		groupMembers.put(member.getObjectId(), member);
		member.setAllianceId(allianceId);
	}
	
	/**
	 * 
	 * @param memberObjectId
	 * @return removed member
	 */
	public PlayerAllianceMember removeMember(int memberObjectId)
	{
		return groupMembers.remove(memberObjectId);
	}
	
	public PlayerAllianceMember getMemberById(int memberObjectId)
	{
		return groupMembers.get(memberObjectId);
	}
	
	public Collection<PlayerAllianceMember> getMembers()
	{
		return groupMembers.values();
	}
	public boolean isInSamePlayerAllianceGroup(int memberObjectId,int member2ObjectId)
	{
		return (groupMembers.containsKey(memberObjectId) && groupMembers.containsKey(member2ObjectId));
	}

}
