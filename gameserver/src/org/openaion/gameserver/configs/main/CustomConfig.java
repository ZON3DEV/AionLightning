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
package org.openaion.gameserver.configs.main;

import org.openaion.commons.configuration.Property;

public class CustomConfig
{
	/**
	 * Factions speaking mode
	 */
	@Property(key = "gameserver.factions.speaking.mode", defaultValue = "0")
	public static int			FACTIONS_SPEAKING_MODE;

	/**
	 * Factions whisper mode
	 */
	@Property(key = "gameserver.factions.whisper.mode", defaultValue = "0")
	public static int			FACTIONS_WHISPER_MODE;

	/**
	 * Factions search mode
	 */
	@Property(key = "gameserver.factions.search.mode", defaultValue = "false")
	public static boolean		FACTIONS_SEARCH_MODE;

	/**
	 * Skill autolearn
	 */
	@Property(key = "gameserver.skill.autolearn", defaultValue = "false")
	public static boolean		SKILL_AUTOLEARN;

	/**
	 * Stigma autolearn
	 */
	@Property(key = "gameserver.stigma.autolearn", defaultValue = "false")
	public static boolean		STIGMA_AUTOLEARN;

	/**
	 * Retail like char deletion
	 */
	@Property(key = "gameserver.character.delete.retail", defaultValue = "true")
	public static boolean		RETAIL_CHAR_DELETION;

	/**
	 * Disable monsters aggressive behave
	 */
	@Property(key = "gameserver.disable.mob.aggro", defaultValue = "false")
	public static boolean		DISABLE_MOB_AGGRO;

	/**
	 * Enable 2nd class change simple mode
	 */
	@Property(key = "gameserver.enable.simple.2ndclass", defaultValue = "false")
	public static boolean		ENABLE_SIMPLE_2NDCLASS;
	
	/**
	 * Enable the Christmas decorations
	 */
	@Property(key = "gameserver.enable.decor.christmas", defaultValue = "false")
	public static boolean		ENABLE_DECOR_CHRISTMAS;

	/**
	 * Unstuck delay
	 */
	@Property(key = "gameserver.unstuck.delay", defaultValue = "3600")
	public static int			UNSTUCK_DELAY;

	/**
	 * Enable instances
	 */
	@Property(key = "gameserver.instances.enable", defaultValue = "true")
	public static boolean		ENABLE_INSTANCES;

	/**
	 * Base Fly Time
	 */
	@Property(key = "gameserver.base.flytime", defaultValue = "60")
	public static int			BASE_FLYTIME;

    /**
     * Allows players of opposite factions to bind in enemy territory
     */
	@Property(key = "gameserver.cross.faction.binding", defaultValue = "false")
	public static boolean		ENABLE_CROSS_FACTION_BINDING;

	/**
	 * Allowed Kills in time period for full AP. Move to separate config when more pvp options. 
	 */
	@Property(key = "gameserver.pvp.maxkills", defaultValue = "5")
	public static int			MAX_DAILY_PVP_KILLS;

	/**
	 * Time period for max daily kills in pvp 
	 */
	@Property(key = "gameserver.pvp.period", defaultValue = "24")
	public static int			DAILY_PVP_PERIOD;

	/**
	 * Enable customs channels
	 */
	@Property(key = "gameserver.channels.all.enabled", defaultValue = "false")
	public static boolean		CHANNEL_ALL_ENABLED;

	/**
	 * Enable custom channel .world
	 */
	@Property(key = "gameserver.channels.world.enabled", defaultValue = "false")
	public static boolean		CHANNEL_WORLD_ENABLED;
	
	/**
	 * Interval between messages in world/race chats
	 */
	@Property(key = "gameserver.channels.message.interval", defaultValue = "0")
	public static byte		CHANNEL_MESSAGE_INTERVAL;

	/**
	 * Enable V-research showing all connected players from both faction for GMs
	 */
	@Property(key = "gameserver.search.listall", defaultValue = "false")
	public static boolean		SEARCH_LIST_ALL;

	/**
	 * Enable or disable gm tags
	 */
	@Property(key = "gameserver.gmtag.display", defaultValue = "false")
	public static boolean		GMTAG_DISPLAY;

	@Property(key = "gameserver.gmtag.level1", defaultValue = "<GM>")
	public static String		GM_LEVEL1;

	@Property(key = "gameserver.gmtag.level2", defaultValue = "<HEADGM>")
	public static String		GM_LEVEL2;

	@Property(key = "gameserver.gmtag.level3", defaultValue = "<ADMIN>")
	public static String		GM_LEVEL3;

	/**
	 * Announce on GM connection
	 */
	@Property(key = "gameserver.announce.gm.connection", defaultValue = "false")
	public static boolean		ANNOUNCE_GM_CONNECTION;

	/**
	 * Invis on GM connection
	 */
	@Property(key = "gameserver.invis.gm.connection", defaultValue = "false")
	public static boolean		INVIS_GM_CONNECTION;

