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
package com.aionemu.gameserver.model.templates.item;

import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Himiko
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ItemEnchantTemplate")
public class ItemEnchantTemplate {

  @XmlAttribute(name = "id")
  private int id;
  @XmlAttribute(name = "type")
  private EnchantType type;
  @XmlElement(name = "item_enchant", required=false)
  private List<ItemEnchantBonus> item_enchant;
  @XmlTransient
  private TIntObjectHashMap<List<StatFunction>> enchants = new TIntObjectHashMap<List<StatFunction>>();

  public List<StatFunction> getStats(int level) {
    if (enchants.contains(level)) {
      return (List<StatFunction>)enchants.get(level);
    }
    return null;
  }

  public List<ItemEnchantBonus> getItemEnchant() {
    return this.item_enchant;
  }

  public int getId() {
    return this.id;
  }

  public EnchantType getEnchantType() {
    return this.type;
  }

  void afterUnmarshal(Unmarshaller u, Object parent) {
    if (item_enchant == null) {
      return;
    }
    for (ItemEnchantBonus ie : item_enchant) {
      enchants.put(ie.getLevel(), ie.getModifiers().getModifiers());
    }
  }
}
