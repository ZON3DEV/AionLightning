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
package org.openaion.gameserver.quest.model;

/**
 * @author MrPoke
 * 
 */

public class QuestVars
{
	private Integer[]	questVars	= new Integer[6];

	public QuestVars()
	{
	}

	public QuestVars(int var)
	{
		setVar(var);
	}

	/**
	 * @param id
	 * @return Quest var by id.
	 */
	public int getVarById(int id)
	{
		if(id == 5)
			return (questVars[id] & 0x03);
		return questVars[id];
	}

	/**
	 * @param id
	 * @param var
	 */
	public void setVarById(int id, int var)
	{
		if(id == 5)
			questVars[id] = (var & 0x03);
		else
			questVars[id] = (var & 0x3F);
	}

	/**
	 * @return integer
	 */
	public int getQuestVars()
	{
		int var = 0;
		var |= questVars[5];
		for(int i = 5; i >= 0; i--)
		{
			if(i == 5)
				var <<= 0x02;
			else
				var <<= 0x06;
			var |= questVars[i];
		}
		return var;
	}
	
	public void setVar(int var)
	{
		for(int i = 0; i < 6; i++)
		{
			if(i == 5)
				questVars[i] = (var & 0x03);
			else
			{
				questVars[i] = (var & 0x3F);
				var >>= 0x06;
			}
		}
	}
}
