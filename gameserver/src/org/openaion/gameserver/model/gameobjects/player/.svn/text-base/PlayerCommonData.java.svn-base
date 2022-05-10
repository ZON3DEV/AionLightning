/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.model.gameobjects.player;

import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.openaion.gameserver.GameServer;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.configs.main.GSConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.StaticData;
import org.openaion.gameserver.model.Gender;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.templates.VisibleObjectTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_ABYSS_RANK;
import org.openaion.gameserver.network.aion.serverpackets.SM_ABYSS_RANK_UPDATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_DP_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_LEGION_EDIT;
import org.openaion.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_STATUPDATE_DP;
import org.openaion.gameserver.network.aion.serverpackets.SM_STATUPDATE_EXP;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.HTMLService;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.SkillTemplate;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.stats.XPLossEnum;
import org.openaion.gameserver.world.World;
import org.openaion.gameserver.world.WorldPosition;


/**
 * This class is holding base information about player, that may be used even when player itself is not online.
 * 
 * @author Luno
 * 
 */
public class PlayerCommonData extends VisibleObjectTemplate
{
	/** Logger used by this class and {@link StaticData} class */
	static Logger			log						= Logger.getLogger(PlayerCommonData.class);

	private final int		playerObjId;
	private Race			race;
	private String			name;
	private PlayerClass		playerClass;
	/** Should be changed right after character creation **/
	private int				level					= 0;
	private long			exp						= 0;
	private long			expRecoverable			= 0;
	private long			repletionstate;
	private Gender			gender;
	private Timestamp		lastOnline;
	private boolean			online;
	private String			note;
	private WorldPosition	position;
	private int				cubeSize				= 0;
	private int				warehouseSize			= 0;
	private int				advencedStigmaSlotSize	= 0;
	private int				bindPoint;
	private int				titleId					= -1;
	private int				dp						= 0;
	private int				mailboxLetters;

	

	// TODO: Move all function to playerService or Player class.
	public PlayerCommonData(int objId)
	{
		this.playerObjId = objId;
	}

	public int getPlayerObjId()
	{
		return playerObjId;
	}

	public long getExp()
	{
		return this.exp;
	}

	public int getCubeSize()
	{
		return this.cubeSize;
	}

	public void setCubesize(int cubeSize)
	{
		this.cubeSize = cubeSize;
	}

	/**
	 * @return the advencedStigmaSlotSize
	 */
	public int getAdvencedStigmaSlotSize()
	{
		return advencedStigmaSlotSize;
	}

	/**
	 * @param advencedStigmaSlotSize
	 *            the advencedStigmaSlotSize to set
	 */
	public void setAdvencedStigmaSlotSize(int advencedStigmaSlotSize)
	{
		this.advencedStigmaSlotSize = advencedStigmaSlotSize;
	}

