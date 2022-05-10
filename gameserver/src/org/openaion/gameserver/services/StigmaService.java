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

import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.DescriptionId;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.PersistentState;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.SkillListEntry;
import org.openaion.gameserver.model.items.ItemSlot;
import org.openaion.gameserver.model.templates.item.ItemCategory;
import org.openaion.gameserver.model.templates.item.ItemType;
import org.openaion.gameserver.model.templates.item.RequireSkill;
import org.openaion.gameserver.model.templates.item.Stigma;
import org.openaion.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import org.openaion.gameserver.network.aion.serverpackets.SM_STIGMA_SKILL_REMOVE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.skill.SkillEngine;
import org.openaion.gameserver.skill.model.Skill;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author ATracer
 *
 */
public class StigmaService
{
	private static final Logger	log			= Logger.getLogger(StigmaService.class);
	/**
	 * @param resultItem
	 */
	public static boolean notifyEquipAction(Player player, Item resultItem, int slot)
	{
		if(resultItem.getItemTemplate().isStigma())
		{
			int lvl = player.getLevel();
			
			int stigmaSlotCount = (lvl/10);

			if(lvl == 55)
				stigmaSlotCount = 6;
			
			//check number of AdvancedStigmaSlots
			if (CustomConfig.ADVSTIGMA_ONLVLUP)
			{
				if (lvl < 45)
				{
					if (player.getCommonData().getAdvencedStigmaSlotSize() > 0)
					{
						log.info("[AUDIT]Possible client hack Advanced Stigma count big :O player: "+player.getName());
						player.getCommonData().setAdvencedStigmaSlotSize(0);
						return false;
					}
				}
				else if (lvl < 50)
				{
					if (player.getCommonData().getAdvencedStigmaSlotSize() > 2)
					{
						log.info("[AUDIT]Possible client hack Advanced Stigma count big :O player: "+player.getName());
						player.getCommonData().setAdvencedStigmaSlotSize(2);
						return false;
					}
				}
				else if (lvl < 52)
				{
					if (player.getCommonData().getAdvencedStigmaSlotSize() > 3)
					{
						log.info("[AUDIT]Possible client hack Advanced Stigma count big :O player: "+player.getName());
						player.getCommonData().setAdvencedStigmaSlotSize(3);
						return false;
					}
				}
				else if (lvl < 55)
				{
					if (player.getCommonData().getAdvencedStigmaSlotSize() > 4)
					{
						log.info("[AUDIT]Possible client hack Advanced Stigma count big :O player: "+player.getName());
						player.getCommonData().setAdvencedStigmaSlotSize(4);
						return false;
					}
				}
				else if (lvl == 55)
				{
					if (player.getCommonData().getAdvencedStigmaSlotSize() > 5)
					{
						log.info("[AUDIT]Possible client hack Advanced Stigma count big :O player: "+player.getName());
						player.getCommonData().setAdvencedStigmaSlotSize(5);
						return false;
					}
				}
			}
			else
			{
				if (player.getCommonData().getAdvencedStigmaSlotSize() > 5)
				{
					log.info("[AUDIT]Possible client hack Advanced Stigma count big :O player: "+player.getName());
					player.getCommonData().setAdvencedStigmaSlotSize(5);
					return false;
				}
			}
			
			//check if stigma slot is already full
			if (player.getEquipment().getEquippedItemBySlot(slot) != null)
			{
				//cannot equip stigma on this slot
				return false;
			}
			
			//decide whether player is trying to equip to adv or normal stigma slot
			boolean advSlot = false;
			if ((slot & ItemSlot.ADV_STIGMAS.getSlotIdMask()) != 0)
				advSlot = true;
			
			//check each stigma slots independent
			if (advSlot)
			{
				if (player.getEquipment().getEquippedItemsAdvStigma().size() >= player.getCommonData().getAdvencedStigmaSlotSize())
				{
					log.info("[AUDIT]Possible client hack doesnt have enough adv stigma slots player: "+player.getName());
					return false;
				}
			}
			else
			{
				if (player.getEquipment().getEquippedItemsNormalStigma().size() >= stigmaSlotCount)
				{
					log.info("[AUDIT]Possible client hack doesnt have enough normal stigma slots player: "+player.getName());
					return false;
				}
			}
			
			//check if trying to equip adv stigma into normalstigmaslot
			if (resultItem.getItemTemplate().getItemCategory() == ItemCategory.ENHANCED && resultItem.getItemTemplate().getItemType() == ItemType.ABYSS && !advSlot)
			{
				log.info("[AUDIT]Possible client hack trying to equip adv. stigma into normal stigma slot. player: "+player.getName());
				return false;
			}

			if (!resultItem.getItemTemplate().checkClassRestrict(player.getCommonData().getPlayerClass()))
			{
				log.info("[AUDIT]Possible client hack not valid for class. player: "+player.getName());
				return false;
			}

			Stigma stigmaInfo = resultItem.getItemTemplate().getStigma();
			
			if(stigmaInfo == null)
			{
				log.warn("Stigma info missing for item: " + resultItem.getItemTemplate().getTemplateId());
				return false;
			}
			
			int skillId = stigmaInfo.getSkillid();
			int shardCount = stigmaInfo.getShard();
			if (player.getInventory().getItemCountByItemId(141000001) < shardCount)
			{
				log.info("[AUDIT]Possible client hack stigma shard count low player: "+player.getName());
				return false;
			}
			int needSkill = stigmaInfo.getRequireSkill().size();
			for (RequireSkill rs : stigmaInfo.getRequireSkill())
			{
				for (int id : rs.getSkillId())
				{
					if (player.getSkillList().isSkillPresent(id))
					{
						needSkill--;
						break;
					}
				}
			}
			if (needSkill != 0)
			{
				log.info("[AUDIT]Possible client hack advenced stigma skill player: "+player.getName());
				return false;
			}

			boolean removeResult = player.getInventory().removeFromBagByItemId(141000001, shardCount);
			if(!removeResult)
				return false;
			
			SkillListEntry skill = new SkillListEntry(skillId, true, stigmaInfo.getSkilllvl(), PersistentState.NOACTION);
			player.getSkillList().addSkill(skill);
			PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(skill, 1300401));
			
			//start effect for dual-wielding stigmas
			if (skillId == 19 || skillId == 360)
			{
				Skill sk = SkillEngine.getInstance().getSkill(player, skillId, stigmaInfo.getSkilllvl(), player);
				if(sk != null)
					sk.useSkill();
			}
		
		}
		return true;
	}
	
	/**
	 * @param resultItem
	 */
	public static boolean notifyUnequipAction(Player player, Item resultItem)
	{
		if(resultItem.getItemTemplate().isStigma())
		{
			
			Stigma stigmaInfo = resultItem.getItemTemplate().getStigma();
			int skillId = stigmaInfo.getSkillid();
			
			//trying to remove dual/wielding stigma
			if (skillId == 19 || skillId == 360)
			{
				if (player.getEquipment().isDualWieldEquipped())
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_STIGMA_CANNT_UNEQUIP_STONE_FIRST_UNEQUIP_CURRENT_EQUIPPED_ITEM());
					return false;
				}
			}
			for (Item item : player.getEquipment().getEquippedItemsStigma())
			{
				Stigma si = item.getItemTemplate().getStigma();
				if (resultItem == item || si == null)
					continue;
				for (RequireSkill rs : si.getRequireSkill())
				{
					if (rs.getSkillId().contains(skillId))
					{
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300410, new DescriptionId(resultItem.getItemTemplate().getNameId()), new DescriptionId(item.getItemTemplate().getNameId())));
						return false;
					}
				}
			}
			
			//remove efect for dual-wielding stigma stones
			if (skillId == 19 || skillId == 360)
				player.getEffectController().removePassiveEffect(skillId);
			
			player.getSkillList().removeSkill(skillId);
			int nameId = DataManager.SKILL_DATA.getSkillTemplate(skillId).getNameId();
		  PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(resultItem, false));
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300403, new DescriptionId(nameId)));
			PacketSendUtility.sendPacket(player, new SM_STIGMA_SKILL_REMOVE(skillId));
		}
		return true;
	}
	
	/**
	 * 
	 * @param player
	 */
	public static void onPlayerLogin(Player player)
	{
		List<Item> equippedItems = player.getEquipment().getEquippedItemsStigma();
		for(Item item : equippedItems)
		{
			if(item.getItemTemplate().isStigma())
			{
				Stigma stigmaInfo = item.getItemTemplate().getStigma();
				
				if(stigmaInfo == null)
				{
					log.warn("Stigma info missing for item: " + item.getItemTemplate().getTemplateId());
					return;
				}
				int skillId = stigmaInfo.getSkillid();
				SkillListEntry skill = new SkillListEntry(skillId, true, stigmaInfo.getSkilllvl(), PersistentState.NOACTION);
				player.getSkillList().addSkill(skill);
				
				//start effect for dual-wielding stigmas
				if (skillId == 19 || skillId == 360)
				{
					Skill sk = SkillEngine.getInstance().getSkill(player, skillId, stigmaInfo.getSkilllvl(), player);
					if(sk != null)
						sk.useSkill();
				}
			}
		}

		for(Item item : equippedItems)
		{
			if(item.getItemTemplate().isStigma())
			{
				int currentStigmaCount = player.getEquipment().getEquippedItemsStigma().size();
				
				int lvl = player.getLevel();

				int stigmaSlotCount = (lvl/10);

				if(lvl == 55)
					stigmaSlotCount = 6;
				
				//check number of AdvancedStigmaSlots
				if (CustomConfig.ADVSTIGMA_ONLVLUP)
				{
					if (lvl < 45)
					{
						if (player.getCommonData().getAdvencedStigmaSlotSize() > 0)
						{
							log.info("[AUDIT]Possible client hack Advanced Stigma count big :O player: "+player.getName());
							player.getCommonData().setAdvencedStigmaSlotSize(0);
						}
					}
					else if (lvl < 50)
					{
						if (player.getCommonData().getAdvencedStigmaSlotSize() > 2)
						{
							log.info("[AUDIT]Possible client hack Advanced Stigma count big :O player: "+player.getName());
							player.getCommonData().setAdvencedStigmaSlotSize(2);
						}
					}
					else if (lvl < 52)
					{
						if (player.getCommonData().getAdvencedStigmaSlotSize() > 3)
						{
							log.info("[AUDIT]Possible client hack Advanced Stigma count big :O player: "+player.getName());
							player.getCommonData().setAdvencedStigmaSlotSize(3);
						}
					}
					else if (lvl < 55)
					{
						if (player.getCommonData().getAdvencedStigmaSlotSize() > 4)
						{
							log.info("[AUDIT]Possible client hack Advanced Stigma count big :O player: "+player.getName());
							player.getCommonData().setAdvencedStigmaSlotSize(4);
						}
					}
					else if (lvl == 55)
					{
						if (player.getCommonData().getAdvencedStigmaSlotSize() > 5)
						{
							log.info("[AUDIT]Possible client hack Advanced Stigma count big :O player: "+player.getName());
							player.getCommonData().setAdvencedStigmaSlotSize(5);
						}
					}
				}
				else
				{
					if (player.getCommonData().getAdvencedStigmaSlotSize() > 5)
					{
						log.info("[AUDIT]Possible client hack Advanced Stigma count big :O player: "+player.getName());
						player.getCommonData().setAdvencedStigmaSlotSize(5);
					}
				}
					
				
				if ((stigmaSlotCount + player.getCommonData().getAdvencedStigmaSlotSize()) < currentStigmaCount)
				{
					log.info("[AUDIT]Possible client hack stigma count big :O player: "+player.getName());
					player.getEquipment().unEquipItem(item.getObjectId(), 0);
					continue;
				}
				
				Stigma stigmaInfo = item.getItemTemplate().getStigma();
				
				if(stigmaInfo == null)
				{
					log.warn("Stigma info missing for item: " + item.getItemTemplate().getTemplateId());
					player.getEquipment().unEquipItem(item.getObjectId(), 0);
					continue;
				}
				
				int needSkill = stigmaInfo.getRequireSkill().size();
				for (RequireSkill rs : stigmaInfo.getRequireSkill())
				{
					for(int id : rs.getSkillId())
					{
						if (player.getSkillList().isSkillPresent(id))
						{
							needSkill--;
							break;
						}
					}
				}
				if (needSkill != 0)
				{
					log.info("[AUDIT]Possible client hack advenced stigma skill player: "+player.getName());
					player.getEquipment().unEquipItem(item.getObjectId(), 0);
					continue;
				}
				if (item.getItemTemplate().checkClassRestrict(player.getCommonData().getPlayerClass()) == false)
				{
					log.info("[AUDIT]Possible client hack not valid for class. player: "+player.getName());
					player.getEquipment().unEquipItem(item.getObjectId(), 0);
					continue;
				}
			}
		}
	}

}
