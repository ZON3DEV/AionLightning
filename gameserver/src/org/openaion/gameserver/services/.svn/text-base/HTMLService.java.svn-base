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

import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.main.GSConfig;
import org.openaion.gameserver.dao.SurveyDAO;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.Survey;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUESTIONNAIRE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.idfactory.IDFactory;
import org.openaion.gameserver.world.World;

import java.util.List;


/**
 * Use this service to send raw html to the client 
 * Absolut alpha phase. Not yet tested what is allowed
 * 
 * @author lhw, ginho1
 */
public class HTMLService
{
	private static final Logger     log     = Logger.getLogger(HTMLService.class);

	/**
	 * Returns an HTML template ready to be pushed to player.
	 * @param title The title of the survey
	 * @param message The introduction message
	 * @param select_text A list of radio buttons to propose to player
	 * @param itemId An item id to give as reward
	 * @param itemCount Count of items.
	 * @return
	 */
	public static String HTMLTemplate(String title, String message, String[] select_text, int itemId, int itemCount)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("<poll>\n");
		sb.append("<poll_introduction>\n");
		sb.append("	<![CDATA[<font color='4CB1E5'>" + title + "</font>]]>\n");
		sb.append("</poll_introduction>\n");
		sb.append("<poll_title>\n");
		sb.append("	<font color='ffc519'></font>\n");
		sb.append("</poll_title>\n");
		sb.append("<start_date>2010-08-08 00:00</start_date>\n");
		sb.append("<end_date>2010-09-14 01:00</end_date>\n");
		sb.append("<servers></servers>\n");
		sb.append("<order_num></order_num>\n");
		sb.append("<race></race>\n");
		sb.append("<main_class></main_class>\n");
		sb.append("<world_id></world_id>\n");
		sb.append("<item_id>");
		sb.append(itemId);
		sb.append("</item_id>\n");
		sb.append("<item_cnt>");
		sb.append(itemCount);
		sb.append("</item_cnt>\n");
		sb.append("<level>1~55</level>\n");
		sb.append("<questions>\n");
		sb.append("	<question>\n");
		sb.append("		<title>\n");
		sb.append("			<![CDATA[\n");
		sb.append("<br><br>");
		sb.append(message);
		sb.append("<br><br><br>\n");
		sb.append("			]]>\n");
		sb.append("		</title>\n");
		sb.append("		<select>\n");
		for(String select : select_text)
		{
			sb.append("<input type='radio'>");
			sb.append(select);
			sb.append("</input>\n");
		}
		sb.append("		</select>\n");
		sb.append("	</question>\n");
		sb.append("</questions>\n");
		sb.append("</poll>\n");

		return sb.toString();
	}

	public static void onPlayerLogin(Player player)
	{
		if(player == null)
		    return;

		List<Survey> surveys = DAOManager.getDAO(SurveyDAO.class).loadSurveys(player.getObjectId());

		for(Survey survey : surveys) {
			String html = HTMLTemplate(survey.getTitle(), survey.getMessage(), new String[] { survey.getSelectText() }, survey.getItemId(), survey.getItemCount());
			sendData(player, survey.getSurveyId(), html);
		}
	}
	
	public static void onPlayerLevelUp(Player player)
	{
		if (player == null || !player.isOnline())
			return;
		
		Survey survey = DataManager.LEVEL_UP_SURVEYS.getSurvey(player);
		if (survey == null)
			return;
		
		DAOManager.getDAO(SurveyDAO.class).addSurvey(player.getObjectId(), survey.getTitle(),
			survey.getMessage(), survey.getSelectText(), survey.getItemId(), survey.getItemCount());
		
		String html = HTMLTemplate(survey.getTitle(), survey.getMessage(), new String[] { survey.getSelectText() }, survey.getItemId(), survey.getItemCount());
		showHTML(player, html);
	}

	public static void getMessage(Player player, int messageId)
	{
		if(player == null)
		    return;

		if(messageId < 1)
		    return;

		Survey survey = DAOManager.getDAO(SurveyDAO.class).loadSurvey(player.getObjectId(), messageId);
	
		if(survey != null)
		{
			if(player.getInventory().isFull())
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_INVEN_ERROR);
				return;
			}
			DAOManager.getDAO(SurveyDAO.class).deleteSurvey(survey.getSurveyId());
			ItemService.addItem(player, survey.getItemId(), survey.getItemCount(), "", survey.getItemExistTime(), survey.getItemTradeTime());
			if(GSConfig.LOG_ITEM)
				log.info(String.format("[ITEM] Item Survey ID/Count - %d/%d to player %s.", survey.getItemId(), survey.getItemCount(), player.getName()));
		}
	}

	public static void pushSurvey(final String html)
	{
		final int messageId = IDFactory.getInstance().nextId();
		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player ply)
			{
				sendData(ply, messageId, html);
				return true;
			}
		});
	}

	public static void showHTML(Player player, String html)
	{
		sendData(player, IDFactory.getInstance().nextId(), html);
	}

	private static void sendData(Player player, int messageId, String html)
	{
		byte packet_count = (byte) Math.ceil(html.length() / (Short.MAX_VALUE - 8) + 1);
		if(packet_count < 256)
		{
			for(byte i = 0; i < packet_count; i++)
			{
				try
				{
					int from = i * (Short.MAX_VALUE - 8), to = (i + 1) * (Short.MAX_VALUE - 8);
					if(from < 0)
						from = 0;
					if(to > html.length())
						to = html.length();
					String sub = html.substring(from, to);
					player.getClientConnection().sendPacket(new SM_QUESTIONNAIRE(messageId, i, packet_count, sub));
				}
				catch(Exception e)
				{
					log.error(e);
				}
			}
		}
	}
}
