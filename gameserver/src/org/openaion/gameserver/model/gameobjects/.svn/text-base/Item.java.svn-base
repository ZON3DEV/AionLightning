/*
 * This file is part of aion-unique <aion-unique.com>.
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
package org.openaion.gameserver.model.gameobjects;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.itemengine.actions.ItemActions;
import org.openaion.gameserver.model.items.FusionStone;
import org.openaion.gameserver.model.items.GodStone;
import org.openaion.gameserver.model.items.ItemStorage;
import org.openaion.gameserver.model.items.ManaStone;
import org.openaion.gameserver.model.templates.item.EquipType;
import org.openaion.gameserver.model.templates.item.ItemQuality;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.model.templates.item.ItemType;
import org.openaion.gameserver.services.ItemService;


/**
 * @author ATracer
 */
public class Item extends AionObject
{	
	private long itemCount = 1;

	private int itemColor = 0;

	private ItemTemplate	itemTemplate;
	private ItemTemplate	itemSkinTemplate;

	private boolean isEquipped = false;

	private int equipmentSlot = ItemStorage.FIRST_AVAILABLE_SLOT;

	private PersistentState persistentState;

	private Set<ManaStone> manaStones;

	private Set<FusionStone> fusionStones;

	private int optionalSocket;
	
	private int optionalFusionSocket;
	
	private GodStone godStone;

	private boolean isSoulBound = false;

	private int itemLocation;
	
	private int enchantLevel;
	
	private int fusionedItemId = 0;

	private String crafterName;
	
	private int ownerId;

	private long itemCreationTime;

	private long tempItemTime;
	
	private int tempTradeTime;
	/**
	 * @param objId
	 * @param itemTemplate
	 * @param itemCount
	 * @param isEquipped
	 * @param equipmentSlot
	 * 
	 * This constructor should be called from ItemService
	 * for newly created items and loadedFromDb
	 */
	public Item(int objId, ItemTemplate itemTemplate, long itemCount, boolean isEquipped, int equipmentSlot, String crafterName, int ownerId, long tempItemTime, int tempTradeTime)
	{
		super(objId);

		this.itemTemplate = itemTemplate;
		this.itemCount = itemCount;
		this.isEquipped = isEquipped;
		this.equipmentSlot = equipmentSlot;
		this.persistentState = PersistentState.NEW;
		this.crafterName = crafterName;
		this.ownerId = ownerId;
		this.itemCreationTime = System.currentTimeMillis();
		this.tempItemTime = tempItemTime;
		this.tempTradeTime = tempTradeTime;
	}

	/**
	 * @param objId
	 * @param itemId
	 * @param itemCount
	 * @param isEquipped
	 * @param equipmentSlot
	 * 
	 * This constructor should be called only from DAO while loading from DB
	 */
	public Item(int objId, int itemId, long itemCount, int itemColor, boolean isEquipped, boolean isSoulBound,int equipmentSlot, int itemLocation,
		int enchant, int itemSkin, int fusionedItem, int optionalSocket, int optionalFusionSocket, String crafterName, int ownerId, long itemCreationTime, long tempItemTime, int tempTradeTime)
	{
		super(objId);

		this.itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
		this.itemCount = itemCount;
		this.itemColor = itemColor;
		this.isEquipped = isEquipped;
		this.isSoulBound = isSoulBound;
		this.equipmentSlot = equipmentSlot;
		this.itemLocation = itemLocation;
		this.enchantLevel = enchant;
		this.fusionedItemId = fusionedItem;
		this.itemSkinTemplate = DataManager.ITEM_DATA.getItemTemplate(itemSkin);
		this.optionalSocket = optionalSocket;
		this.optionalFusionSocket = optionalFusionSocket;
		this.crafterName = crafterName;
		this.ownerId = ownerId;
		this.itemCreationTime = itemCreationTime;
		this.tempItemTime = tempItemTime;
		this.tempTradeTime = tempTradeTime;
	}

	@Override
	public String getName()
	{
		//TODO
		//item description should return probably string and not id
		return String.valueOf(itemTemplate.getNameId());
	}
	
	public String getItemName()
	{
		return itemTemplate.getName();
	}

