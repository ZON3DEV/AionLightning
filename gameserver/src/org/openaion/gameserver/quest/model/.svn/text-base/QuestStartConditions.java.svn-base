/**
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
package org.openaion.gameserver.quest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.gameobjects.player.QuestStateList;


/**
 * @author Rolandas
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestStartConditions")
public class QuestStartConditions
{
	@XmlElement(name = "condition")
	protected List<QuestStartCondition> conditions;

	public List<QuestStartCondition> getConditions() {
		if (conditions == null) {
			conditions = new ArrayList<QuestStartCondition>();
		}
		return this.conditions;
	}

	public static class Finished implements IQuestConditionChecker
	{
		@Override
		public boolean checkFailure(QuestState questState, QuestStep questStep)
		{
			return questState == null || questState.getStatus() != QuestStatus.COMPLETE ||
				   questStep.rewardNo != 0 && questStep.rewardNo != questState.getQuestVarById(0);
		}
	}

	public static class Unfinished implements IQuestConditionChecker
	{
		@Override
		public boolean checkFailure(QuestState questState, QuestStep questStep)
		{
			return questState != null && questState.getStatus() == QuestStatus.COMPLETE;
		}
	}

	public static class Acquired implements IQuestConditionChecker
	{
		@Override
		public boolean checkFailure(QuestState questState, QuestStep questStep)
		{
			return questState == null || questState.getStatus() == QuestStatus.NONE ||
				   questState.getStatus() == QuestStatus.LOCKED;
		}
	}

	public static class NoAcquired implements IQuestConditionChecker
	{
		@Override
		public boolean checkFailure(QuestState questState, QuestStep questStep)
		{
			return questState != null && (questState.getStatus() == QuestStatus.START ||
				questState.getStatus() == QuestStatus.REWARD);
		}
	}

	public boolean Check(QuestStateList questStateList, IQuestConditionChecker delegate)
	{
		if (this.conditions.size() == 0)
			return true;
		
		boolean matchedAny = false;
		for(QuestStartCondition conditions : this.conditions)
		{
			boolean matchedAll = true;
			for (QuestStep step : conditions.getQuests())
			{
				QuestState qs = questStateList.getQuestState(step.getQuestId());
				matchedAll &= !delegate.checkFailure(qs, step);
			}
			if (matchedAll) 
			{
				matchedAny = true;
				break;
			}
		}
		return matchedAny;
	}
	
	public List<Integer> Verify(QuestStateList questStateList, IQuestConditionChecker delegate)
	{
		List<Integer> failedQuests = new ArrayList<Integer>();
		if (this.conditions.size() == 0)
			return failedQuests;
		
		for(QuestStartCondition conditions : this.conditions)
		{
			for (QuestStep step : conditions.getQuests())
			{
				QuestState qs = questStateList.getQuestState(step.getQuestId());
				boolean matched = !delegate.checkFailure(qs, step);
				if (!matched)
					failedQuests.add(step.getQuestId());
			}
		}
		return failedQuests;		
	}

}
