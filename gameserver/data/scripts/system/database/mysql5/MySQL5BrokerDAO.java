package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DB;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.dao.BrokerDAO;
import org.openaion.gameserver.dao.InventoryDAO;
import org.openaion.gameserver.model.broker.BrokerRace;
import org.openaion.gameserver.model.gameobjects.BrokerItem;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.PersistentState;


public class MySQL5BrokerDAO extends BrokerDAO
{
	private static final Logger	log	= Logger.getLogger(MySQL5BrokerDAO.class);

	@Override
	public List<BrokerItem> loadBroker()
	{
		final List<BrokerItem> brokerItems = new ArrayList<BrokerItem>();

		final List<Item> items = getBrokerItems();
		
		Connection con = null;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM broker");
			ResultSet resultSet = stmt.executeQuery();
					
			while (resultSet.next())
		 	{
				int itemPointer = resultSet.getInt("itemPointer");
				int itemId = resultSet.getInt("itemId");
				long itemCount = resultSet.getLong("itemCount");
				String seller = resultSet.getString("seller");
				int sellerId = resultSet.getInt("sellerId");
				long price = resultSet.getLong("price");
				BrokerRace itemBrokerRace = BrokerRace.valueOf(resultSet.getString("brokerRace"));
				Timestamp expireTime = resultSet.getTimestamp("expireTime");
				Timestamp settleTime = resultSet.getTimestamp("settleTime");
				int sold = resultSet.getInt("isSold");
				int settled = resultSet.getInt("isSettled");

				boolean isSold = sold == 1;
				boolean isSettled = settled == 1;

				Item item = null;
				if(!isSold)
					for(Item brItem : items)
					{
						if(itemPointer == brItem.getObjectId())
						{
							item = brItem;
							break;
						}
					}

				brokerItems.add(new BrokerItem(item, itemId, itemPointer, itemCount, price, seller, sellerId,
					itemBrokerRace, isSold, isSettled, expireTime, settleTime));
		 	}
			
			resultSet.close();
			stmt.close();
		}
		catch(SQLException e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return brokerItems;
	}

	private List<Item> getBrokerItems()
	{
		final List<Item> brokerItems = new ArrayList<Item>();
		
		Connection con = null;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM inventory WHERE `itemLocation` = 126");
			
			ResultSet resultSet = stmt.executeQuery();
					
			while (resultSet.next())
		 	{
				int itemUniqueId = resultSet.getInt("itemUniqueId");
				int itemId = resultSet.getInt("itemId");
				int itemOwner = resultSet.getInt("itemOwner");
				long itemCount = resultSet.getLong("itemCount");
				int itemColor = resultSet.getInt("itemColor");
				int slot = resultSet.getInt("slot");
				int location = resultSet.getInt("itemLocation");
				int enchant = resultSet.getInt("enchant");
				int itemSkin = resultSet.getInt("itemSkin");
				int fusionedItem = resultSet.getInt("fusionedItem");
				int optionalSocket = resultSet.getInt("optionalSocket");
				int optionalFusionSocket = resultSet.getInt("optionalFusionSocket");
				String crafterName = resultSet.getString("itemCreator");
				long itemCreationTime = resultSet.getTimestamp("itemCreationTime").getTime();
				long tempItemTime = resultSet.getLong("itemExistTime");
				int tempTradeTime = resultSet.getInt("itemTradeTime");
				brokerItems.add(new Item(itemUniqueId, itemId, itemCount, itemColor, false, false, slot, location, enchant, itemSkin, fusionedItem, optionalSocket, optionalFusionSocket, crafterName, itemOwner, itemCreationTime, tempItemTime, tempTradeTime));
		 	}
			
			resultSet.close();
			stmt.close();
		}
		catch(SQLException e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return brokerItems;
	}

	@Override
	public boolean store(BrokerItem item)
	{
		boolean result = false;

		if(item == null)
		{
			log.warn("Null broker item on save");
			return result;
		}

		switch(item.getPersistentState())
		{
			case NEW:
				result = insertBrokerItem(item);
				if(item.getItem() != null)
					DAOManager.getDAO(InventoryDAO.class).store(item.getItem(), item.getSellerId());
				break;

			case DELETED:
				result = deleteBrokerItem(item);
				break;

			case UPDATE_REQUIRED:
				result = updateBrokerItem(item);
				break;
		}

		if(result)
			item.setPersistentState(PersistentState.UPDATED);

		return result;
	}

	private boolean insertBrokerItem(final BrokerItem item)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO `broker` (`itemPointer`, `itemId`, `itemCount`,`seller`, `price`, `brokerRace`, `expireTime`, `sellerId`, `isSold`, `isSettled`) VALUES (?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1, item.getItemUniqueId());
			stmt.setInt(2, item.getItemId());
			stmt.setLong(3, item.getItemCount());
			stmt.setString(4, item.getSeller());
			stmt.setLong(5, item.getPrice());
			stmt.setString(6, String.valueOf(item.getItemBrokerRace()));
			stmt.setTimestamp(7, item.getExpireTime());
			stmt.setInt(8, item.getSellerId());
			stmt.setBoolean(9, item.isSold());
			stmt.setBoolean(10, item.isSettled());

			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error(e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}

	private boolean deleteBrokerItem(final BrokerItem item)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM `broker` WHERE `itemPointer` = ? AND `sellerId` = ? AND `expireTime` = ?");
			stmt.setInt(1, item.getItemUniqueId());
			stmt.setInt(2, item.getSellerId());
			stmt.setTimestamp(3, item.getExpireTime());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error(e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}

	private boolean updateBrokerItem(final BrokerItem item)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE broker SET `isSold` = ?, `isSettled` = 1, `settleTime` = ? WHERE `itemPointer` = ? AND `expireTime` = ? AND `sellerId` = ? AND `isSettled` = 0");
			stmt.setBoolean(1, item.isSold());
			stmt.setTimestamp(2, item.getSettleTime());
			stmt.setInt(3, item.getItemUniqueId());
			stmt.setTimestamp(4, item.getExpireTime());
			stmt.setInt(5, item.getSellerId());

			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error(e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public int[] getUsedIDs()
	{
		PreparedStatement statement = DB.prepareStatement("SELECT id FROM players", ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY);

		try
		{
			ResultSet rs = statement.executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			int[] ids = new int[count];
			for(int i = 0; i < count; i++)
			{
				rs.next();
				ids[i] = rs.getInt("id");
			}
			return ids;
		}
		catch(SQLException e)
		{
			log.error("Can't get list of id's from players table", e);
		}
		finally
		{
			DB.close(statement);
		}

		return new int[0];
	}

	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
