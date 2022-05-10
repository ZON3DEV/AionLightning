/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.ItemStoneListDAO;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.PersistentState;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.items.FusionStone;
import org.openaion.gameserver.model.items.GodStone;
import org.openaion.gameserver.model.items.ManaStone;
import org.openaion.gameserver.model.items.ItemStone.ItemStoneType;


/**
 * @author ATracer
 *
 */
public class MySQL5ItemStoneListDAO extends ItemStoneListDAO
{
	private static final Logger	log	= Logger.getLogger(MySQL5ItemStoneListDAO.class);

	public static final String INSERT_QUERY = "INSERT INTO `item_stones` (`itemUniqueId`, `itemId`, `slot`, `category`) VALUES (?,?,?, ?)";
	public static final String UPDATE_QUERY = "UPDATE `item_stones` SET `itemId`=? where `itemUniqueId`=? AND `category`=?";
	public static final String DELETE_QUERY = "DELETE FROM `item_stones` WHERE `itemUniqueId`=? AND slot=? AND category=?";
	public static final String SELECT_QUERY = "SELECT `itemId`, `slot`, `category` FROM `item_stones` WHERE `itemUniqueId`=?";
	

	@Override
	public void load(final List<Item> items)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			for (Item item : items)
			{
				int manaSlots = 0;
				int fusionSlots = 0;
				if(item.getItemTemplate().isArmor(true) || item.getItemTemplate().isWeapon())
				{
					stmt.setInt(1, item.getObjectId());
					ResultSet rset = stmt.executeQuery();
					while(rset.next())
					{
						int itemId = rset.getInt("itemId");
						int slot = rset.getInt("slot");
						int stoneType = rset.getInt("category");
						if (stoneType == 0 && (itemId / 100000) == 1670)
						{
							if (manaSlots >= item.getSockets(false))
							{
								manaSlots++;
								//too many stones
								Logger.getLogger(this.getClass()).info("[AUDIT]Player: "+item.getOwnerId()+" item: "+item.getObjectId()+" sockets: "+item.getSockets(false)+" socketed: "+manaSlots+" too many socketed manastones.");
								//this.deleteItemStone(item.getObjectId(), slot);
								continue;
							}
							item.getItemStones().add(new ManaStone(item.getObjectId(), itemId, slot, PersistentState.UPDATED));
							manaSlots++;
						}
						else if (stoneType == 1)
						{
							if (!item.getItemTemplate().isWeapon())
							{
								//godstone in non weapon
								Logger.getLogger(this.getClass()).info("[AUDIT]Player: "+item.getOwnerId()+" item: "+item.getObjectId()+" socketed godstone through hacking.");
								//this.deleteGodStone(item.getObjectId(), slot);
								continue;
							}
							item.setGoodStone(new GodStone(item.getObjectId(), itemId, PersistentState.UPDATED));
						}
						else
						{
							if (item.getFusionedItem() == 0)
								continue;
							if (fusionSlots >= item.getSockets(true))
							{
								fusionSlots++;
								//too many fusionstones
								Logger.getLogger(this.getClass()).info("[AUDIT]Player: "+item.getOwnerId()+" item: "+item.getObjectId()+" sockets: "+item.getSockets(true)+" socketed: "+fusionSlots+" too many socketed fusionstones.");
								//this.deleteFusionStone(item.getObjectId(), slot);
								continue;
							}
							item.getFusionStones().add(new FusionStone(item.getObjectId(), itemId, slot, PersistentState.UPDATED));	
							fusionSlots++;
						}
					}
					rset.close();
				}
				else//non weapon and non armor
				{
					stmt.setInt(1, item.getObjectId());
					ResultSet rset = stmt.executeQuery();
					while(rset.next())
					{
						int itemId = rset.getInt("itemId");
						if (itemId != 0)
							log.info("[AUDIT] Player: "+item.getOwnerId()+" item: "+item.getObjectId()+" socketed manastone/godstone through hacking.");
					}
					rset.close();
				}
			}
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not restore ItemStoneList data from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public void save(Player player)
	{
		List<Item> allPlayerItems = player.getAllItems();
		
		for(Item item : allPlayerItems)
		{
			if(item.hasManaStones())
				storeManaStone(item.getItemStones());
			
			if(item.hasFusionStones())
				storeFusionStone(item.getFusionStones());
			
			GodStone godStone = item.getGodStone();
			store(godStone);
		}	
	}

