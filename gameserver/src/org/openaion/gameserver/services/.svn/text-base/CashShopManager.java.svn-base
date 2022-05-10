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
package org.openaion.gameserver.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.network.NetworkConfig;
import org.openaion.gameserver.dao.PlayerDAO;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.PlayerCommonData;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.configs.main.CustomConfig;

/**
 * @author sylar
 * Modified by aion-germany, Dallas
 */


public class CashShopManager
{
        private static final Logger        log        = Logger.getLogger(CashShopManager.class);
        private static CashShopManager instance;
        public HashMap<Integer, ShopCategory> categories = new HashMap<Integer, ShopCategory>();

        public class ShopCategory
        {
                public int id;
                public String name;
                public HashMap<Integer, ShopItem> items = new HashMap<Integer, ShopItem>();
                public Timer timer;
                public ShopCategory(int id)
                {
                        this.id = id;
                }
        }

        public class ShopItem
        {
                public int id;
                public int itemId;
                public int count;
                public long price;
                public String name;
                public String desc = "";
                public int        eyecatch;

                public ShopItem(int id)
                {
                        this.id = id;
                }
        }

        public static CashShopManager getInstance()
        {
                if(instance == null)
                        instance = new CashShopManager();
                return instance;
        }

        public CashShopManager()
        {
                // TODO : Relocate the DAO

                log.info("Loading ingame shop...");

                // The first category (id 1) display all the items. Any additionnal category must be id 2 and +
                @SuppressWarnings("unused")
                int catId = 1;
                @SuppressWarnings("unused")
                int itId = 0;

                Connection con = null;

                try
                {
                        con = DatabaseFactory.getConnection();
                        PreparedStatement stmt = con.prepareStatement("SELECT * FROM " + CustomConfig.AIONSHOP_DB + ".aionshop_categories");
                        ResultSet catRs = stmt.executeQuery();
                        while(catRs.next())
                        {
                                ShopCategory cat = new ShopCategory(catRs.getInt("categoryId"));
                                cat.name = catRs.getString("categoryName");

                                PreparedStatement stmt2 = con.prepareStatement("SELECT * FROM " + CustomConfig.AIONSHOP_DB + ".aionshop_items WHERE itemCategory = ?");
                                stmt2.setInt(1, cat.id);
                                ResultSet rs2 = stmt2.executeQuery();

                                while(rs2.next())
                                {
                                        ShopItem item = new ShopItem(rs2.getInt("itemUniqueId"));
                                        item.itemId = rs2.getInt("itemTemplateId");
                                        item.count = rs2.getInt("itemCount");
                                        item.price = rs2.getInt("itemPrice");
                                        item.name = rs2.getString("itemName");
                                        item.desc = rs2.getString("itemDesc");
                                        item.eyecatch = rs2.getInt("itemEyecatch");
                                        cat.items.put(item.id, item);
                                }

                                rs2.close();
                                stmt2.close();

                                categories.put(cat.id, cat);
                        }

                        catRs.close();
                        stmt.close();
                        con.close();

                }
                catch(Exception e)
                {
                        log.error("Cannot load ingame shop contents !", e);
                }
                finally
                {
                        DatabaseFactory.close(con);
                }

                log.info("Loaded " + getAllItems().length +" items.");
        }

        public int getItemsCount()
        {
                return getAllItems().length;
        }

        public int getItemsCount(int catId)
        {
                return getItems(catId).length;
        }

        public ShopItem getItem(int id)
        {
                for(ShopCategory category : categories.values())
                        if(category.items.containsKey(id))
                                return category.items.get(id);
                return null;
        }

        public ShopItem[] getAllItems()
        {
                ArrayList<ShopItem> res = new ArrayList<ShopItem>();
                for(ShopCategory category : categories.values())
                        res.addAll(category.items.values());
                return res.toArray(new ShopItem[res.size()]);
        }

        public ShopItem[] getItems(int catId)
        {
                if(catId == 1)
                        return getAllItems();
                ShopCategory category = categories.get(catId);
                if(category == null)
                        return new ShopItem[0];
                return category.items.values().toArray(new ShopItem[category.items.size()]);
        }