	/**
	 * Invul on GM connection
	 */
	@Property(key = "gameserver.invul.gm.connection", defaultValue = "false")
	public static boolean		INVUL_GM_CONNECTION;

	/**
	 * Silence on GM connection
	 */
	@Property(key = "gameserver.silence.gm.connection", defaultValue = "false")
	public static boolean		SILENCE_GM_CONNECTION;

	/**
	 * Speed on GM connection
	 */
	@Property(key = "gameserver.speed.gm.connection", defaultValue = "0")
	public static int			SPEED_GM_CONNECTION;

	/**
	 * Enable or disable instance cooldown
	 */
	@Property(key = "gameserver.instance.cooldown", defaultValue = "true")
	public static boolean		INSTANCE_COOLDOWN;

	/**
	 * Enable or disable Global announce for rare drops
	 */
	@Property(key = "gameserver.announce.raredrops", defaultValue = "false")
	public static boolean		ANNOUNCE_RAREDROPS;

	/**
	 * Enable or disable Kick players using speed hack
	 */
	@Property(key = "gameserver.kick.speedhack.enable", defaultValue = "true")
	public static boolean		KICK_SPEEDHACK;

	/**
	 * Ping minimum Interval to consider hack
	 */
	@Property(key = "gameserver.kick.speedhack.pinginterval", defaultValue = "100000")
	public static long			KICK_PINGINTERVAL;

	/**
	 * Toggle castspell hack detection
	 */
	@Property(key = "gameserver.log.castspell.targethack", defaultValue = "true")
	public static boolean		LOG_CASTSPELL_TARGETHACK;

	@Property(key = "gameserver.log.castspell.speedhack", defaultValue = "true")
	public static boolean		LOG_CASTSPELL_SPEEDHACK;

	@Property(key = "gameserver.log.castspell.cooldownhack", defaultValue = "true")
	public static boolean		LOG_CASTSPELL_COOLDOWNHACK;

	/**
     * Chain trigger override enable. True == Use Custom Rate.
     */
    @Property(key = "gameserver.skill.chain.trigger", defaultValue = "true")
    public static boolean SKILL_CHAIN_TRIGGER;
    
    /**
	 * Chain trigger rate. If false all Chain are 100% success.
	 */ 
    @Property(key = "gameserver.skill.chain.rate", defaultValue = "80")
    public static int SKILL_CHAIN_RATE;

	/**
	 * Add a reward to player for pvp kills
	 */
	@Property(key = "gameserver.pvpreward.enable", defaultValue = "false")
	public static boolean		PVPREWARD_ENABLE;

	/**
	 * Kills needed for item reward
	 */
	@Property(key = "gameserver.pvpreward.kills.needed1", defaultValue = "5")
	public static int		PVPREWARD_KILLS_NEEDED1;

	@Property(key = "gameserver.pvpreward.kills.needed2", defaultValue = "10")
	public static int		PVPREWARD_KILLS_NEEDED2;

	@Property(key = "gameserver.pvpreward.kills.needed3", defaultValue = "15")
	public static int		PVPREWARD_KILLS_NEEDED3;

	/**
	 * Item Rewards
	 */
	@Property(key = "gameserver.pvpreward.item.reward1", defaultValue = "186000031")
	public static int		PVPREWARD_ITEM_REWARD1;

	@Property(key = "gameserver.pvpreward.item.reward2", defaultValue = "186000030")
	public static int		PVPREWARD_ITEM_REWARD2;

	@Property(key = "gameserver.pvpreward.item.reward3", defaultValue = "186000096")
	public static int		PVPREWARD_ITEM_REWARD3;

	/**
	 * Player Search Level Restriction (Level 10)
	 */
	@Property(key = "search.level.restriction", defaultValue = "10")
	public static int	LEVEL_TO_SEARCH;

	/**
	 * Whisper Level Restriction (Level 10)
	 */
	@Property(key = "whisper.level.restriction", defaultValue = "10")
	public static int	LEVEL_TO_WHISPER;

	@Property(key = "gameserver.player.experience.control", defaultValue = "false")
	public static boolean	PLAYER_EXPERIENCE_CONTROL;

	/**
	 * Time in seconds which character stays online after closing client window
	 */
	@Property(key = "gameserver.disconnect.time", defaultValue = "10")
	public static int	DISCONNECT_DELAY;

	/**
	 * Enable Surveys
	 */
	@Property(key = "gameserver.enable.surveys", defaultValue = "false")
	public static boolean	ENABLE_SURVEYS;

	/**
	 * Enable the HTML Welcome Message Window on Player Login
	 */
	@Property(key = "enable.html.welcome", defaultValue = "false")
	public static boolean   ENABLE_HTML_WELCOME;

	/**
	 * Time when top ranking is updated
	 */
	@Property(key = "gameserver.topranking.time", defaultValue = "0:00:00")
	public static String   TOP_RANKING_TIME;

