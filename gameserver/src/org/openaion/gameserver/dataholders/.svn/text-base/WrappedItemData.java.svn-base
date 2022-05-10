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
package org.openaion.gameserver.dataholders;

import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openaion.gameserver.model.items.WrapperItem;
import org.openaion.gameserver.model.templates.item.ItemRace;


/**
 * @author rolandas
 *
 */
@XmlRootElement(name = "wrapped_items")
@XmlAccessorType(XmlAccessType.FIELD)
public class WrappedItemData
{
	
	@XmlElement(required = true, name = "wrapper_item")
	protected List<WrapperItem> list;
	
	/** A map containing all wrap item data */
	private TIntObjectHashMap<WrapperItem> wrappedItemData;

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		wrappedItemData = new TIntObjectHashMap<WrapperItem>();
		for(WrapperItem it : list)
		{
			wrappedItemData.put(it.getItemId(), it);
		}
		list = null;
	}

	/**
	 * @return wrappedItemData()
	 */
	public int size()
	{
		return wrappedItemData.size();
	}
	
	public WrapperItem getItemWrapper(int itemId)
	{
		return wrappedItemData.get(itemId);
	}
	
	public TIntIntHashMap rollItems(int wrapperItemId, int playerLevel, ItemRace race)
	{
		TIntIntHashMap itemCountMap = new TIntIntHashMap();
		
		final WrapperItem wrapperItem = wrappedItemData.get(wrapperItemId);
		if (wrapperItem == null)
			return itemCountMap;
		
		return wrapperItem.rollItems(playerLevel, race);
	}
	
}
