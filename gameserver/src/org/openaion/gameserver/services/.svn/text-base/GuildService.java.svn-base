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
package org.openaion.gameserver.services;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.DescriptionId;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.templates.GuildTemplate;
import org.openaion.gameserver.model.templates.guild.GuildQuest;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;


/**
 * @author HellBoy
 *
 */
public class GuildService
{
	private static final Logger	log	= Logger.getLogger(GuildService.class);
	
	private GuildService()
	{
		this.load();
	}
	
	public static final GuildService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private void load()
	{
		log.info("GuildService: Loaded!");
		
		scheduleUpdate();
	}
	
	/**
	 * @param player
	 * @param npc
	 * @return
	 */
	public void enterGuild(Player player, Npc npc)
	{
		int targetObjectId = npc.getObjectId();
		GuildTemplate guildTemplate = DataManager.GUILDS_DATA.getGuildTemplateByNpcId(npc.getNpcId());
		int playerGuildId = player.getGuild().getGuildId();
		int guildId = guildTemplate.getGuildId();
		
		if(player.getCommonData().getLevel() < guildTemplate.getRequiredLevel())
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1182));
			return;
		}
		if(player.getCommonData().getRace() != guildTemplate.getGuildRace())
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1097));
			return;
		}
		if(playerGuildId == guildId)
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300525));
			return;
		}
		if(playerGuildId != 0)
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1267));
			return;
		}
		if(playerGuildId == 0)
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300524, new DescriptionId(guildTemplate.getNameID()*2 + 1)));
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1012));
			player.getGuild().setGuildId(guildId);
			sendDailyQuest(player, guildTemplate);
		}
	}
	
	/**
	 * @param player
	 * @param npc
	 * @return
	 */
	public void exitGuild(Player player, Npc npc)
	{
		int targetObjectId = npc.getObjectId();
		GuildTemplate guildTemplate = DataManager.GUILDS_DATA.getGuildTemplateByNpcId(npc.getNpcId());
		int playerGuildId = player.getGuild().getGuildId();
		int currentQuest = player.getGuild().getCurrentQuest();
		int guildId = guildTemplate.getGuildId();
		if(playerGuildId == guildId)
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300526, new DescriptionId(guildTemplate.getNameID()*2 + 1)));
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1353));
			player.getGuild().setGuildId(0);
			player.getGuild().setLastQuest(0);
			
			if(currentQuest != 0)
			{
				player.getGuild().setCurrentQuest(0);
				player.getGuild().setCompleteTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(currentQuest));
				player.getController().updateNearbyQuests();
			}
		}
		else
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1438));
	}
	
	/**
	 * @param player
	 * @param guildTemplate
	 * @return
	 */
	public void sendDailyQuest(Player player, GuildTemplate guildTemplate)
	{
		int guildId = player.getGuild().getGuildId();		
		int playerLevel = player.getCommonData().getLevel();
		int currentQuest = player.getGuild().getCurrentQuest();
		int startQuestId = 0;
		
		if(currentQuest != 0)
			return;
		
		if(!timeCheck(player))
			return;
		
		switch(guildId)
		{
			case 10:
			case 20:
				if(playerLevel >= 30 && playerLevel <= 39)
					startQuestId = chooseQuestByLvl(player, guildTemplate, 30, 39);
				if(playerLevel >= 40 && playerLevel <= 49)
					startQuestId = chooseQuestByLvl(player, guildTemplate, 40, 49);
				if(playerLevel >= 50 && playerLevel <= 55)
					startQuestId = chooseQuestByLvl(player, guildTemplate, 50, 55);
				break;
			case 11:
			case 21:
				if(playerLevel >= 40 && playerLevel <= 49)
					startQuestId = chooseQuestByLvl(player, guildTemplate, 40, 49);
				if(playerLevel >= 50 && playerLevel <= 55)
					startQuestId = chooseQuestByLvl(player, guildTemplate, 50, 55);
				break;
			case 12:
			case 22:
				if(playerLevel >= 50 && playerLevel <= 52)
					startQuestId = chooseQuestByLvl(player, guildTemplate, 50, 52);
				if(playerLevel >= 53 && playerLevel <= 54)
					startQuestId = chooseQuestByLvl(player, guildTemplate, 53, 54);
				if(playerLevel == 55)
					startQuestId = chooseQuestByLvl(player, guildTemplate, 55, 55);
		}
		
		if(startQuestId != 0)
		{
			player.getGuild().setCurrentQuest(startQuestId);
			player.getGuild().setCompleteTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(6, startQuestId));
		}
	}
	
	public boolean timeCheck(Player player)
	{
		Timestamp completeTime = player.getGuild().getCompleteTime();
		
		if(completeTime == null)
			return true;
		else
		{
			String[] time = CustomConfig.DAILY_START_TIME.split(":");
			Calendar current = Calendar.getInstance();
			Calendar complete = Calendar.getInstance();
			Calendar startTime = Calendar.getInstance();
			
			complete.setTimeInMillis(completeTime.getTime());
			
			startTime.set(Calendar.YEAR, complete.get(Calendar.YEAR));
			startTime.set(Calendar.MONTH, complete.get(Calendar.MONTH));
			startTime.set(Calendar.DATE, complete.get(Calendar.DATE));
			startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
			startTime.set(Calendar.MINUTE, Integer.parseInt(time[1]));
			startTime.set(Calendar.SECOND, Integer.parseInt(time[2]));
			
			if(complete.after(startTime))
				startTime.setTimeInMillis(startTime.getTimeInMillis() + 86400000);
			
			if(complete.before(startTime) && (current.after(startTime) || current.equals(startTime)))
				return true;
		}
		
		return false;
	}
	
	private int chooseQuestByLvl(Player player, GuildTemplate guildTemplate, int lowLvl, int highLvl)
	{
		int index = 0;
		int i = 0;
		int firstQuest = 0;
		int questLvl = 0;
		int lastQuest = player.getGuild().getLastQuest();
		int curQuest = 0;
		boolean difRange = true;
		
		List<GuildQuest> guildQuests = guildTemplate.getGuildQuests().getGuildQuest();
		SortedMap<Integer, Integer> hasQuest = new TreeMap<Integer, Integer>();
		for(GuildQuest guildQuest : guildQuests)
		{
			questLvl = guildQuest.getLevel();
			if(questLvl >= lowLvl && questLvl <= highLvl)
			{
				curQuest = guildQuest.getGuildQuestId();
				if(firstQuest == 0)
					firstQuest = curQuest;
				if(lastQuest == curQuest)
					difRange = false;
				hasQuest.put(curQuest, i);
			}
			i++;
		}
		
		if(lastQuest == 0 || difRange)
			index = hasQuest.get(firstQuest);
		else
			index = hasQuest.get(lastQuest) + 1;
		
		if (index > guildQuests.size() - 1)
			index = hasQuest.get(firstQuest);

		return 	guildQuests.get(index).getGuildQuestId();
	}
	
	public void deleteDaily(Player player, int questId)
	{
		int currentQuest = player.getGuild().getCurrentQuest();
		if(questId == currentQuest)
		{
			player.getGuild().setCompleteTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			player.getGuild().setLastQuest(currentQuest);
			player.getGuild().setCurrentQuest(0);
		}
	}
	
	public void scheduleUpdate()
	{
		String[] time = CustomConfig.DAILY_START_TIME.split(":");
		Calendar start = Calendar.getInstance();
		Calendar current = Calendar.getInstance();
		
		start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
		start.set(Calendar.MINUTE, Integer.parseInt(time[1]));
		start.set(Calendar.SECOND, Integer.parseInt(time[2]));
		
		long startTime = start.getTimeInMillis();
		long currentTime = current.getTimeInMillis();
		
		if(startTime < currentTime)
			startTime = startTime + 86400000;
		
		long delay = startTime - currentTime;

		final Executor<Player> sendDaily = new Executor<Player>(){
			@Override
			public boolean run(Player player)
			{
				int guildId = player.getGuild().getGuildId();
				if(guildId != 0)
				{
					GuildTemplate guildTemplate = DataManager.GUILDS_DATA.getGuildTemplateByGuildId(guildId);
					sendDailyQuest(player, guildTemplate);
				}
				return true;
			}
		};
		
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				World.getInstance().doOnAllPlayers(sendDaily);
			}
		}, delay, 86400000);
	}

	@SuppressWarnings("synthetic-access")
	public static class SingletonHolder
	{
		protected static final GuildService instance = new GuildService();
	}
}
