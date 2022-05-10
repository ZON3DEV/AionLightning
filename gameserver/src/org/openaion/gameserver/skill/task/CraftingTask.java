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
package org.openaion.gameserver.skill.task;

import org.openaion.gameserver.model.gameobjects.StaticObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.model.templates.recipe.RecipeTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_CRAFT_ANIMATION;
import org.openaion.gameserver.network.aion.serverpackets.SM_CRAFT_UPDATE;
import org.openaion.gameserver.services.CraftService;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke, HellBoy
 *
 */
public class CraftingTask extends AbstractCraftTask
{
	private RecipeTemplate recipeTemplate;
	private ItemTemplate itemTemplate;
	private int skillId;

	/**
	 * @param requestor
	 * @param responder
	 * @param recipeTemplate
	 */

	public CraftingTask(Player requestor, StaticObject responder, RecipeTemplate recipeTemplate, ItemTemplate itemTemplate)
	{
		super(requestor, responder, recipeTemplate.getSkillid() == 40009 ? 99999 :requestor.getSkillList().getSkillLevel(recipeTemplate.getSkillid())-recipeTemplate.getSkillpoint());
		this.recipeTemplate = recipeTemplate;
		this.itemTemplate = itemTemplate;
		this.skillId = recipeTemplate.getSkillid();
	}

	/* (non-Javadoc)
	 * @see org.openaion.gameserver.skill.task.AbstractCraftTask#onFailureFinish()
	 */
	@Override
	protected void onFailureFinish()
	{
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, currentSuccessValue, currentFailureValue, 6));
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(),responder.getObjectId(), 0, 3), true);
	}

	/* (non-Javadoc)
	 * @see org.openaion.gameserver.skill.task.AbstractCraftTask#onSuccessFinish()
	 */
	@Override
	protected boolean onSuccessFinish()
	{
		ItemTemplate critItemTemplate = CraftService.finishCrafting(requestor, recipeTemplate, itemTemplate.getTemplateId());
		
		if(critItemTemplate == null)
		{
			PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, currentSuccessValue, currentFailureValue, 5));
			PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(),responder.getObjectId(), 0, 2), true);
			return true;
		}
		else
		{
			this.itemTemplate = critItemTemplate;
			PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, 0, 0, 3));
			onInteractionStart();
			currentSuccessValue = 0;
			currentFailureValue = 0;
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.openaion.gameserver.skill.task.AbstractCraftTask#sendInteractionUpdate()
	 */
	@Override
	protected void sendInteractionUpdate()
	{
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, currentSuccessValue, currentFailureValue, speedUp? 2:1));
	}

	/* (non-Javadoc)
	 * @see org.openaion.gameserver.skill.task.AbstractInteractionTask#onInteractionAbort()
	 */
	@Override
	protected void onInteractionAbort()
	{
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, 0, 0, 4));
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(),responder.getObjectId(), 0, 2), true);
	}

	/* (non-Javadoc)
	 * @see org.openaion.gameserver.skill.task.AbstractInteractionTask#onInteractionFinish()
	 */
	@Override
	protected void onInteractionFinish()
	{
	}

	/* (non-Javadoc)
	 * @see org.openaion.gameserver.skill.task.AbstractInteractionTask#onInteractionStart()
	 */
	@Override
	protected void onInteractionStart()
	{
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, 100, 100, 0));
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, 0, 0, 1));
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(),responder.getObjectId(), skillId, 0), true);
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(),responder.getObjectId(), skillId, 1), true);
	}
}
