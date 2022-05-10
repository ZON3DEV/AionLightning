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
package quest.custom;

import org.openaion.gameserver.configs.main.EventConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.Storage;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Rolandas
 *
 */
public class _3999ItemGiving extends QuestHandler
{

	private final static int	questId	= 3999;
	
	public _3999ItemGiving()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		// Juice
		qe.setNpcQuestData(799702).addOnTalkEvent(questId); // Laylin (elyos)
		qe.setNpcQuestData(799703).addOnTalkEvent(questId); // Ronya (asmodian)
		// Cakes
		qe.setNpcQuestData(798414).addOnTalkEvent(questId); // Brios (elyos)
		qe.setNpcQuestData(798416).addOnTalkEvent(questId); // Bothen (asmodian)
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		int itemId = 0;
		Player player = env.getPlayer();
		
		switch (env.getPlayer().getCommonData().getRace())
		{
			case ASMODIANS:
				if (env.getTargetId() == 799703)
					itemId = EventConfig.EVENT_GIVEJUICE_ASMOS;
				else if (env.getTargetId() == 798416)
					itemId = EventConfig.EVENT_GIVECAKE_ASMOS;
				break;
			case ELYOS:
				if (env.getTargetId() == 799702)
					itemId = EventConfig.EVENT_GIVEJUICE_ELYOS;
				else if (env.getTargetId() == 798414)
					itemId = EventConfig.EVENT_GIVECAKE_ELYOS;
				break;
		}
		
		if (itemId == 0)
			return false;
		
		int targetId = env.getVisibleObject().getObjectId();
		switch(env.getDialogId())
		{
			case -1:
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1011, 0));
				return true;
			case 1012:
			{
				Storage inventory = player.getInventory();
				if (inventory.getItemCountByItemId(itemId) > 0)
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1097, 0));
					return true;
				}
				else
				{
					if (defaultQuestGiveItem(env, itemId, 1))
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1012, 0));
					return true;
				}
			}
		}
		return false;
		
	}
}
