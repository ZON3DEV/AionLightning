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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.templates.item.EnchantType;
import com.aionemu.gameserver.model.templates.item.ItemEnchantTemplate;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "enchant_templates")
public class ItemEnchantData {

	@XmlElement(name = "enchant_template", required = true)
	protected List<ItemEnchantTemplate> enchantTemplates;

	@XmlTransient
	private TIntObjectHashMap<ItemEnchantTemplate> enchants = new TIntObjectHashMap<ItemEnchantTemplate>();
	@XmlTransient
	private TIntObjectHashMap<ItemEnchantTemplate> authorizes = new TIntObjectHashMap<ItemEnchantTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (ItemEnchantTemplate it : this.enchantTemplates) {
			getEnchantMap(it.getEnchantType()).put(it.getId(), it);
		}
	}

	private TIntObjectHashMap<ItemEnchantTemplate> getEnchantMap(EnchantType type) {
		if (type == EnchantType.ENCHANT) {
			return this.enchants;
		}
		return this.authorizes;
	}

	public ItemEnchantTemplate getEnchantTemplate(EnchantType type, int id) {
		if (type == EnchantType.ENCHANT) {
			return (ItemEnchantTemplate) this.enchants.get(id);
		}
		return (ItemEnchantTemplate) this.authorizes.get(id);
	}

	public int size() {
		return this.enchants.size() + this.authorizes.size();
	}
}
