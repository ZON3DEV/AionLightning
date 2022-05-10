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
package org.openaion.gameserver.model.templates.siege;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sylar
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FortressGateArtifact")
public class FortressGateArtifactTemplate
{
	
	@XmlElement(name = "baseinfo")
	protected SiegeSingleSpawnBaseInfo baseInfo;
	@XmlElement(name = "heal_gate")
	protected FortressGateArtifactHealGateTemplate healGateProperties;

	public SiegeSingleSpawnBaseInfo getBaseInfo()
	{
		return baseInfo;
	}
	
	public int getHealGatePercent()
	{
		return healGateProperties.getPercent();
	}
	
}