        public ShopItem[] getItems(int catId, int page)
        {
                ShopItem[] list = getItems(catId);
                ShopItem[] res = new ShopItem[9];
                int n = 0;
                for(int i = page * 9; i < (page + 1) * 9; i++)
                {
                        if(i < list.length)
                        {
                                res[n] = list[i];
                                n++;
                        }
                        else
                                break;
                }
                if(n != 9)
                        res = compact(res, n);
                return res;
        }

        private ShopItem[] compact(ShopItem[] items, int size)
        {
                ShopItem[] new_items = new ShopItem[size];
                System.arraycopy(items, 0, new_items, 0, size);
                return new_items;
        }

        public ShopCategory[] getCategories()
        {
                return categories.values().toArray(new ShopCategory[categories.size()]);
        }

        public void buyItem(Player player, int id, int count)
        {
                ShopItem item = getItem(id);

                if(item == null)
                {
                        // You have failed to purchase the item.
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400105));
                        return;
                }
                if(player.shopMoney < item.price)
                {
                        // You do not have enough Cash Points.
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400108));
                        return;
                }

        if (count > player.getInventory().getNumberOfFreeSlots())
        {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_FULL_INVENTORY);
            return;
                }
                ItemService.addItem(player, item.itemId, item.count);

                decreaseAndUpdate(player, item.price);

                logPurchase(player, item.itemId);
        }

        void decreaseAndUpdate(Player player, long value)
        {
                /**
                 * param value : the credits spent
                 *
                 * Add your own code before sending credit update packet if you need.
                 * PacketSendUtility.sendPacket(player, new SM_INGAMESHOP_BALANCE());
                 *
                 */
                decreaseCredits(player, value);
        }

        void increaseAndUpdate(Player player, long value)
        {
                /**
                 * param value : the credits spent
                 *
                 * Add your own code before sending credit update packet if you need.
                 * PacketSendUtility.sendPacket(player, new SM_INGAMESHOP_BALANCE());
                 *
                 */
                increaseCredits(player, value);
        }

        public void giftItem(Player player, int id, int count, String receiver, String message)
        {
                ShopItem item = getItem(id);

                //vorerst deaktiviert.
                if (!CustomConfig.AIONSHOP_GIFT_ENABLE)
                {
                        PacketSendUtility.sendMessage(player, "Gift service is not enabled");
                        return;
                }
                if(item == null)
                {
                        // You have failed to purchase the item.
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400105));
                        return;
                }
                if(player.shopMoney < item.price)
                {
                        // You do not have enough Cash Points.
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400108));
                        return;
                }

                PlayerCommonData pcd = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(receiver);

                if(pcd == null || pcd.getRace() != player.getCommonData().getRace())
                {
                        // You have chosen an invalid target to give the gift.
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400106));
                        return;
                }

                if(player.getName() == receiver)
                {
                        // You cannot give gifts to yourself.
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400116));
                        return;
                }

                // Checks passed, we proceed to the item send.
                decreaseAndUpdate(player, item.price);

                MailService.getInstance().sendSystemMail("CASH_ITEM_MAIL", "Gift from " + player.getName(), message, pcd.getPlayerObjId(), ItemService.newItem(item.itemId, item.count, "Purchased at Black Cloud Traders Shop", pcd.getPlayerObjId(), 0, 0), 0);

                // The gift has been delivered successfully.
                PacketSendUtility.sendMessage(player, "Your gift has been delivered successfully");
              return;
           }

        public void logPurchase(Player player, int itemUniqueId)
        {
                Connection con = null;

                try
                {
                        con = DatabaseFactory.getConnection();
                        PreparedStatement stmt = con.prepareStatement("INSERT INTO " + CustomConfig.AIONSHOP_DB + ".aionshop_transactions(server_id,item_unique_id,buy_timestamp,player_id) VALUES(?,?,?,?)");
                        stmt.setInt(1, NetworkConfig.GAMESERVER_ID);
                        stmt.setInt(2, itemUniqueId);
                        stmt.setLong(3, System.currentTimeMillis() / 1000);
                        stmt.setLong(4, player.getObjectId());
                        stmt.execute();
                        con.close();
                }
                catch(Exception e)
                {
                        log.error("Cannot log purchase ! " + player.getObjectId() + " - " + itemUniqueId, e);
                }finally
                {
                        DatabaseFactory.close(con);
                }
        }

        public int getPlayerCredits(Player player)
        {
                int result = -1;
                Connection con = null;

                try
                {
                        con = DatabaseFactory.getConnection();
                        PreparedStatement stmt = con.prepareStatement("SELECT credits FROM " + CustomConfig.AIONSHOP_DB + ".account_data WHERE name = ?");
                        stmt.setString(1, player.getAcountName());
                        ResultSet rs = stmt.executeQuery();
                        if(rs.next())
                        {
                                result = rs.getInt("credits");
                        }
                        stmt.close();
                        con.close();
                        log.info("Got player credits = " + result);
                }
                catch(Exception e)
                {
                        log.error("Cannot get credits !", e);
                }finally
                {
                        DatabaseFactory.close(con);
                }

                player.shopMoney = result;

                return result;
        }

        public ShopItem[] getRankItems()
        {
                Connection con = null;
                ArrayList<ShopItem> items = new ArrayList<ShopItem>();

                try
                {
                        con = DatabaseFactory.getConnection();
                        PreparedStatement stmt = con.prepareStatement("SELECT i.*, COUNT(t.item_unique_id) as total FROM " + CustomConfig.AIONSHOP_DB + ".aionshop_items i, " + CustomConfig.AIONSHOP_DB + ".aionshop_transactions t WHERE t.item_unique_id = i.itemTemplateId GROUP BY t.item_unique_id ORDER BY total DESC LIMIT 6");
                        ResultSet rs = stmt.executeQuery();

                        while(rs.next())
                        {
                                ShopItem item = new ShopItem(rs.getInt("itemUniqueId"));
                                item.itemId = rs.getInt("itemTemplateId");
                                item.count = rs.getInt("itemCount");
                                item.price = rs.getInt("itemPrice");
                                item.name = rs.getString("itemName");
                                item.desc = rs.getString("itemDesc");
                                item.eyecatch = rs.getInt("itemEyecatch");
                                items.add(item);
                        }
                        stmt.close();
                        con.close();
                }
                catch(Exception e)
                {
                        log.error("Cannot get Rank Items !", e);
                }finally
                {
                        DatabaseFactory.close(con);
                }

                return items.toArray(new ShopItem[items.size()]);
        }

        public void decreaseCredits(Player player, long value)
        {
                player.shopMoney -= value;
                Connection con = null;

                try
                {
                        con = DatabaseFactory.getConnection();
                        PreparedStatement stmt = con.prepareStatement("UPDATE " + CustomConfig.AIONSHOP_DB + ".account_data SET credits = ? WHERE name = ?");
                        stmt.setInt(1, (int)player.shopMoney);
                        stmt.setString(2, player.getAcountName());
                        stmt.execute();
                        stmt.close();
                        log.info("Decreased " + player.getName() + "'s credits by " + value);
                }
                catch(Exception e)
                {
                        log.error("Cannot get credits !", e);
                }finally
                {
                        DatabaseFactory.close(con);
                }
        }

        public void increaseCredits(Player player, long value)
        {
                player.shopMoney += value;
                Connection con = null;

                try
                {
                        con = DatabaseFactory.getConnection();
                        PreparedStatement stmt = con.prepareStatement("UPDATE " + CustomConfig.AIONSHOP_DB + ".account_data SET credits = ? WHERE name = ?");
                        stmt.setInt(1, (int)player.shopMoney);
                        stmt.setString(2, player.getAcountName());
                        stmt.execute();
                        stmt.close();
                        log.info("Increased " + player.getName() + "'s credits by " + value);
                }
                catch(Exception e)
                {
                        log.error("Cannot set credits !", e);
                }finally
                {
                        DatabaseFactory.close(con);
                }
        }
}