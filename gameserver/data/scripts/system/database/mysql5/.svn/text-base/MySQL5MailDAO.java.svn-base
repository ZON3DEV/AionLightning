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
package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.dao.ItemStoneListDAO;
import org.openaion.gameserver.dao.MailDAO;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Letter;
import org.openaion.gameserver.model.gameobjects.PersistentState;
import org.openaion.gameserver.model.gameobjects.player.Mailbox;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.PlayerCommonData;
import org.openaion.gameserver.model.gameobjects.player.StorageType;


/**
 * @author kosyachok
 *
 */
public class MySQL5MailDAO extends MailDAO
{
	private static final Logger log = Logger.getLogger(MySQL5MailDAO.class);
	
	@Override
	public Mailbox loadPlayerMailbox(Player player)
	{
		final Mailbox mailbox = new Mailbox();
		final int playerId = player.getObjectId();
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM mail WHERE mailRecipientId = ? LIMIT 100");
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			List<Item> mailboxItems = loadMailboxItems(playerId);
			while(rset.next())
			{
				int mailUniqueId = rset.getInt("mailUniqueId");
				int recipientId = rset.getInt("mailRecipientId");
				String senderName = rset.getString("senderName");
				String mailTitle = rset.getString("mailTitle");
				String mailMessage = rset.getString("mailMessage");
				int unread = rset.getInt("unread");
				int attachedItemId = rset.getInt("attachedItemId");
				long attachedKinahCount = rset.getLong("attachedKinahCount");
				int express = rset.getInt("express");
				Timestamp recievedTime = rset.getTimestamp("recievedTime");
				Item attachedItem = null;
				if(attachedItemId != 0)
					for(Item item : mailboxItems)
						if(item.getObjectId() == attachedItemId)
						{
							if(item.getItemTemplate().isArmor(true) || item.getItemTemplate().isWeapon())
								DAOManager.getDAO(ItemStoneListDAO.class).load(Collections.singletonList(item));
							
							attachedItem = item;
						}
						
				Letter letter = new Letter(mailUniqueId, recipientId, attachedItem, attachedKinahCount, mailTitle,
					mailMessage, senderName, recievedTime, unread == 1, express == 1);
				letter.setPersistState(PersistentState.UPDATED);
				mailbox.putLetterToMailbox(letter);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		
		return mailbox;
	}
	
	private List<Item> loadMailboxItems(final int playerId)
	{
		final List<Item> mailboxItems = new ArrayList<Item>();
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM inventory WHERE `itemOwner` = ? AND `itemLocation` = 127");
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			while(rset.next())
			{
				int itemUniqueId = rset.getInt("itemUniqueId");
				int itemId = rset.getInt("itemId");
				long itemCount = rset.getLong("itemCount");
				int itemColor = rset.getInt("itemColor");
				int itemOwner = rset.getInt("itemOwner");
				int isEquiped = rset.getInt("isEquiped");
				int isSoulBound = rset.getInt("isSoulBound");
				int slot = rset.getInt("slot");
				int enchant = rset.getInt("enchant");
				int itemSkin = rset.getInt("itemSkin");
				int fusionedItem = rset.getInt("fusionedItem");
				int optionalSocket = rset.getInt("optionalSocket");
				int optionalFusionSocket = rset.getInt("optionalFusionSocket");
				String crafterName = rset.getString("itemCreator");
				long itemCreationTime = rset.getTimestamp("itemCreationTime").getTime();
				long tempItemTime = rset.getLong("itemExistTime");
				int tempTradeTime = rset.getInt("itemTradeTime");
				Item item = new Item(itemUniqueId, itemId, itemCount, itemColor, isEquiped == 1, isSoulBound == 1, slot, StorageType.MAILBOX.getId(), enchant, itemSkin, fusionedItem, optionalSocket, optionalFusionSocket, crafterName, itemOwner, itemCreationTime, tempItemTime, tempTradeTime);
				item.setPersistentState(PersistentState.UPDATED);
				mailboxItems.add(item);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		
		return mailboxItems;
	}
	
	@Override
	public void storeMailbox(Player player)
	{
		Mailbox mailbox = player.getMailbox();
		if (mailbox == null)
			return;
		Collection<Letter> letters = mailbox.getLetters();		
		for(Letter letter : letters)
		{
			storeLetter(letter.getTimeStamp(), letter);
		}		
	}
	
	@Override
	public boolean storeLetter(Timestamp time, Letter letter)
	{
		boolean result = false;
		switch(letter.getLetterPersistentState())
		{
			case NEW:
				result = saveLetter(time, letter);
				break;
			
			case UPDATE_REQUIRED:
				result = updateLetter(time, letter);
				break;
			/*	
			case DELETED:
				return deleteLetter(letter);*/
		}
		letter.setPersistState(PersistentState.UPDATED);
		
		return result;
	}
	
	private boolean saveLetter(final Timestamp time, final Letter letter)
	{
		int attachedItemId = 0;
		if(letter.getAttachedItem() != null)
			attachedItemId = letter.getAttachedItem().getObjectId();
			
		final int fAttachedItemId = attachedItemId;
		boolean success = false;
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO `mail` (`mailUniqueId`, `mailRecipientId`, `senderName`, `mailTitle`, `mailMessage`, `unread`, `attachedItemId`, `attachedKinahCount`, `express`, `recievedTime`) VALUES(?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1, letter.getObjectId());
			stmt.setInt(2, letter.getRecipientId());
			stmt.setString(3, letter.getSenderName());
			stmt.setString(4, letter.getTitle());
			stmt.setString(5, letter.getMessage());
			stmt.setBoolean(6, letter.isUnread());
			stmt.setInt(7, fAttachedItemId);
			stmt.setLong(8, letter.getAttachedKinah());
			stmt.setBoolean(9, letter.isExpress());
			stmt.setTimestamp(10, time);
			stmt.execute();
			success = true;
		}
		catch(Exception e)
		{
			log.error(e);
			success = false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return success;
	}
	
	private boolean updateLetter(final Timestamp time, final Letter letter)
	{
		int attachedItemId = 0;
		if(letter.getAttachedItem() != null)
			attachedItemId = letter.getAttachedItem().getObjectId();
		
		final int fAttachedItemId = attachedItemId;
		boolean success = false;
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE mail SET  unread=?, attachedItemId=?, attachedKinahCount=?, recievedTime=? WHERE mailUniqueId=?");
			stmt.setBoolean(1, letter.isUnread());
			stmt.setInt(2, fAttachedItemId);
			stmt.setLong(3, letter.getAttachedKinah());
			stmt.setTimestamp(4, time);
			stmt.setInt(5, letter.getObjectId());
			stmt.execute();
			success = true;
		}
		catch(Exception e)
		{
			log.error(e);
			success = false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return success;
	}
	
	@Override
	public boolean deleteLetter (final int letterId)
	{
		Connection con = null;
		boolean success = false;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM mail WHERE mailUniqueId=?");
			stmt.setInt(1, letterId);
			stmt.execute();
			success = true;
		}
		catch(Exception e)
		{
			log.error(e);
			success = false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return success;
	}

	@Override
	public void updateOfflineMailCounter(final PlayerCommonData recipientCommonData)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE players SET mailboxLetters=? WHERE name=?");
			stmt.setInt(1, recipientCommonData.getMailboxLetters());				
			stmt.setString(2, recipientCommonData.getName());
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
	public int[] getUsedIDs() 
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT mailUniqueId FROM mail", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			ResultSet rs = statement.executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			int[] ids = new int[count];
			for(int i = 0; i < count; i++)
			{
				rs.next();
				ids[i] = rs.getInt("mailUniqueId");
			}
			return ids;
		}
		catch(SQLException e)
		{
			log.error("Can't get list of id's from mail table", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return new int[0];
	}
	
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