	public int getOptionalSocket()
	{
		return optionalSocket;
	}
	
	public boolean hasOptionalSocket()
	{
		if(optionalSocket == 0)
			return false;
		return true;
	}
	
	public void setOptionalSocket(int optionalSocket)
	{
		this.optionalSocket = optionalSocket;
	}
	
	public int getOptionalFusionSocket()
	{
		return optionalFusionSocket;
	}
	
	public boolean hasOptionalFusionSocket()
	{
		if(optionalFusionSocket == 0)
			return false;
		return true;
	}
	
	public void setOptionalFusionSocket(int optionalFusionSocket)
	{
		this.optionalFusionSocket = optionalFusionSocket;
	}

	/**
	 * @return the itemTemplate
	 */
	public ItemTemplate getItemTemplate()
	{
		return itemTemplate;
	}
	
	/**
	 * @return the itemAppearanceTemplate
	 */
	public ItemTemplate getItemSkinTemplate()
	{
		if (this.itemSkinTemplate == null)
			return this.itemTemplate;
		return this.itemSkinTemplate;
	}
	
	public void setItemSkinTemplate(ItemTemplate newTemplate)
	{
		this.itemSkinTemplate = newTemplate;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 *@return the itemColor
	 */
	public int getItemColor()
	{
		return itemColor;
	}

	/**
	 * @param itemColor the itemColor to set
	 */
	public void setItemColor(int itemColor)
	{
		this.itemColor = itemColor;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	/**
	 * @param itemTemplate the itemTemplate to set
	 */
	public void setItemTemplate(ItemTemplate itemTemplate)
	{
		this.itemTemplate = itemTemplate;
	}

	/**
	 * @return the itemCount
	 *  Number of this item in stack. Should be not more than template maxstackcount ?
	 */
	public long getItemCount()
	{
		return itemCount;
	}

	/**
	 * @param itemCount the itemCount to set
	 */
	public void setItemCount(long itemCount)
	{
		this.itemCount = itemCount;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	/**
	 *  This method should be called ONLY from Storage class
	 *  In all other ways it is not guaranteed to be updated in a regular update service
	 *  It is allowed to use this method for newly created items which are not yet in any storage 
	 *  
	 * @param addCount 
	 */
	public void increaseItemCount(long addCount)
	{
		//TODO overflow check
		this.itemCount += addCount;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	/**
	 *  This method should be called ONLY from Storage class
	 *  In all other ways it is not guaranteed to be updated in a regular update service
	 *  It is allowed to use this method for newly created items which are not yet in any storage 
	 *  
	 * @param remCount
	 */
	public boolean decreaseItemCount(long remCount)
	{
		if( this.itemCount - remCount >= 0 )
		{
			this.itemCount -= remCount;
			if(itemCount == 0 && !this.itemTemplate.isKinah())
			{
				setPersistentState(PersistentState.DELETED);
			}
			else
			{
				setPersistentState(PersistentState.UPDATE_REQUIRED);
			}
			return true;
		}

		return false;
	}
	
	public boolean isReadable()
	{
		ItemActions actions = this.getItemTemplate().getActions();
		return actions != null && actions.getReadActions().size() > 0;
	}
	
	public boolean canStartQuest()
	{
		ItemActions actions = this.getItemTemplate().getActions();
		return actions != null && actions.getQuestStartActions().size() > 0;		
	}

	/**
	 * @return the isEquipped
	 */
	public boolean isEquipped()
	{
		return isEquipped;
	}

	/**
	 * @param isEquipped the isEquipped to set
	 */
	public void setEquipped(boolean isEquipped)
	{
		this.isEquipped = isEquipped;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	/**
	 * @return the equipmentSlot
	 *  Equipment slot can be of 2 types - one is the ItemSlot enum type if equipped, second - is position in cube
	 */
	public int getEquipmentSlot()
	{
		return equipmentSlot;
	}

	/**
	 * @param equipmentSlot the equipmentSlot to set
	 */
	public void setEquipmentSlot(int equipmentSlot)
	{
		this.equipmentSlot = equipmentSlot;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	/**
	 * This method should be used to lazy initialize empty manastone list
	 * @return the itemStones
	 */
	public Set<ManaStone> getItemStones()
	{
		if(manaStones == null)
			this.manaStones = new TreeSet<ManaStone>(new Comparator<ManaStone>(){

				@Override
				public int compare(ManaStone o1, ManaStone o2)
				{
					if(o1.getSlot() == o2.getSlot())
						return 0;
					return o1.getSlot() > o2.getSlot() ? 1 : -1;
				}
				
			});
		return manaStones;
	}
	
	/**
	 * This method should be used to lazy initialize empty manastone list
	 * @return the itemStones
	 */
	public Set<FusionStone> getFusionStones()
	{
		if(fusionStones == null)
			this.fusionStones = new TreeSet<FusionStone>(new Comparator<FusionStone>(){

				@Override
				public int compare(FusionStone o1, FusionStone o2)
				{
					if(o1.getSlot() == o2.getSlot())
						return 0;
					return o1.getSlot() > o2.getSlot() ? 1 : -1;
				}
				
			});
		return fusionStones;
	}
	
	/**
	 * Check manastones without initialization
	 * 
	 * @return
	 */
	public boolean hasManaStones()
	{
		return manaStones != null && manaStones.size() > 0;
	}
	
	/**
	 * Check manastones without initialization
	 * 
	 * @return
	 */
	public boolean hasFusionStones()
	{
		return fusionStones != null && fusionStones.size() > 0;
	}
	
	/**
	 * @return the goodStone
	 */
	public GodStone getGodStone()
	{
		return godStone;
	}
	
	/**
	 * 
	 * @param itemId
	 * @return
	 */
	public GodStone addGodStone(int itemId)
	{
		PersistentState state = this.godStone != null ? PersistentState.UPDATE_REQUIRED : PersistentState.NEW;
		this.godStone = new GodStone(getObjectId(), itemId, state);
		return this.godStone;
	}
	
	/**
	 * @param goodStone the goodStone to set
	 */
	public void setGoodStone(GodStone goodStone)
	{
		this.godStone = goodStone;
	}

	/**
	 * @return the echantLevel
	 */
	public int getEnchantLevel()
	{
		return enchantLevel;
	}

	/**
	 * @param enchantLevel the echantLevel to set
	 */
	public void setEnchantLevel(int enchantLevel)
	{
		this.enchantLevel = enchantLevel;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	/**
	 * @return the persistentState
	 */
	public PersistentState getPersistentState()
	{
		return persistentState;
	}

	/**
	 *  Possible changes:
	 *  NEW -> UPDATED
	 *  NEW -> UPDATE_REQURIED
	 *  UPDATE_REQUIRED -> DELETED
	 *  UPDATE_REQUIRED -> UPDATED
	 *  UPDATED -> DELETED
	 *  UPDATED -> UPDATE_REQUIRED
	 * @param persistentState the persistentState to set
	 */
	public void setPersistentState(PersistentState persistentState)
	{
		switch(persistentState)
		{
			case DELETED:
				if(this.persistentState == PersistentState.NEW)
				{
					this.persistentState = PersistentState.NOACTION;
					ItemService.releaseItemId(this);
				}
				else
					this.persistentState = PersistentState.DELETED;
				break;
			case UPDATE_REQUIRED:
				if(this.persistentState == PersistentState.NEW)
					break;
			default:
				this.persistentState = persistentState;
		}

	}

	public void setItemLocation(int storageType)
	{
		this.itemLocation = storageType;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	public int getItemLocation()
	{
		return itemLocation;
	}
	
	public int getItemMask()
	{
		return itemTemplate.getMask();
	}

	public boolean isSoulBound()
	{
		return isSoulBound;
	}

	public void setSoulBound(boolean isSoulBound)
	{
		this.isSoulBound = isSoulBound;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	public EquipType getEquipmentType()
	{
		if(itemTemplate.isStigma())
			return EquipType.STIGMA;
		else
			return itemTemplate.getEquipmentType();
	}
	
	@Override
	public String toString()
	{
		return "Item [equipmentSlot=" + equipmentSlot + ", godStone=" + godStone + ", isEquipped=" + isEquipped
			+ ", itemColor=" + itemColor + ", itemCount=" + itemCount + ", itemLocation="
			+ itemLocation + ", itemTemplate=" + itemTemplate + ", manaStones=" + manaStones + ", persistentState="
			+ persistentState + "]";
	}

	public int getItemId()
	{
		return itemTemplate.getTemplateId();
	}

	public int getNameID()
	{
		return itemTemplate.getNameId();
	}
	
	public boolean hasFusionedItem()
	{
		return (fusionedItemId != 0);
	}
	
	public int getFusionedItem()
	{
		return fusionedItemId;
	}
	
	public void setFusionedItem(int itemTemplateId)
	{
		fusionedItemId = itemTemplateId;
	}

	private static int basicSocket(ItemQuality rarity)
	{
		switch(rarity)
		{
		case COMMON: return 1;
		case RARE: return 2;
		case LEGEND: return 3;
		case UNIQUE: return 4;
		case EPIC: return 5;
		default: return 1;
		}
	}
	
	private static int extendedSocket(ItemType type)
	{
		switch(type)
		{
			case NORMAL: return 0;
			case ABYSS: return 2;
			case DRACONIC: return 1;
			case DEVANION: return 0;
			default: return 0;
		}
	}

	public int getSockets(boolean isFusionItem)
	{
		int numSockets;
		if(itemTemplate.isWeapon() || itemTemplate.isArmor(true))
		{
			if(isFusionItem)
			{
				ItemTemplate fusedTemp = DataManager.ITEM_DATA.getItemTemplate(getFusionedItem());
				numSockets = basicSocket(fusedTemp.getItemQuality());
				numSockets += extendedSocket(fusedTemp.getItemType());
				numSockets += hasOptionalFusionSocket() ? getOptionalFusionSocket() : 0;
			}
			else
			{
				numSockets = basicSocket(getItemTemplate().getItemQuality());
				numSockets += extendedSocket(getItemTemplate().getItemType());
				numSockets += hasOptionalSocket() ? getOptionalSocket() : 0;
			}
			if(numSockets < 6)
				return numSockets;
			return 6;
		}
		return 0;
	}
	
	public boolean isStorable(int storageType)
	{
		switch (storageType)
		{
			case 0: 
				return true;
			case 1://regular warehouse
				if (this.getItemTemplate().isStorableinCharWarehouse())
					return true;
			break;
			case 2://account warehouse
				if (this.getItemTemplate().isStorableinAccWarehouse())
					return true;
			break;
			case 3://legion warehouse
				if (this.getItemTemplate().isStorableinLegionWarehouse())
					return true;
			break;
			case 32:
			case 33:
			case 34:
			case 35:
				return true;
		}
		
		return false;
	}

	public String getCrafterName()
	{
		return crafterName;
	}
	
	public void setOwnerId(int ownerId)
	{
		this.ownerId = ownerId;
	}
	
	public int getOwnerId()
	{
		return ownerId;
	}
	
	public long getItemCreationTime()
	{
		return itemCreationTime;
	}
	
	public void setTempItemTime(long timeInSec)
	{
		this.tempItemTime = timeInSec;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 * Used by InventoryDAO when saving
	 * @return
	 */
	public long getTempItemSettedTime()
	{
		return tempItemTime;
	}
	
	public long getTempItemExpireTime()
	{
		return itemCreationTime + (tempItemTime * 1000L);
	}
	
	public long getTempItemTimeLeft()
	{
		long timeLeft = (itemCreationTime + (tempItemTime * 1000L)) - System.currentTimeMillis();
		if(timeLeft < 0)
			timeLeft = 0;
		
		return timeLeft /1000L ;
	}
	
	public void setTempTradeTime(int timeInSec)
	{
		this.tempTradeTime = timeInSec;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 * Used by InventoryDAO when saving
	 * @return
	 */
	public int getTempTradeSettedTime()
	{
		return tempTradeTime;
	}
	
	public int getTempTradeTimeLeft()
	{
		long timeLeft = (itemCreationTime + (tempTradeTime * 1000)) - System.currentTimeMillis();
		if(timeLeft < 0)
			timeLeft = 0;
		
		return (int)(timeLeft /1000L);
	}
}
