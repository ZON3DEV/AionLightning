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
package org.openaion.gameserver.model.templates.tribe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tribe")
public class Tribe
{
	public static final String GUARD_DARK = "GUARD_DARK";
	public static final String GUARD_DRAGON = "GUARD_DRAGON";
	public static final String GUARD_LIGHT = "GUARD";
	
	@XmlElement(name = "aggro")
	protected AggroRelations aggroRelations;
	@XmlElement(name = "friend")
	protected FriendlyRelations friendlyRelations;
	@XmlElement(name = "support")
	protected SupportRelations supportRelations;
	@XmlElement(name = "neutral")
	protected NeutralRelations neutralRelations;
	@XmlElement(name = "hostile")
	protected HostileRelations hostileRelations;
	@XmlAttribute(required = true)
	protected String name;
	@XmlAttribute
	protected String base;
	/**
	 * @return the aggroRelations
	 */
	public AggroRelations getAggroRelations()
	{
		return aggroRelations;
	}
	/**
	 * @return the sypportRelations
	 */
	public SupportRelations getSupportRelations()
	{
		return supportRelations;
	}
	/**
	 * @return the friendlyRelations
	 */
	public FriendlyRelations getFriendlyRelations()
	{
		return friendlyRelations;
	}

	/**
	 * @return the neutralRelations
	 */
	public NeutralRelations getNeutralRelations()
	{
		return neutralRelations;
	}
	/**
	 * @return the hostileRelations
	 */
	public HostileRelations getHostileRelations()
	{
		return hostileRelations;
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @return the base
	 */
	public String getBase()
	{
		return base;
	}
}
