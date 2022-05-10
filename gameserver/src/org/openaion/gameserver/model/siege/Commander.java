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
package org.openaion.gameserver.model.siege;

import org.openaion.gameserver.model.templates.siege.SiegeLocationTemplate;

/**
 * @author Sarynth
 *
 */
public class Commander extends SiegeLocation
{
	public Commander(SiegeLocationTemplate template)
	{
		super(template);
		
		// Commander always 0?
		setVulnerable(false);
		setNextState(0);
	}

	@Override
	public int getInfluenceValue()
	{
		// unknown
		return 0;
	}
}
