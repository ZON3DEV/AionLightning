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

import org.apache.log4j.Logger;
import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.DescriptionId;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.StaticObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.model.templates.recipe.ComboProduct;
import org.openaion.gameserver.model.templates.recipe.Component;
import org.openaion.gameserver.model.templates.recipe.RecipeTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.skill.model.SkillTemplate;
import org.openaion.gameserver.skill.task.CraftingTask;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.World;


/**
 * @author MrPoke, sphinx, HellBoy
 *
 */
public class CraftService 
{
	private static final Logger log = Logger.getLogger(CraftService.class);

	/**
	 * 
	 * @param player
	 * @param recipetemplate
	 */
	public static ItemTemplate finishCrafting(Player player, RecipeTemplate recipetemplate, int itemId)
	{
		boolean critical = false;
		boolean getReward = false;
		int critItemId = 0;
		
		if(recipetemplate.getComboProduct().size() == 0)
			getReward = true;
		else
			critical = Rnd.get(100) <= CustomConfig.CRITICAL_CRAFTING_SUCCESS;
		
		if(critical)
		{
			boolean getNext = false;
			
			for(ComboProduct comboProduct: recipetemplate.getComboProduct())
			{
				if(itemId == recipetemplate.getProductid() && critItemId == 0)
				{
					critItemId = comboProduct.getItemid();
					break;
				}
				else if(itemId == comboProduct.getItemid())
					getNext = true;
				else if(getNext)
				{
					critItemId = comboProduct.getItemid();
					break;
				}
			}
			
			if(critItemId == 0)
				getReward = true;
		}
		else
			getReward = true;
		
		if(critical && !getReward)
			return ItemService.getItemTemplate(critItemId);
		else
		{
			int productItemId = itemId;
			int skillId = recipetemplate.getSkillid();
			
			if(recipetemplate.getMaxProductionCount() == 1)
				player.getRecipeList().deleteRecipe(player, recipetemplate.getId());
			
			if(skillId == 40009)
			{
				ItemService.addItem(player, productItemId, recipetemplate.getQuantity(), player.getName(), 0, 0);
				PacketSendUtility.sendPacket(player,SM_SYSTEM_MESSAGE.STR_COMBINE_SUCCESS(new DescriptionId(ItemService.getItemTemplate(productItemId).getNameId())));
			}
			else if(productItemId != 0 && skillId != 40009)
			{
				int skillPoint = recipetemplate.getSkillpoint();
				SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillId);
				
				ItemService.addItem(player, productItemId, recipetemplate.getQuantity(), player.getName(), 0, 0);
				
				if(skillPoint + 40 > player.getSkillList().getSkillLevel(skillId))
				{
					int xpReward = (int)((0.0144*skillPoint*skillPoint+3.5*skillPoint+270)*player.getRates().getCraftingLvlRate());
					if(player.getXpBoost() > 0)
						xpReward = xpReward * ((player.getXpBoost() / 100) + 1);
	
					if(player.getSkillList().addSkillXp(player, skillId, xpReward))
					{
						player.getCommonData().addExp((int)(xpReward*player.getRates().getCraftingXPRate()/player.getRates().getCraftingLvlRate()));
						PacketSendUtility.sendPacket(player ,SM_SYSTEM_MESSAGE.STR_CRAFT_SUCCESS_GETEXP);
					}
					else
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_DONT_GET_PRODUCTION_EXP(new DescriptionId(skillTemplate.getNameId())));
				}
				else
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_DONT_GET_PRODUCTION_EXP(new DescriptionId(skillTemplate.getNameId())));
			}
			player.setCraftingTask(null);
			return null;
		}
	}

	/**
	 * 
	 * @param player
	 * @param recipeId
	 * @param targetObjId
	 */
	public static void startCrafting(Player player, int recipeId, int targetObjId)
	{
		if(player.getCraftingTask() != null && player.getCraftingTask().isInProgress())
			return;

		RecipeTemplate recipeTemplate = DataManager.RECIPE_DATA.getRecipeTemplateById(recipeId);

		if(recipeTemplate == null)
		{
			log.warn(String.format("recipeTemplate with id %d not found", recipeId));
			return;
		}
		
		// check for pre-usage crafting
		int skillId = recipeTemplate.getSkillid();
		AionObject target = World.getInstance().findAionObject(targetObjId);
		
		//morphing dont need static object/npc to use
		if((skillId != 40009) && (target == null || !(target instanceof StaticObject)))
		{
			log.info("[AUDIT] Player " + player.getName() + " tried to craft incorrect target.");
			return;
		}
		//check distance to avoid sending of fake packets
		if((skillId != 40009) && !MathUtil.isIn3dRange(player, (StaticObject)target, 10))
		{
			log.info("[AUDIT] Player " + player.getName() + " sending fake packet CM_CRAFT.");
			return;
		}
		
		if(recipeTemplate.getDp() != null && (player.getCommonData().getDp() < recipeTemplate.getDp()))
		{
			log.info("[AUDIT] Player " + player.getName() + " modded her/his client.");
			return;
		}
		
		if(player.getInventory().isFull())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.COMBINE_INVENTORY_IS_FULL);
			return;
		}
		
		for(Component component : recipeTemplate.getComponent())
		{
			if(player.getInventory().getItemCountByItemId(component.getItemid()) < component.getQuantity())
			{
				log.info("[AUDIT] Player " + player.getName() + " modded her/his client.");
				return;
			}
		}
		
		ItemTemplate itemTemplate = ItemService.getItemTemplate(recipeTemplate.getProductid());
		
		if(itemTemplate == null)
		{
			log.warn(String.format("itemTemplate with id %d not found", recipeTemplate.getProductid()));
			return;
		}
	
		if(recipeTemplate.getDp() != null)
			player.getCommonData().addDp(-recipeTemplate.getDp());
	
		for(Component component : recipeTemplate.getComponent())
		{
			if(!player.getInventory().removeFromBagByItemId(component.getItemid(), component.getQuantity()))
				return;
		}
		
		// start crafting
		player.setCraftingTask(new CraftingTask(player, (StaticObject)target, recipeTemplate, itemTemplate));
		
		player.getCraftingTask().start();
	}
}