	/**
	 * Time between updates of top ranking
	 */
	@Property(key = "gameserver.topranking.delay", defaultValue = "24")
	public static int   TOP_RANKING_DELAY;

	/**
	* Time when daily quest is stating
	*/
	@Property(key = "gameserver.dailyquest.time", defaultValue = "9:00:00")
	public static String   DAILY_START_TIME;

	/**
	 * Disable rifts for opposing race of territory
	 */
	@Property(key = "gameserver.rift.race", defaultValue = "false")
	public static boolean   RIFT_RACE;

	/**
	* Enable or Disable launching effects on critical
	*/
	@Property(key = "gameserver.criticaleffect", defaultValue = "false")
	public static boolean   CRITICAL_EFFECTS;

	/**
	* Enable or Disable effects related to geodata
	*/
	@Property(key = "gameserver.geodata.related.effects", defaultValue = "false")
	public static boolean   GEODATA_EFFECTS_ENABLED;

	/**
	* Enable or Disable adding of adv. stigma slots on level up
	*/
	@Property(key = "gameserver.advstigmaslot.onlvlup", defaultValue = "false")
	public static boolean   ADVSTIGMA_ONLVLUP;

	/**
	* Droplist Master Data Source
	*/
	@Property(key = "gameserver.droplist.master.source", defaultValue = "xml")
	public static String   GAMESERVER_DROPLIST_MASTER_SOURCE;

	/**
	 * Crafting Configs
	 */
	@Property(key = "gameserver.crafting.speedupchance", defaultValue = "15")
	public static int		CRAFTING_SPEEDUP;

	@Property(key = "gameserver.regular.crafting.success", defaultValue = "33")
	public static int		REGULAR_CRAFTING_SUCCESS;

	@Property(key = "gameserver.critical.crafting.success", defaultValue = "30")
	public static int		CRITICAL_CRAFTING_SUCCESS;

	@Property(key = "gameserver.workorder.bonus", defaultValue = "false")
	public static boolean		WORK_ORDER_BONUS;

	@Property(key = "gameserver.mastercraft.limit.disable", defaultValue = "false")
	public static boolean		MASTERCRAFT_LIMIT_DISABLE;

	/**
	* Enable or Disable count down of duration of Abyss xform after logout
	*/
	@Property(key = "gameserver.abyssxform.afterlogout", defaultValue = "false")
	public static boolean		ABYSS_XFORM_DURATION_AFTER_LOGOUT;
	
	@Property(key = "gameserver.dmgreduction.lvldiffpvp", defaultValue = "false")
	public static boolean		DMG_REDUCTION_LVL_DIFF_PVP;

	@Property(key = "gameserver.emotions.retail", defaultValue = "true")
	public static boolean		RETAIL_EMOTIONS;

	@Property(key = "gameserver.dredgion.ap.win.bonus", defaultValue = "3000")
	public static int		DREDGION_AP_WIN;

	@Property(key = "gameserver.dredgion.ap.lose.bonus", defaultValue = "1000")
	public static int		DREDGION_AP_LOSE;

	@Property(key = "gameserver.chantradredgion.ap.win.bonus", defaultValue = "5000")
	public static int		CHANTRA_DREDGION_AP_WIN;

	@Property(key = "gameserver.chantradredgion.ap.lose.bonus", defaultValue = "5000")
	public static int		CHANTRA_DREDGION_AP_LOSE;

	@Property(key = "gameserver.npc.relation.aggro", defaultValue = "true")
	public static boolean		NPC_RELATION_AGGRO;
	
	@Property(key = "gameserver.npc.dynamicstat", defaultValue = "false")
	public static boolean		NPC_DYNAMIC_STAT;
	
	/**
	 * Databasename of AionShop (Loginserver).
	 */
	@Property(key = "gameserver.aionshop.database", defaultValue = "au_server_ls")
	public static String AIONSHOP_DB;

	@Property(key = "gameserver.aionshop.gift.enable", defaultValue = "false")
	public static boolean AIONSHOP_GIFT_ENABLE;

	@Property(key = "gameserver.rate.tollexchange.enable", defaultValue = "true")
	public static boolean TOLL_EXCHANGE_ENABLED;

	@Property(key = "gameserver.rate.tollexchange.restriction", defaultValue = "none")
	public static String TOLL_EXCHANGE_RESTRICTION;

	@Property(key = "gameserver.rate.tollexchange.ap", defaultValue = "10")
	public static int TOLL_EXCHANGE_AP_RATE;

	@Property(key = "gameserver.rate.tollexchange.kinah", defaultValue = "1000")
	public static int TOLL_EXCHANGE_KINAH_RATE;
	/**
    * Enable dredgion
    */
    @Property(key = "gameserver.dredgion.enable", defaultValue = "true")
    public static boolean       ENABLE_DREDGION;
}
