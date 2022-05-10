/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.model.templates.globaldrops;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;

/**
 * @author Waii
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GlobalRule")
public class GlobalRule {

	@XmlElement(name = "gd_items", required = false)
	protected GlobalDropItems gdItems;

	@XmlElement(name = "gd_maps", required = false)
	protected GlobalDropMaps gdMaps;

	@XmlElement(name = "gd_races", required = false)
	protected GlobalDropRaces gdRaces;

	@XmlElement(name = "gd_tribes", required = false)
	protected GlobalDropTribes gdTribes;

	@XmlElement(name = "gd_ratings", required = false)
	protected GlobalDropRatings gdRatings;

	@XmlElement(name = "gd_worlds", required = false)
	protected GlobalDropWorlds gdWorlds;

	@XmlElement(name = "gd_npcs", required = false)
	protected GlobalDropNpcs gdNpcs;

	@XmlElement(name = "gd_zones", required = false)
	protected GlobalDropZones gdZones;

	@XmlAttribute(name = "rule_name", required = true)
	protected String ruleName;
	@XmlAttribute(name = "min_count")
	protected Long minCount;
	@XmlAttribute(name = "max_count")
	protected Long maxCount;
	@XmlAttribute(name = "base_chance", required = true)
	protected float chance;
	@XmlAttribute(name = "min_diff")
	protected int minDiff;
	@XmlAttribute(name = "max_diff")
	protected int maxDiff;
	@XmlAttribute(name = "restriction_race")
	protected RestrictionRace restrictionRace;
	@XmlAttribute(name = "no_reduction")
	protected boolean noReduction;

	/**
	 * Gets the value of the globalItems property.
	 *
	 * @return possible object is {@link GlobalItems }
	 *
	 */
	public GlobalDropItems getGlobalRuleItems() {
		return gdItems;
	}

	/**
	 * Sets the value of the globalItems property.
	 *
	 * @param value
	 *            allowed object is {@link GlobalItems }
	 *
	 */
	public void setItems(GlobalDropItems value) {
		this.gdItems = value;
	}

	/**
	 * Gets the value of the worlds property.
	 *
	 * @return possible object is {@link WorldTypes }
	 *
	 */
	public GlobalDropWorlds getGlobalRuleWorlds() {
		return gdWorlds;
	}

	/**
	 * Sets the value of the worlds property.
	 *
	 * @param value
	 *            allowed object is {@link WorldTypes }
	 *
	 */
	public void setWorlds(GlobalDropWorlds value) {
		this.gdWorlds = value;
	}

	/**
	 * Gets the value of the npcRaces property.
	 *
	 * @return possible object is {@link NpcRaces }
	 *
	 */
	public GlobalDropRaces getGlobalRuleRaces() {
		return gdRaces;
	}

	/**
	 * Sets the value of the npcRaces property.
	 *
	 * @param value
	 *            allowed object is {@link NpcRaces }
	 *
	 */
	public void setNpcRaces(GlobalDropRaces value) {
		this.gdRaces = value;
	}

	/**
	 * Gets the value of the npcRatings property.
	 *
	 * @return possible object is {@link NpcRatings }
	 *
	 */
	public GlobalDropRatings getGlobalRuleRatings() {
		return gdRatings;
	}

	/**
	 * Sets the value of the npcRatings property.
	 *
	 * @param value
	 *            allowed object is {@link NpcRatings }
	 *
	 */
	public void setNpcRatings(GlobalDropRatings value) {
		this.gdRatings = value;
	}

	/**
	 * Gets the value of the maps property.
	 *
	 * @return possible object is {@link MapIds }
	 *
	 */
	public GlobalDropMaps getGlobalRuleMaps() {
		return gdMaps;
	}

	/**
	 * Sets the value of the maps property.
	 *
	 * @param value
	 *            allowed object is {@link MapIds }
	 *
	 */
	public void setMaps(GlobalDropMaps value) {
		this.gdMaps = value;
	}

	/**
	 * Gets the value of the npcTribes property.
	 *
	 * @return possible object is {@link NpcTribes }
	 *
	 */
	public GlobalDropTribes getGlobalRuleTribes() {
		return gdTribes;
	}

	/**
	 * Sets the value of the npcTribes property.
	 *
	 * @param value
	 *            allowed object is {@link NpcTribes }
	 *
	 */
	public void setNpcTribes(GlobalDropTribes value) {
		this.gdTribes = value;
	}

	/**
	 * Gets the value of the npcs property.
	 *
	 * @return possible object is {@link NpcIds }
	 *
	 */
	public GlobalDropNpcs getGlobalRuleNpcs() {
		return gdNpcs;
	}

	/**
	 * Sets the value of the npcs property.
	 *
	 * @param value
	 *            allowed object is {@link NpcIds }
	 *
	 */
	public void setNpcs(GlobalDropNpcs value) {
		this.gdNpcs = value;
	}

	/**
	 * Gets the value of the zones property.
	 *
	 * @return possible object is {@link NpcIds }
	 *
	 */
	public GlobalDropZones getGlobalRuleZones() {
		return gdZones;
	}

	/**
	 * Sets the value of the zones property.
	 *
	 * @param value
	 *            allowed object is {@link NpcIds }
	 *
	 */
	public void setZones(GlobalDropZones value) {
		this.gdZones = value;
	}

	/**
	 * Gets the value of the globalName property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * Sets the value of the globalName property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setRuleName(String value) {
		this.ruleName = value;
	}

	/**
	 * Gets the value of the minCount property.
	 *
	 * @return possible object is {@link Long }
	 *
	 */
	public long getMinCount() {
		if (minCount == null) {
			return 1L;
		} else {
			return minCount;
		}
	}

	/**
	 * Sets the value of the minCount property.
	 *
	 * @param value
	 *            allowed object is {@link Long }
	 *
	 */
	public void setMinCount(Long value) {
		this.minCount = value;
	}

	/**
	 * Gets the value of the maxCount property.
	 *
	 * @return possible object is {@link Long }
	 *
	 */
	public long getMaxCount() {
		if (maxCount == null) {
			return 1L;
		} else {
			return maxCount;
		}
	}

	/**
	 * Sets the value of the maxCount property.
	 *
	 * @param value
	 *            allowed object is {@link Long }
	 *
	 */
	public void setMaxCount(Long value) {
		this.maxCount = value;
	}

	/**
	 * Gets the value of the chance property.
	 *
	 */
	public float getChance() {
		return chance;
	}

	/**
	 * Sets the value of the chance property.
	 *
	 */
	public void setChance(float value) {
		this.chance = value;
	}

	/**
	 * Gets the value of the minDiff property.
	 *
	 * @return possible object is {@link Integer }
	 *
	 */
	public int getMinDiff() {
		return minDiff;
	}

	/**
	 * Sets the value of the minDiff property.
	 *
	 * @param value
	 *            allowed object is {@link Integer }
	 *
	 */
	public void setMinDiff(int value) {
		this.minDiff = value;
	}

	/**
	 * Gets the value of the maxDiff property.
	 *
	 * @return possible object is {@link Integer }
	 *
	 */
	public int getMaxDiff() {
		return maxDiff;
	}

	/**
	 * Sets the value of the maxDiff property.
	 *
	 * @param value
	 *            allowed object is {@link Integer }
	 *
	 */
	public void setMaxDiff(int value) {
		this.maxDiff = value;
	}

	/**
	 * Gets the value of the restrictionRace property.
	 *
	 * @return possible object is {@link RestrictionRace }
	 *
	 */
	public RestrictionRace getRestrictionRace() {
		return restrictionRace;
	}

	/**
	 * Sets the value of the restrictionRace property.
	 *
	 * @param value
	 *            allowed object is {@link RestrictionRace }
	 *
	 */
	public void setRestrictionRace(RestrictionRace value) {
		this.restrictionRace = value;
	}

	/**
	 * Gets the value of the restrictionRace property.
	 *
	 * @return possible object is {@link RestrictionRace }
	 *
	 */
	public boolean getNoReduction() {
		return noReduction;
	}

	/**
	 * Sets the value of the restrictionRace property.
	 *
	 * @param value
	 *            allowed object is {@link RestrictionRace }
	 *
	 */
	public void setNoReduction(boolean value) {
		this.noReduction = value;
	}

	@XmlType(name = "race_restriction")
	@XmlEnum
	public enum RestrictionRace {
		ASMODIANS, ELYOS
	}
}