	@Override
	public void storeManaStone(final Set<ManaStone> manaStones)
	{
		if(manaStones == null)
			return;
		
		Iterator<ManaStone> iterator = manaStones.iterator();
		while(iterator.hasNext())
		{
			ManaStone manaStone = iterator.next();
			switch(manaStone.getPersistentState())
			{
				case NEW:
					addItemStone(manaStone.getItemObjId(), manaStone.getItemId(),
						manaStone.getSlot());
					break;
				case DELETED:
					deleteItemStone(manaStone.getItemObjId(), manaStone.getSlot());
					break;
				
			}
			manaStone.setPersistentState(PersistentState.UPDATED);
		}
	}
	
	@Override
	public void storeFusionStone(final Set<FusionStone> manaStones)
	{
		if(manaStones == null)
			return;
		
		Iterator<FusionStone> iterator = manaStones.iterator();
		while(iterator.hasNext())
		{
			FusionStone manaStone = iterator.next();
			switch(manaStone.getPersistentState())
			{
				case NEW:
					addFusionStone(manaStone.getItemObjId(), manaStone.getItemId(),
						manaStone.getSlot());
					break;
				case DELETED:
					deleteFusionStone(manaStone.getItemObjId(), manaStone.getSlot());
					break;
				
			}
			manaStone.setPersistentState(PersistentState.UPDATED);
		}
	}

	/**
	 * 
	 * @param godstone
	 */
	@Override
	public void store(GodStone godstone)
	{
		if(godstone == null)
			return;
		
		switch(godstone.getPersistentState())
		{
			case NEW:
				addGodStone(godstone.getItemObjId(), godstone.getItemId(),
					godstone.getSlot());
				break;
			case UPDATE_REQUIRED:
				updateGodStone(godstone.getItemObjId(), godstone.getItemId());
				break;
			case DELETED:
				deleteGodStone(godstone.getItemObjId(), godstone.getSlot());
				break;
		}
		godstone.setPersistentState(PersistentState.UPDATED);
	}

	/**
	 *  Adds new item stone to item
	 *  
	 * @param itemObjId
	 * @param itemId
	 * @param statEnum
	 * @param statValue
	 * @param slot
	 */
	private void addItemStone(final int itemObjId, final int itemId, final int slot)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, itemObjId);
			stmt.setInt(2, itemId);
			stmt.setInt(3, slot);
			stmt.setInt(4, ItemStoneType.MANASTONE.ordinal());
			stmt.execute();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	/**
	 *  Adds new fusion item stone to item
	 *  
	 * @param itemObjId
	 * @param itemId
	 * @param statEnum
	 * @param statValue
	 * @param slot
	 */
	private void addFusionStone(final int itemObjId, final int itemId, final int slot)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, itemObjId);
			stmt.setInt(2, itemId);
			stmt.setInt(3, slot);
			stmt.setInt(4, ItemStoneType.FUSIONSTONE.ordinal());
			stmt.execute();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	/**
	 * 
	 * @param itemObjId
	 * @param itemId
	 * @param slot
	 */
	private void addGodStone(final int itemObjId, final int itemId, final int slot)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, itemObjId);
			stmt.setInt(2, itemId);
			stmt.setInt(3, slot);
			stmt.setInt(4, ItemStoneType.GODSTONE.ordinal());
			stmt.execute();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	/**
	 * 
	 * @param itemObjId
	 * @param itemId
	 */
	private void updateGodStone(final int itemObjId, final int itemId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, itemId);
			stmt.setInt(2, itemObjId);
			stmt.setInt(3, ItemStoneType.GODSTONE.ordinal());
			stmt.execute();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/**
	 *  Deleted item stone from selected item
	 *  
	 * @param itemObjId
	 * @param slot
	 */
	private void deleteItemStone(final int itemObjId, final int slot)
	{		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, itemObjId);
			stmt.setInt(2, slot);
			stmt.setInt(3, ItemStoneType.MANASTONE.ordinal());
			stmt.execute();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	/**
	 *  Deleted item stone from selected item
	 *  
	 * @param itemObjId
	 * @param slot
	 */
	private void deleteFusionStone(final int itemObjId, final int slot)
	{		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, itemObjId);
			stmt.setInt(2, slot);
			stmt.setInt(3, ItemStoneType.FUSIONSTONE.ordinal());
			stmt.execute();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	/**
	 * 
	 * @param itemObjId
	 * @param slot
	 */
	private void deleteGodStone(final int itemObjId, final int slot)
	{		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, itemObjId);
			stmt.setInt(2, slot);
			stmt.setInt(3, ItemStoneType.GODSTONE.ordinal());
			stmt.execute();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

    @Override
    public boolean supports(String s, int i, int i1)
    {
        return MySQL5DAOUtils.supports(s, i, i1);
    }

}