	public long getExpShown()
	{
		return this.exp - DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level);
	}

	public long getExpNeed()
	{
		if(this.level == DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel())
		{
			return 0;
		}
		return DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level + 1)
			- DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level);
	}

	/**
	 * calculate the lost experience must be called before setexp
	 * 
	 * @author Jangan
	 */
	public void calculateExpLoss()
	{
		long expLost = XPLossEnum.getExpLoss(this.level, this.getExpNeed()); // This Calculates all the exp lost when
																				// dieing.
		int unrecoverable = (int) (expLost * 0.33333333); // This is 1000% Correct
		int recoverable = (int) expLost - unrecoverable;// This is 1000% Correct
		long allExpLost = recoverable + this.expRecoverable; // lol some crack headed formula ???

		// This loops states that if the unrecoverable exp is bigger than your current exp
		// we delete all your exp and go back to 0 pretty much.
		if(this.getExpShown() > unrecoverable)
		{
			this.exp = this.exp - unrecoverable;
		}
		else
		{
			this.exp = this.exp - this.getExpShown();
		}
		if(this.getExpShown() > recoverable)
		{
			this.expRecoverable = allExpLost;
			this.exp = this.exp - recoverable;
		}
		else
		{
			this.expRecoverable = this.expRecoverable + this.getExpShown();
			this.exp = this.exp - this.getExpShown();
		}
		if(this.getPlayer() != null)
			PacketSendUtility.sendPacket(this.getPlayer(), new SM_STATUPDATE_EXP(this.getExpShown(), this
				.getExpRecoverable(), this.getExpNeed()));
	}

	public void setRecoverableExp(long expRecoverable)
	{
		this.expRecoverable = expRecoverable;
	}

	public void resetRecoverableExp()
	{
		long el = this.expRecoverable;
		this.expRecoverable = 0;
		this.setExp(this.exp + el);
	}

	public long getExpRecoverable()
	{
		return this.expRecoverable;
	}

    public boolean isXPGainOn() {
        if (CustomConfig.PLAYER_EXPERIENCE_CONTROL && getPlayer().isNoExperienceGain()) {
            return false;
        } else {
        	return true;
        }
    }

	/**
	 * 
	 * @param value
	 */
	public void addExp(long value)
	{
        if (!isXPGainOn()) return;
		addExp(value, null);
	}

	/**
	 * @param value
	 *            The gained exp.
	 * @param fromObject
	 *            The object that gave the exp.
	 */
	public void addExp(long value, VisibleObject fromObject)
	{
		if(getPlayer() == null)
			return;

		/**
		 * Energy of Repose
		 */
		long repletionBonus = 0;
		if(this.getRepletionState() > 0)
		{
			repletionBonus = (value / 100) * (getPlayer().getCommonData().getLevel() < 45 ? 40 : 30);
			long finalState = this.getRepletionState() - repletionBonus;
			this.setRepletionState(finalState);
		}

		value += repletionBonus;

		if(CustomConfig.PLAYER_EXPERIENCE_CONTROL && getPlayer().isNoExperienceGain())
		{
			value = 0;
		}
        if (!isXPGainOn()) return;
        value = (value < 0) ? Long.MAX_VALUE : value;

		this.setExp(this.exp + value);
		if(fromObject == null || fromObject.getObjectTemplate() == null)
		{
			if(repletionBonus == 0)
				PacketSendUtility.sendPacket(this.getPlayer(), SM_SYSTEM_MESSAGE.EXP(Long.toString(value)));
			else
				PacketSendUtility
					.sendPacket(this.getPlayer(), SM_SYSTEM_MESSAGE.EXP(value, (int) repletionBonus, true));
		}
		else
		{
			if(repletionBonus == 0)
				PacketSendUtility.sendPacket(this.getPlayer(), SM_SYSTEM_MESSAGE.EXP(value, fromObject
					.getObjectTemplate().getNameId()));
			else
				PacketSendUtility.sendPacket(this.getPlayer(), SM_SYSTEM_MESSAGE.EXP(value, fromObject
					.getObjectTemplate().getNameId(), (int) repletionBonus));
		}
	}

	/**
	 * sets the exp value
	 * 
	 * @param exp
	 */
	public void setExp(long exp)
	{
        if (exp < 0) return;

		// maxLevel is 51 but in game 50 should be shown with full XP bar
		int maxLevel = DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel();

		if(getPlayerClass() != null && getPlayerClass().isStartingClass())
			maxLevel = 10;

		long maxExp = DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(maxLevel);
		int level = 1;

		if(exp > maxExp)
		{
			exp = maxExp;
		}

		// make sure level is never larger than maxLevel-1
		while((level + 1) != maxLevel && exp >= DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(level + 1))
		{
			level++;
		}

		if(level != this.level)
		{
			if(GSConfig.FACTIONS_RATIO_LIMITED && getPlayer() != null)
			{
				if(level > this.level && level >= GSConfig.FACTIONS_RATIO_LEVEL
					&& getPlayer().getPlayerAccount().getNumberOf(getRace()) == 1)
				{
					GameServer.updateRatio(getRace(), 1);
				}

				if(level < this.level && this.level >= GSConfig.FACTIONS_RATIO_LEVEL
					&& getPlayer().getPlayerAccount().getNumberOf(getRace()) == 1)
				{
					GameServer.updateRatio(getRace(), -1);
				}
			}

			this.level = level;
			this.exp = exp;

			if(this.getPlayer() != null)
			{
				upgradePlayer();
				HTMLService.onPlayerLevelUp(this.getPlayer());
			}
		}
		else
		{
			this.exp = exp;

			if(this.getPlayer() != null)
			{
				PacketSendUtility.sendPacket(this.getPlayer(), new SM_STATUPDATE_EXP(this.getExpShown(), this
					.getExpRecoverable(), this.getExpNeed()));
			}
		}
	}

	/**
	 * Do necessary player upgrades on level up
	 */
	public void upgradePlayer()
	{
		Player player = this.getPlayer();
		if(player != null)
		{
			player.getController().upgradePlayer(level);
		}
	}

	public void addAp(int value)
	{
		Player player = this.getPlayer();

		if(player == null)
			return;

		AbyssRank rank = player.getAbyssRank();

		// Notify player of AP gained (This should happen before setAp happens.)
		// TODO: Find System Message for "You have lost %d Abyss Points." (Lost instead of Gained)
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.EARNED_ABYSS_POINT(String.valueOf(value)));

		// Set the new AP value
		this.setAp(rank.getAp() + value);

		// add ap to daily and week ap
		rank.addDWAp(value);

		// Add Abyss Points to Legion
		if(player.isLegionMember() && value > 0)
		{
			player.getLegion().addContributionPoints(value);
			PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_EDIT(0x03, player.getLegion()));
		}
	}

	public void setAp(int value)
	{
		Player player = this.getPlayer();

		if(player == null)
			return;

		AbyssRank rank = player.getAbyssRank();

		int oldAbyssRank = rank.getRank().getId();

		rank.setAp(value);

		if(rank.getRank().getId() != oldAbyssRank)
		{
			PacketSendUtility.broadcastPacket(player, new SM_ABYSS_RANK_UPDATE(player));

			updateAbyssSkills(player, rank);

			// Apparently we are not in our own known list... so we must tell ourselves as well
			PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK_UPDATE(player));

			// remove abyss transformation
			if(rank.getRank().getId() < 14)
			{
				for(Effect ef : player.getEffectController().getAbnormalEffects())
				{
					if(ef.isAvatar())
					{
						player.getEffectController().removeEffect(ef.getSkillId());
						player.getEffectController().removeEffect(ef.getLaunchSkillId());
						break;
					}
				}
			}

		}
		PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK(player.getAbyssRank()));
	}

	public void updateAbyssSkills(Player player, AbyssRank rank)
	{
		// Update abyss skills
		for(SkillListEntry sle : player.getSkillList().getAllSkills())
		{
			SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(sle.getSkillId());
			if(skillTemplate == null)
				continue;
			if(skillTemplate.getStack().contains("ABYSS_RANKERSKILL"))
			{
				player.getSkillList().removeSkill(skillTemplate.getSkillId());
			}
		}
		switch(rank.getRank().getId())
		{
			case 14:
				if(this.getRace().getRaceId() == 0)
					player.getSkillList().addSkill(player, 11885, 1, false);// trans I elyos
				else
					player.getSkillList().addSkill(player, 11890, 1, false);// trans I asmo
				player.getSkillList().addSkill(player, 11895, 1, false);// abyssal fury I
				break;
			case 15:
				if(this.getRace().getRaceId() == 0)
				{
					player.getSkillList().addSkill(player, 11886, 1, false);// trans II elyos
					player.getSkillList().addSkill(player, 11899, 1, false);// summon abyssal energy I elyos
				}
				else
				{
					player.getSkillList().addSkill(player, 11891, 1, false);// trans II asmo
					player.getSkillList().addSkill(player, 11901, 1, false);// summon abyssal energy I asmo
				}
				player.getSkillList().addSkill(player, 11896, 1, false);// abyssal fury II
				break;
			case 16:
				if(this.getRace().getRaceId() == 0)
				{
					player.getSkillList().addSkill(player, 11887, 1, false);// trans III elyos
					player.getSkillList().addSkill(player, 11899, 1, false);// summon abyssal energy I elyos
				}
				else
				{
					player.getSkillList().addSkill(player, 11892, 1, false);// trans III asmo
					player.getSkillList().addSkill(player, 11901, 1, false);// summon abyssal energy I asmo
				}
				player.getSkillList().addSkill(player, 11897, 1, false);// abyssal fury III
				player.getSkillList().addSkill(player, 11903, 1, false);// aegis I
				break;
			case 17:
				if(this.getRace().getRaceId() == 0)
				{
					player.getSkillList().addSkill(player, 11888, 1, false);// trans IV elyos
					player.getSkillList().addSkill(player, 11900, 1, false);// summon abyssal energy II elyos
				}
				else
				{
					player.getSkillList().addSkill(player, 11893, 1, false);// trans IV asmo
					player.getSkillList().addSkill(player, 11902, 1, false);// summon abyssal energy II asmo
				}
				player.getSkillList().addSkill(player, 11898, 1, false);// abyssal fury IV
				player.getSkillList().addSkill(player, 11903, 1, false);// aegis I
				player.getSkillList().addSkill(player, 11904, 1, false);// abyssal wave I
				break;
			case 18:
				if(this.getRace().getRaceId() == 0)
				{
					player.getSkillList().addSkill(player, 11889, 1, false);// trans V elyos
					player.getSkillList().addSkill(player, 11900, 1, false);// summon abyssal energy II elyos
				}
				else
				{
					player.getSkillList().addSkill(player, 11894, 1, false);// trans V asmo
					player.getSkillList().addSkill(player, 11902, 1, false);// summon abyssal energy II asmo
				}
				player.getSkillList().addSkill(player, 11898, 1, false);// abyssal fury IV
				player.getSkillList().addSkill(player, 11903, 1, false);// aegis I
				player.getSkillList().addSkill(player, 11904, 1, false);// abyssal wave I
				player.getSkillList().addSkill(player, 11905, 1, false);// tidal wave I
				player.getSkillList().addSkill(player, 11906, 1, false);// verdict I
				break;

		}
	}

	public Race getRace()
	{
		return race;
	}

	public void setRace(Race race)
	{
		this.race = race;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public PlayerClass getPlayerClass()
	{
		return playerClass;
	}

	public void setPlayerClass(PlayerClass playerClass)
	{
		this.playerClass = playerClass;
	}

	public boolean isOnline()
	{
		return online;
	}

	public void setOnline(boolean online)
	{
		this.online = online;
	}

	public Gender getGender()
	{
		return gender;
	}

	public void setGender(Gender gender)
	{
		this.gender = gender;
	}

	public WorldPosition getPosition()
	{
		return position;
	}

	public Timestamp getLastOnline()
	{
		return lastOnline;
	}

	public void setBindPoint(int bindId)
	{
		this.bindPoint = bindId;
	}

	public int getBindPoint()
	{
		return bindPoint;
	}

	public void setLastOnline(Timestamp timestamp)
	{
		lastOnline = timestamp;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		if(level <= DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel())
		{
			this.setExp(DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(level));
		}
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public int getTitleId()
	{
		return titleId;
	}

	public void setTitleId(int titleId)
	{
		this.titleId = titleId;
	}

	/**
	 * This method should be called exactly once after creating object of this class
	 * 
	 * @param position
	 */
	public void setPosition(WorldPosition position)
	{
		if(this.position != null)
		{
			throw new IllegalStateException("position already set");
		}
		this.position = position;
	}

	/**
	 * Gets the cooresponding Player for this common data. Returns null if the player is not online
	 * 
	 * @return Player or null
	 */
	public Player getPlayer()
	{
		if(online && getPosition() != null)
		{
			return World.getInstance().findPlayer(playerObjId);
		}
		return null;
	}

	public void addDp(int dp)
	{
		setDp(this.dp + dp);
	}

	/**
	 * //TODO move to lifestats -> db save?
	 * 
	 * @param dp
	 */
	public void setDp(int dp)
	{
		if(getPlayer() != null)
		{
			if(playerClass.isStartingClass())
				return;

			int maxDp = getPlayer().getGameStats().getCurrentStat(StatEnum.MAXDP);
			this.dp = dp > maxDp ? maxDp : dp;

			PacketSendUtility.broadcastPacket(getPlayer(), new SM_DP_INFO(playerObjId, this.dp), true);
			PacketSendUtility.sendPacket(getPlayer(), new SM_STATS_INFO(getPlayer()));
			PacketSendUtility.sendPacket(getPlayer(), new SM_STATUPDATE_DP(this.dp));
		}
		else
		{
			log.warn("CHECKPOINT : getPlayer in PCD return null for setDP " + isOnline() + " " + getPosition());
		}
	}

	public int getDp()
	{
		return this.dp;
	}

	@Override
	public int getTemplateId()
	{
		return 100000 + race.getRaceId() * 2 + gender.getGenderId();
	}

	@Override
	public int getNameId()
	{
		return 0;
	}

	/**
	 * @param warehouseSize
	 *            the warehouseSize to set
	 */
	public void setWarehouseSize(int warehouseSize)
	{
		this.warehouseSize = warehouseSize;
	}

	/**
	 * @return the warehouseSize
	 */
	public int getWarehouseSize()
	{
		return warehouseSize;
	}

	public void setMailboxLetters(int count)
	{
		this.mailboxLetters = count;
	}

	public int getMailboxLetters()
	{
		return mailboxLetters;
	}

	/**
	 * @param repletionState
	 *            the repletionState to set
	 */
	public void setRepletionState(long repletionAmount)
	{
		if(this.getLevel() >= 20)
			this.repletionstate = repletionAmount;
	}
	
	/**
	 * @return the repletionState
	 */
	public long getRepletionState()
	{
		if(this.getLevel() >= 20)
			return repletionstate;
		else
			return 0;
	}

	
}
