/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.poeta;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.world.zone.ZoneName;


/**
 * @author MrPoke
 *
 */
public class _1123WheresTutty extends QuestHandler
{
	private final static int	questId	= 1123;
	
	public _1123WheresTutty()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(790001).addOnTalkEvent(questId);
		qe.setNpcQuestData(790001).addOnQuestStart(questId);
		qe.setQuestEnterZone(ZoneName.Q1123).add(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(defaultQuestNoneDialog(env, 790001))
			return true;
		if(qs == null)
			return false;
		return defaultQuestRewardDialog(env, 790001, 1352);
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(zoneName != ZoneName.Q1123)
			return false;
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		defaultQuestMovie(env, 11);
		qs.setStatus(QuestStatus.REWARD);
		updateQuestStatus(env);
		return true;
	}
}
