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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sylar
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "siege_spawn")
public class SiegeSpawnList
{
	@XmlAttribute(name = "location_id")
	protected int		locationId;

	@XmlElement(name = "guards")
	protected SiegeGuards guards;
	
	@XmlElement(name = "instance_portal")
	protected InstancePortalTemplate instancePortal;
	
	@XmlElement(name = "fortress_general")
	protected FortressGeneralTemplate fortressGeneral;
	
	@XmlElement(name = "fortress_gate")
	protected List<FortressGateTemplate> fortressGates;
	
	@XmlElement(name = "artifact")
	protected ArtifactTemplate artifact;
	
	@XmlElement(name = "aetheric_field")
	protected AethericFieldTemplate aethericField;
	
	
	public int getLocationId()
	{
		return locationId;
	}
	
	public SiegeGuards getGuards()
	{
		return guards;
	}
	
	public InstancePortalTemplate getInstancePortalTemplate()
	{
		return instancePortal;
	}
	
	public FortressGeneralTemplate getFortressGeneralTemplate()
	{
		return fortressGeneral;
	}
	
	public List<FortressGateTemplate> getFortressGatesTemplates()
	{
		return fortressGates;
	}
	
	public ArtifactTemplate getArtifactTemplate()
	{
		return artifact;
	}
	
	public AethericFieldTemplate getAethericFieldTemplate()
	{
		return aethericField;
	}
	
}
