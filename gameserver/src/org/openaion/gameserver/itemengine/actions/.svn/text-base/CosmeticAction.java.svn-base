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
package org.openaion.gameserver.itemengine.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;


/**
 * @author Rolandas
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CosmeticAction")
public class CosmeticAction extends AbstractItemAction
{
	@XmlAttribute
	private String lips;

	@XmlAttribute
	private String eyes;

	@XmlAttribute
	private String face;

	@XmlAttribute
	private String hair;

	@XmlAttribute
	private int hairType;

	@XmlAttribute
	private int faceType;

	@XmlAttribute
	private int tattooType;

	@XmlAttribute
	private int makeupType;

	@XmlAttribute
	private int voiceType;

	@XmlAttribute
	private String preset;

	public void act(Player player, Item parentItem, Item targetItem)
	{
	}

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		return false;
	}

	/**
	 * @return the lips
	 */
	public String getLipsColor()
	{
		return lips;
	}

	/**
	 * @return the eyes
	 */
	public String getEyesColor()
	{
		return eyes;
	}

	/**
	 * @return the face
	 */
	public String getFaceColor()
	{
		return face;
	}

	/**
	 * @return the hair
	 */
	public String getHairColor()
	{
		return hair;
	}

	/**
	 * @return the hairType
	 */
	public int getHairType()
	{
		return hairType;
	}

	/**
	 * @return the faceType
	 */
	public int getFaceType()
	{
		return faceType;
	}

	/**
	 * @return the tattooType
	 */
	public int getTattooType()
	{
		return tattooType;
	}

	/**
	 * @return the makeupType
	 */
	public int getMakeupType()
	{
		return makeupType;
	}

	/**
	 * @return the voiceType
	 */
	public int getVoiceType()
	{
		return voiceType;
	}

	/**
	 * @return the preset
	 */
	public String getPresetName()
	{
		return preset;
	}

}
