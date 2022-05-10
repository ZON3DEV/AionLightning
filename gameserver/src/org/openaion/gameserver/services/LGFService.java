package org.openaion.gameserver.services;

import java.util.HashMap;
import java.util.Collection;

import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.LFGApplyGroup;
import org.openaion.gameserver.model.gameobjects.LFGRecruitGroup;
import org.openaion.gameserver.model.gameobjects.player.Player;


/**
 *
 * @author ginho1
 */
public class LGFService
{
	private HashMap<Integer, LFGApplyGroup> asmosApplyGroup = new HashMap<Integer,LFGApplyGroup>();
	private HashMap<Integer, LFGApplyGroup> elyosApplyGroup = new HashMap<Integer,LFGApplyGroup>();
	private HashMap<Integer, LFGRecruitGroup> asmosRecruitGroup = new HashMap<Integer,LFGRecruitGroup>();
	private HashMap<Integer, LFGRecruitGroup> elyosRecruitGroup = new HashMap<Integer,LFGRecruitGroup>();

	public boolean addAsmosApplyGroup(int playerID, String applyString, int groupType, Player player)
	{
		if(asmosApplyGroup.containsKey(playerID))
			return false;

		LFGApplyGroup playerApplyGroup = new LFGApplyGroup(player, applyString, groupType, System.currentTimeMillis());

		asmosApplyGroup.put(playerID, playerApplyGroup);

		return true;
	}

	public boolean addElyosApplyGroup(int playerID, String applyString, int groupType, Player player)
	{
		if(elyosApplyGroup.containsKey(playerID))
			return false;

		LFGApplyGroup playerApplyGroup = new LFGApplyGroup(player, applyString, groupType, System.currentTimeMillis());

		elyosApplyGroup.put(playerID, playerApplyGroup);

		return true;
	}

	public Collection<LFGApplyGroup> getAsmosApplyGroup()
	{
		return asmosApplyGroup.values();
	}

	public Collection<LFGApplyGroup> getElyosApplyGroup()
	{
		return elyosApplyGroup.values();
	}

	public boolean addApplyGroup(int playerID, String applyString, int groupType, Player player)
	{
		if(player.getCommonData().getRace() == Race.ASMODIANS)
			return LGFService.getInstance().addAsmosApplyGroup(playerID, applyString, groupType, player);

		return LGFService.getInstance().addElyosApplyGroup(playerID, applyString, groupType, player);
	}

	public Collection<LFGApplyGroup> geApplyGroup(Race race)
	{
		if(race == Race.ASMODIANS)
			return getAsmosApplyGroup();

		return getElyosApplyGroup();
	}

	public void removeApplyGroup(int playerID)
	{
		if(asmosApplyGroup.containsKey(playerID))
			asmosApplyGroup.remove(playerID);

		if(elyosApplyGroup.containsKey(playerID))
			elyosApplyGroup.remove(playerID);
	}

	public boolean addAsmosRecruitGroup(int playerID, String applyString, int groupType, int maxLevel, Player player)
	{
		if(asmosRecruitGroup.containsKey(playerID))
			return false;

		LFGRecruitGroup playerRecruitGroup = new LFGRecruitGroup(player, applyString, groupType, maxLevel, System.currentTimeMillis());

		asmosRecruitGroup.put(playerID, playerRecruitGroup);

		return true;
	}

	public boolean addElyosRecruitGroup(int playerID, String applyString, int groupType, int maxLevel, Player player)
	{
		if(elyosRecruitGroup.containsKey(playerID))
			return false;

		LFGRecruitGroup playerRecruitGroup = new LFGRecruitGroup(player, applyString, groupType, maxLevel, System.currentTimeMillis());

		elyosRecruitGroup.put(playerID, playerRecruitGroup);

		return true;
	}

	public Collection<LFGRecruitGroup> getAsmosRecruitGroup()
	{
		for(int key : asmosRecruitGroup.keySet())
		{
			LFGRecruitGroup rg = asmosRecruitGroup.get(key);

			if((System.currentTimeMillis() - rg.getCreationTime()) < 3600000)
			{
				Player pl = rg.getPlayer();

				if(!pl.isOnline() || !pl.isInGroup())
					asmosRecruitGroup.remove(key);
			}else
				asmosRecruitGroup.remove(key);

		}

		return asmosRecruitGroup.values();
	}

	public Collection<LFGRecruitGroup> getElyosRecruitGroup()
	{
		for(int key : elyosRecruitGroup.keySet())
		{
			LFGRecruitGroup rg = elyosRecruitGroup.get(key);

			if((System.currentTimeMillis() - rg.getCreationTime()) < 3600000)
			{
				Player pl = rg.getPlayer();

				if(!pl.isOnline() || !pl.isInGroup())
					elyosRecruitGroup.remove(key);
			}else
				elyosRecruitGroup.remove(key);

		}

		return elyosRecruitGroup.values();
	}

	public boolean addRecruitGroup(int playerID, String applyString, int groupType, int maxLevel, Player player)
	{
		if(player.getCommonData().getRace() == Race.ASMODIANS)
			return LGFService.getInstance().addAsmosRecruitGroup(playerID, applyString, groupType, maxLevel, player);

		return LGFService.getInstance().addElyosRecruitGroup(playerID, applyString, groupType, maxLevel, player);
	}

	public Collection<LFGRecruitGroup> geRecruitGroup(Race race)
	{
		if(race == Race.ASMODIANS)
			return getAsmosRecruitGroup();

		return getElyosRecruitGroup();
	}

	public void removeRecruitGroup(int playerID)
	{
		if(asmosRecruitGroup.containsKey(playerID))
			asmosRecruitGroup.remove(playerID);

		if(elyosRecruitGroup.containsKey(playerID))
			elyosRecruitGroup.remove(playerID);
	}

	public static LGFService getInstance()
	{
		return SingletonHolder.instance;
	}

	private static class SingletonHolder
	{
		public static LGFService instance = new LGFService();
	}
}
