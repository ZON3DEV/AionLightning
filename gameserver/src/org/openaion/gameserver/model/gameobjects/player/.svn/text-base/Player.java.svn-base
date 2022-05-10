/**
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

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.configs.main.DropConfig;
import org.openaion.gameserver.configs.main.PeriodicSaveConfig;
import org.openaion.gameserver.controllers.FlyController;
import org.openaion.gameserver.controllers.PlayerController;
import org.openaion.gameserver.controllers.ReviveController;
import org.openaion.gameserver.controllers.effect.PlayerEffectController;
import org.openaion.gameserver.dao.AbyssRankDAO;
import org.openaion.gameserver.dao.InventoryDAO;
import org.openaion.gameserver.dao.ItemStoneListDAO;
import org.openaion.gameserver.dao.PlayerDAO;
import org.openaion.gameserver.dao.PlayerQuestListDAO;
import org.openaion.gameserver.dao.PlayerSkillListDAO;
import org.openaion.gameserver.dao.PlayerWorldBanDAO;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.Gender;
import org.openaion.gameserver.model.NpcType;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.account.Account;
import org.openaion.gameserver.model.alliance.PlayerAlliance;
import org.openaion.gameserver.model.drop.DropList;
import org.openaion.gameserver.model.drop.DropTemplate;
import org.openaion.gameserver.model.drop.NpcDropStat;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.GroupGate;
import org.openaion.gameserver.model.gameobjects.Homing;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Kisk;
import org.openaion.gameserver.model.gameobjects.Monster;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.PersistentState;
import org.openaion.gameserver.model.gameobjects.SkillAreaNpc;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.gameobjects.state.CreatureVisualState;
import org.openaion.gameserver.model.gameobjects.stats.PlayerGameStats;
import org.openaion.gameserver.model.gameobjects.stats.PlayerLifeStats;
import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.model.instances.InstanceCD;
import org.openaion.gameserver.model.items.ItemCooldown;
import org.openaion.gameserver.model.legion.Legion;
import org.openaion.gameserver.model.legion.LegionMember;
import org.openaion.gameserver.model.templates.stats.PlayerStatsTemplate;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.services.ArenaService;
import org.openaion.gameserver.services.BrokerService;
import org.openaion.gameserver.services.CashShopManager;
import org.openaion.gameserver.services.DropService;
import org.openaion.gameserver.services.ExchangeService;
import org.openaion.gameserver.services.PlayerService;
import org.openaion.gameserver.skill.task.CraftingTask;
import org.openaion.gameserver.utils.HumanTime;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.utils.collections.LastUsedCache;
import org.openaion.gameserver.utils.i18n.CustomMessageId;
import org.openaion.gameserver.utils.i18n.LanguageHandler;
import org.openaion.gameserver.utils.rates.Rates;
import org.openaion.gameserver.utils.rates.RegularRates;
import org.openaion.gameserver.world.World;
import org.openaion.gameserver.world.zone.ZoneInstance;
import gnu.trove.TIntObjectHashMap;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;

import org.openaion.gameserver.model.templates.LocationTemplate;

/**
 * This class is representing Player object, it contains all needed data.
 *
 *
 * @author -Nemesiss-
 * @author SoulKeeper
 * @author alexa026
 *
 */
public class Player extends Creature
{
        public static final int                                                CHAT_NOT_FIXED        = 0;
        public static final int                                                CHAT_FIXED_ON_WORLD = 1;
        public static final int                                                CHAT_FIXED_ON_ELYOS = 2;
        public static final int                                                CHAT_FIXED_ON_ASMOS = 4;
        public static final int                                                CHAT_FIXED_ON_BOTH = CHAT_FIXED_ON_ELYOS | CHAT_FIXED_ON_ASMOS;

        private static final Logger                                        log = Logger.getLogger(Player.class);

        private PlayerAppearance                                        playerAppearance;
        private PlayerCommonData                                        playerCommonData;
        private Account                                                        playerAccount;
        private LegionMember                                                legionMember;
        private MacroList                                                macroList;
        private SkillList                                                skillList;
        private FriendList                                                friendList;
        private BlockList                                                blockList;
        private ResponseRequester                                        requester;
        private boolean                                                        lookingForGroup        = false;
        private Storage                                                        inventory;
        private Repurchase                                                repurchase;
        private PurchaseLimit                                                purchaseLimit;
        private Storage                                                        regularWarehouse;
        private Storage                                                        accountWarehouse;
        private Storage[]                                                petBag        = new Storage[4];
        private Equipment                                                equipment;
        private Mailbox                                                        mailbox;
        private PrivateStore                                                store;
        private PlayerStatsTemplate                                        playerStatsTemplate;
        private TitleList                                                titleList;
        private EmotionList                                                emotionList;
        private PlayerSettings                                                playerSettings;
        private QuestStateList                                                questStateList;
        private QuestCookie                                                questCookie;
        private List<Integer>                                                nearbyQuestList        = new ArrayList<Integer>();
        private ZoneInstance                                                zoneInstance;
        private PlayerGroup                                                playerGroup;
        private AbyssRank                                                abyssRank;
        private Guild                                                        guild;
        private Rates                                                        rates;
        private RecipeList                                                recipeList;
        private int                                                        flyState = 0;
        private boolean                                                        isTrading;
        private long                                                        prisonTimer = 0;
        private long                                                        startPrison;
        private boolean                                                        invul;
        private FlyController                                                flyController;
        private ReviveController                                        reviveController;
        private CraftingTask                                                craftingTask;
        private int                                                        flightTeleportId;
        private int                                                        flightDistance;
        private Summon                                                        summon;
        private Kisk                                                        kisk;
        private Prices                                                        prices;
        private boolean                                                        isGagged = false;
        private boolean                                                        isAdminNeutral = false;
        private boolean                                                        isWhisperable = true;
        private long                                                        lastZephyrInvokationSeconds = 0;
        private int                                                        zephyrObjectId = 0;
        private ToyPet                                                        toyPet;
        private boolean                                                        edit_mode = false;
        private boolean                                                        in_arena = false;
        private int                                                        xpBoost        = 0;
        private boolean                                                        in_darkpoeta = false;
        private boolean                                                        in_dredgion = false;
        private int                                                        instancePVPKills = 0;
        private int                                                        instanceBalaurKills = 0;
        private int                                                        instanceCaptured = 0;
        private int                                                        instancePlayerScore = 0;
        private int                                                        instancePlayerAP = 0;
        private boolean                                                        questTimerOn = false;
        private boolean                                                        receive_entry = false;

        private Map<Integer, ItemCooldown>                                itemCoolDowns;
        private TIntObjectHashMap<InstanceCD>                                InstanceCDs = new TIntObjectHashMap<InstanceCD>();

        public int                                                        CHAT_FIX_WORLD_CHANNEL        = CHAT_NOT_FIXED;
        private boolean                                                        bannedFromWorld        = false;
        private String                                                        bannedFromWorldBy = "";
        private long                                                        bannedFromWorldDuring = 0;
        private Date                                                        bannedFromWorldDate = null;
        private String                                                        bannedFromWorldReason = "";
        private ScheduledFuture<?>                                        taskToUnbanFromWorld = null;

        private LastUsedCache<Integer, NpcDropStat>                        lastNpcDrops;

        private long                                                        lastMessageTime;

        /**
         * Test Value for ingame shop currency
         */
        public int                                                        shopMoney = 0;

        /**
         * Windstream variable for handshake.
         */
        private int                                                        enterWindstream = 0;

        /**
         * Static information for players
         */
        private static final int                                        CUBE_SPACE = 9;
        private static final int                                        WAREHOUSE_SPACE = 8;

        /**
         * Connection of this Player.
         */
        private AionConnection                                        clientConnection;

        /**
         * This variable used to check if players sends fake CM_LEVEL_READY packet
         */
        private int                                                         oldWorldId;

        // debug
        private static int                                                counter = 0;

	private LocationTemplate lastLoc;

        /**
         * Quest time counter, to avoid from sending fake quest reward packet.
         */
        @SuppressWarnings("unused")
        private long                                                        lastCompletedQuestTime = 0;
        public List<Integer> spyedLegions = new ArrayList<Integer>();
        public List<Integer> spyedGroups = new ArrayList<Integer>();

        public Player(PlayerController controller, PlayerCommonData plCommonData, PlayerAppearance appereance,
                Account account)
        {
                super(plCommonData.getPlayerObjId(), controller, null, null, plCommonData.getPosition());
                // TODO may be pcd->visibleObjectTemplate ?
                this.playerCommonData = plCommonData;
                this.playerAppearance = appereance;
                this.playerAccount = account;
                this.repurchase = new Repurchase();
                this.purchaseLimit = new PurchaseLimit();
                this.prices = new Prices();
                this.requester = new ResponseRequester(this);
                this.questStateList = new QuestStateList();
                this.titleList = new TitleList();
                this.emotionList = new EmotionList();
                this.lastNpcDrops = new LastUsedCache<Integer, NpcDropStat>(DropConfig.NPC_DROP_HISTORY_COUNT);
                this.shopMoney = CashShopManager.getInstance().getPlayerCredits(this);
                controller.setOwner(this);
                counter++;
        }

	public LocationTemplate getLastLoc()
	{
		return lastLoc;
	}
	public void setLastLoc(LocationTemplate loc)
	{
		lastLoc = loc;
	}
        public Account getAccount()
        {
                return playerAccount;
        }

        protected void finalize() throws Throwable
        {
                try
                {
                        counter--;
                }
                finally
                {
                        super.finalize();
                }
        }

        public static int getCounter()
        {
                return counter;
        }

        public PlayerCommonData getCommonData()
        {
                return playerCommonData;
        }

        @Override
        public String getName()
        {
                return playerCommonData.getName();
        }

        public PlayerAppearance getPlayerAppearance()
        {
                return playerAppearance;
        }

        /**
         * Set connection of this player.
         *
         * @param clientConnection
         */
        public void setClientConnection(AionConnection clientConnection)
        {
                this.clientConnection = clientConnection;
        }

        /**
         * Get connection of this player.
         *
         * @return AionConnection of this player.
         *
         */
        public AionConnection getClientConnection()
        {
                return this.clientConnection;
        }

        public MacroList getMacroList()
        {
                return macroList;
        }

        public void setMacroList(MacroList macroList)
        {
                this.macroList = macroList;
        }

        public SkillList getSkillList()
        {
                return skillList;
        }

        public void setSkillList(SkillList skillList)
        {
                this.skillList = skillList;
        }

        /**
         * @return the toyPet
         */
        public ToyPet getToyPet()
        {
                return toyPet;
        }

        /**
         * @param toyPet
         *            the toyPet to set
         */
        public void setToyPet(ToyPet toyPet)
        {
                this.toyPet = toyPet;
        }

        /**
         * Gets this players Friend List
         *
         * @return FriendList
         */
        public FriendList getFriendList()
        {
                return friendList;
        }

        /**
         * Is this player looking for a group
         *
         * @return true or false
         */
        public boolean isLookingForGroup()
        {
                return lookingForGroup;
        }

        /**
         * Sets whether or not this player is looking for a group
         *
         * @param lookingForGroup
         */
        public void setLookingForGroup(boolean lookingForGroup)
        {
                this.lookingForGroup = lookingForGroup;
        }

        /**
         * Sets this players friend list. <br />
         * Remember to send the player the <tt>SM_FRIEND_LIST</tt> packet.
         *
         * @param list
         */
        public void setFriendList(FriendList list)
        {
                this.friendList = list;
        }

        public BlockList getBlockList()
        {
                return blockList;
        }

        public void setBlockList(BlockList list)
        {
                this.blockList = list;
        }

        /**
         * @return the playerLifeStats
         */
        @Override
        public PlayerLifeStats getLifeStats()
        {
                return (PlayerLifeStats) super.getLifeStats();
        }

        /**
         * @param lifeStats
         *            the lifeStats to set
         */
        public void setLifeStats(PlayerLifeStats lifeStats)
        {
                super.setLifeStats(lifeStats);
        }

        /**
         * @return the gameStats
         */
        @Override
        public PlayerGameStats getGameStats()
        {
                return (PlayerGameStats) super.getGameStats();
        }

        /**
         * @param gameStats
         *            the gameStats to set
         */
        public void setGameStats(PlayerGameStats gameStats)
        {
                super.setGameStats(gameStats);
        }

        public NpcDropStat getNpcDropStats(int npcId)
        {
                NpcDropStat stats = lastNpcDrops.get(npcId);
                if(stats == null)
                {
                        DropList dropList = DropService.getInstance().getDropList();
                        Set<DropTemplate> dropTemplates = dropList.getDropsFor(npcId);
                        stats = new NpcDropStat(dropTemplates);
                        if (dropTemplates != null)
                        {
                                dropTemplates.clear();
                                dropTemplates = null;
                        }
                }
                // renew or add drop stats
                lastNpcDrops.put(npcId, stats, DropConfig.NPC_DROP_EXPIRE_MINUTES * 60 * 1000);
                return stats;
        }

        /**
         * Gets the ResponseRequester for this player
         *
         * @return ResponseRequester
         */
        public ResponseRequester getResponseRequester()
        {
                return requester;
        }

        public boolean isOnline()
        {
                return getClientConnection() != null;
        }

        public int getCubeSize()
        {
                return this.playerCommonData.getCubeSize();
        }

        public PlayerClass getPlayerClass()
        {
                return playerCommonData.getPlayerClass();
        }

        public Gender getGender()
        {
                return playerCommonData.getGender();
        }

        /**
         * Return PlayerController of this Player Object.
         *
         * @return PlayerController.
         */
        @Override
        public PlayerController getController()
        {
                return (PlayerController) super.getController();
        }

        @Override
        public byte getLevel()
        {
                return (byte) playerCommonData.getLevel();
        }

        /**
         * @return the inventory
         */

        public Equipment getEquipment()
        {
                return equipment;
        }

        public void setEquipment(Equipment equipment)
        {
                this.equipment = equipment;
        }

        /**
         * @return the player private store
         */
        public PrivateStore getStore()
        {
                return store;
        }

        /**
         * @param store
         *            the store that needs to be set
         */
        public void setStore(PrivateStore store)
        {
                this.store = store;
        }

        /**
         * @return the questStatesList
         */
        public QuestStateList getQuestStateList()
        {
                return questStateList;
        }

        /**
         * @param questStateList
         *            the QuestStateList to set
         */
        public void setQuestStateList(QuestStateList questStateList)
        {
                this.questStateList = questStateList;
        }

        /**
         * @return the questCookie
         */
        public QuestCookie getQuestCookie()
        {
                return questCookie;
        }

        /**
         * @param questCookie
         *            the questCookie to set
         */
        public void setQuestCookie(QuestCookie questCookie)
        {
                this.questCookie = questCookie;
        }

        /**
         * @return the playerStatsTemplate
         */
        public PlayerStatsTemplate getPlayerStatsTemplate()
        {
                return playerStatsTemplate;
        }

        /**
         * @param playerStatsTemplate
         *            the playerStatsTemplate to set
         */
        public void setPlayerStatsTemplate(PlayerStatsTemplate playerStatsTemplate)
        {
                this.playerStatsTemplate = playerStatsTemplate;
        }

        public List<Integer> getNearbyQuests()
        {
                return nearbyQuestList;
        }

        public RecipeList getRecipeList()
        {
                return recipeList;
        }

        public void setRecipeList(RecipeList recipeList)
        {
                this.recipeList = recipeList;
        }

        /**
         * @param inventory
         *            the inventory to set Inventory should be set right after player object is created
         */
        public void setStorage(Storage storage, StorageType storageType)
        {
                if(storageType == StorageType.CUBE)
                {
                        this.inventory = storage;
                        inventory.setOwner(this);
                }

                if(storageType.getId() > 31 && storageType.getId() < 36)
                {
                        this.petBag[storageType.getId() - 32] = storage;
                }

                if(storageType == StorageType.REGULAR_WAREHOUSE)
                {
                        this.regularWarehouse = storage;
                        regularWarehouse.setOwner(this);
                }

                if(storageType == StorageType.ACCOUNT_WAREHOUSE)
                {
                        this.accountWarehouse = storage;
                        accountWarehouse.setOwner(this);
                }
        }

        /**
         *
         * @param storageType
         * @return
         */
        public Storage getStorage(int storageType)
        {
                if(storageType == StorageType.REGULAR_WAREHOUSE.getId())
                        return regularWarehouse;

                if(storageType == StorageType.ACCOUNT_WAREHOUSE.getId())
                        return accountWarehouse;

                if(storageType == StorageType.LEGION_WAREHOUSE.getId())
                        return getLegion().getLegionWarehouse();

                if(storageType > 31 && storageType < 36)
                        return petBag[storageType - 32];

                if(storageType == StorageType.CUBE.getId())
                        return inventory;
                else
                        return null;
        }

        /**
         * Items from UPDATE_REQUIRED storages and equipment
         *
         * @return
         */
        public List<Item> getDirtyItemsToUpdate()
        {
                List<Item> dirtyItems = new ArrayList<Item>();

                Storage cubeStorage = getStorage(StorageType.CUBE.getId());
                if(cubeStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED)
                {
                        dirtyItems.addAll(cubeStorage.getAllItems());
                        dirtyItems.addAll(cubeStorage.getDeletedItems());
                        cubeStorage.setPersistentState(PersistentState.UPDATED);
                }

                Storage regularWhStorage = getStorage(StorageType.REGULAR_WAREHOUSE.getId());
                if(regularWhStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED)
                {
                        dirtyItems.addAll(regularWhStorage.getAllItems());
                        dirtyItems.addAll(regularWhStorage.getDeletedItems());
                        regularWhStorage.setPersistentState(PersistentState.UPDATED);
                }

                Storage accountWhStorage = getStorage(StorageType.ACCOUNT_WAREHOUSE.getId());
                if(accountWhStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED)
                {
                        dirtyItems.addAll(accountWhStorage.getAllItems());
                        dirtyItems.addAll(accountWhStorage.getDeletedItems());
                        accountWhStorage.setPersistentState(PersistentState.UPDATED);
                }

                for(int petBagId = 32; petBagId < 36; petBagId++)
                {
                        Storage petBag = getStorage(petBagId);
                        if(petBag.getPersistentState() == PersistentState.UPDATE_REQUIRED)
                        {
                                dirtyItems.addAll(petBag.getAllItems());
                                dirtyItems.addAll(petBag.getDeletedItems());
                                petBag.setPersistentState(PersistentState.UPDATED);
                        }
                }

                Equipment equipment = getEquipment();
                if(equipment.getPersistentState() == PersistentState.UPDATE_REQUIRED)
                {
                        dirtyItems.addAll(equipment.getEquippedItems());
                        equipment.setPersistentState(PersistentState.UPDATED);
                }

                return dirtyItems;
        }

        /**
         * //TODO probably need to optimize here
         *
         * @return
         */
        public List<Item> getAllItems()
        {
                List<Item> allItems = new ArrayList<Item>();

                Storage cubeStorage = getStorage(StorageType.CUBE.getId());
                allItems.addAll(cubeStorage.getAllItems());

                Storage regularWhStorage = getStorage(StorageType.REGULAR_WAREHOUSE.getId());
                allItems.addAll(regularWhStorage.getStorageItems());

                Storage accountWhStorage = getStorage(StorageType.ACCOUNT_WAREHOUSE.getId());
                allItems.addAll(accountWhStorage.getStorageItems());

                for(int petBagId = 32; petBagId < 36; petBagId++)
                {
                        Storage petBag = getStorage(petBagId);
                        allItems.addAll(petBag.getAllItems());
                }

                Equipment equipment = getEquipment();
                allItems.addAll(equipment.getEquippedItems());

                return allItems;
        }

        public Storage getInventory()
        {
                return inventory;
        }

        /**
         * @param CubeUpgrade
         *            int Sets the cubesize
         */
        public void setCubesize(int cubesize)
        {
                this.playerCommonData.setCubesize(cubesize);
                getInventory().setLimit(getInventory().getLimit() + (cubesize * CUBE_SPACE));
        }

        /**
         * @return the playerSettings
         */
        public PlayerSettings getPlayerSettings()
        {
                return playerSettings;
        }

        public Repurchase getRepurchase()
        {
                return repurchase;
        }

        public void setReceiveEntry(boolean receive_entry)
        {
                this.receive_entry = receive_entry;
        }

        public boolean getReceiveEntry()
        {
                return receive_entry;
        }

        public PurchaseLimit getPurchaseLimit()
        {
                return purchaseLimit;
        }

        public void setPurchaseLimit(PurchaseLimit purchaseLimit)
        {
                this.purchaseLimit = purchaseLimit;
        }

        /**
         * @param playerSettings
         *            the playerSettings to set
         */
        public void setPlayerSettings(PlayerSettings playerSettings)
        {
                this.playerSettings = playerSettings;
        }

        /**
         * @return the zoneInstance
         */
        public ZoneInstance getZoneInstance()
        {
                return zoneInstance;
        }

        /**
         * @param zoneInstance
         *            the zoneInstance to set
         */
        public void setZoneInstance(ZoneInstance zoneInstance)
        {
                this.zoneInstance = zoneInstance;
        }

        public TitleList getTitleList()
        {
                return titleList;
        }

        public void setTitleList(TitleList titleList)
        {
                this.titleList = titleList;
                this.titleList.setOwner(this);
        }

        public EmotionList getEmotionList()
        {
                return emotionList;
        }

        public void setEmotionList(EmotionList emotionList)
        {
                this.emotionList = emotionList;
                this.emotionList.setOwner(this);
        }

        /**
         * @return the playerGroup
         */
        public PlayerGroup getPlayerGroup()
        {
                return playerGroup;
        }

        /**
         * @param playerGroup
         *            the playerGroup to set
         */
        public void setPlayerGroup(PlayerGroup playerGroup)
        {
                this.playerGroup = playerGroup;
        }

        /**
         * @return the abyssRank
         */
        public AbyssRank getAbyssRank()
        {
                return abyssRank;
        }

        /**
         * @param abyssRank
         *            the abyssRank to set
         */
        public void setAbyssRank(AbyssRank abyssRank)
        {
                this.abyssRank = abyssRank;
        }

        /**
         * @return the guild
         */
        public Guild getGuild()
        {
                return guild;
        }

        /**
         * @param guild
         *            the guild to set
         */
        public void setGuild(Guild guild)
        {
                this.guild = guild;
        }

        @Override
        public PlayerEffectController getEffectController()
        {
                return (PlayerEffectController) super.getEffectController();
        }

        @Override
        public void initializeAi()
        {
                // Empty
        }

        /**
         * <b><font color='red'>NOTICE: </font>this method is supposed to be called only from
         * {@link PlayerService#playerLoggedIn(Player)}</b>
         */
        public void onLoggedIn()
        {
                getController().addTask(
                        TaskId.PLAYER_UPDATE,
                        ThreadPoolManager.getInstance().scheduleAtFixedRate(new GeneralUpdateTask(this),
                                PeriodicSaveConfig.PLAYER_GENERAL * 1000, PeriodicSaveConfig.PLAYER_GENERAL * 1000));
                getController().addTask(
                        TaskId.INVENTORY_UPDATE,
                        ThreadPoolManager.getInstance().scheduleAtFixedRate(new ItemUpdateTask(this),
                                PeriodicSaveConfig.PLAYER_ITEMS * 1000, PeriodicSaveConfig.PLAYER_ITEMS * 1000));

                getCommonData().updateAbyssSkills(this, abyssRank);
        }

        /**
         * <b><font color='red'>NOTICE: </font>this method is supposed to be called only from
         * {@link PlayerService#playerLoggedOut(Player)}</b>
         */
        public void onLoggedOut()
        {
                requester.denyAll();
                friendList.setStatus(FriendList.Status.OFFLINE);
                BrokerService.getInstance().removePlayerCache(this);
                ExchangeService.getInstance().cancelExchange(this);
        }

        /**
         * Returns true if has valid LegionMember
         */
        public boolean isLegionMember()
        {
                return legionMember != null;
        }

        /**
         * @param legionMember
         *            the legionMember to set
         */
        public void setLegionMember(LegionMember legionMember)
        {
                this.legionMember = legionMember;
        }

        /**
         * @return the legionMember
         */
        public LegionMember getLegionMember()
        {
                return legionMember;
        }

        /**
         * @return the legion
         */
        public Legion getLegion()
        {
                if(legionMember != null)
                        return legionMember.getLegion();
                else
                        return null;
        }

        /**
         * Checks if object id's are the same
         *
         * @return true if the object id is the same
         */
        public boolean sameObjectId(int objectId)
        {
                return this.getObjectId() == objectId;
        }

        /**
         * @return true if a player has a store opened
         */
        public boolean hasStore()
        {
                if(getStore() != null)
                        return true;
                return false;
        }

        /**
         * Removes legion from player
         */
        public void resetLegionMember()
        {
                setLegionMember(null);
        }

        /**
         * This method will return true if player is in a group
         *
         * @return true or false
         */
        public boolean isInGroup()
        {
                return playerGroup != null;
        }

        /**
         * Access level of this player
         *
         * @return byte
         */
        public byte getAccessLevel()
        {
                if(playerAccount == null)
                        return 0x00;
                return playerAccount.getAccessLevel();
        }

        /**
         * accountName of this player
         *
         * @return int
         */
        public String getAcountName()
        {
                return playerAccount.getName();
        }

        /**
         * @return the rates
         */
        public Rates getRates()
        {
                if(rates == null)
                        rates = new RegularRates();
                return rates;
        }

        /**
         * @param rates
         *            the rates to set
         */
        public void setRates(Rates rates)
        {
                this.rates = rates;
        }

        /**
         * @return warehouse size
         */
        public int getWarehouseSize()
        {
                return this.playerCommonData.getWarehouseSize();
        }

        /**
         * @param warehouseSize
         */
        public void setWarehouseSize(int warehouseSize)
        {
                this.playerCommonData.setWarehouseSize(warehouseSize);
                getWarehouse().setLimit(getWarehouse().getLimit() + (warehouseSize * WAREHOUSE_SPACE));
        }

        /**
         * @return regularWarehouse
         */
        public Storage getWarehouse()
        {
                return regularWarehouse;
        }

        /**
         * 0: regular, 1: fly, 2: glide
         */
        public int getFlyState()
        {
                return this.flyState;
        }

        public void setFlyState(int flyState)
        {
                this.flyState = flyState;
        }

        /**
         * @return the isTrading
         */
        public boolean isTrading()
        {
                return isTrading;
        }

        /**
         * @param isTrading
         *            the isTrading to set
         */
        public void setTrading(boolean isTrading)
        {
                this.isTrading = isTrading;
        }

        /**
         * @return the isInPrison
         */
        public boolean isInPrison()
        {
                return prisonTimer != 0;
        }

        /**
         * @param prisonTimer
         *            the prisonTimer to set
         */
        public void setPrisonTimer(long prisonTimer)
        {
                if(prisonTimer < 0)
                        prisonTimer = 0;

                this.prisonTimer = prisonTimer;
        }

        /**
         * @return the prisonTimer
         */
        public long getPrisonTimer()
        {
                return prisonTimer;
        }

        /**
         * @return the time in ms of start prison
         */
        public long getStartPrison()
        {
                return startPrison;
        }

        /**
         * @param start
         *            : The time in ms of start prison
         */
        public void setStartPrison(long start)
        {
                this.startPrison = start;
        }

        /**
         * @return
         */
        public boolean isProtectionActive()
        {
                return isInVisualState(CreatureVisualState.BLINKING);
        }

        /**
         * Check is player is invul
         *
         * @return boolean
         **/
        public boolean isInvul()
        {
                return invul;
        }

        /**
         * Sets invul on player
         *
         * @param invul
         *            - boolean
         **/
        public void setInvul(boolean invul)
        {
                this.invul = invul;
        }

        public void setMailbox(Mailbox mailbox)
        {
                this.mailbox = mailbox;
        }

        public Mailbox getMailbox()
        {
                return mailbox;
        }

        /**
         * @return the flyController
         */
        public FlyController getFlyController()
        {
                return flyController;
        }

        /**
         * @param flyController
         *            the flyController to set
         */
        public void setFlyController(FlyController flyController)
        {
                this.flyController = flyController;
        }

        public ReviveController getReviveController()
        {
                return reviveController;
        }

        public void setReviveController(ReviveController reviveController)
        {
                this.reviveController = reviveController;
        }

        public int getLastOnline()
        {
                Timestamp lastOnline = playerCommonData.getLastOnline();
                if(lastOnline == null || isOnline())
                        return 0;

                return (int) (lastOnline.getTime() / 1000);
        }

        /**
         *
         * @param craftingTask
         */
        public void setCraftingTask(CraftingTask craftingTask)
        {
                this.craftingTask = craftingTask;
        }

        /**
         *
         * @return
         */
        public CraftingTask getCraftingTask()
        {
                return craftingTask;
        }

        /**
         *
         * @param flightTeleportId
         */
        public void setFlightTeleportId(int flightTeleportId)
        {
                this.flightTeleportId = flightTeleportId;
        }

        /**
         *
         * @return flightTeleportId
         */
        public int getFlightTeleportId()
        {
                return flightTeleportId;
        }

        /**
         *
         * @param flightDistance
         */
        public void setFlightDistance(int flightDistance)
        {
                this.flightDistance = flightDistance;
        }

        /**
         *
         * @return flightDistance
         */
        public int getFlightDistance()
        {
                return flightDistance;
        }

        /**
         * @return
         */
        public boolean isUsingFlyTeleport()
        {
                return isInState(CreatureState.FLIGHT_TELEPORT) && flightTeleportId != 0;
        }

        public boolean isGM()
        {
                return getAccessLevel() >= AdminConfig.GM_LEVEL;
        }

        /**
         * Npc enemies:<br>
         * - monsters<br>
         * - aggressive npcs<br>
         * - enemies trap npcs<br>
         * - enemies servant npcs<br>
         * - enemies totem npcs<br>
         * - enemies kisk npcs<br>
         *
         * @param npc
         * @return
         */
        @Override
        public boolean isEnemyNpc(Npc npc)
        {
                if(npc.getObjectTemplate().getNpcType() == NpcType.NEUTRAL  || npc.getObjectTemplate().getNpcType() == NpcType.ARTIFACT)
                        return false;
                if(npc instanceof Monster)
                        return true;
                else if(npc instanceof SkillAreaNpc || npc instanceof Homing || npc instanceof GroupGate)
                        return false;
                // Trap, Totem, Servant
                else if(npc != npc.getActingCreature())
                        return npc.isEnemy(this);
                else if(npc instanceof Kisk)
                        return ((Kisk) npc).isEnemy(this);
                else
                        return isAggroFrom(npc);
        }

        /**
         * Player enemies:<br>
         * - different race<br>
         * - duel partner<br>
         * - in arena<br>
         *
         * @param player
         * @return
         */
        @Override
        public boolean isEnemyPlayer(Player player)
        {
                return (player.getCommonData().getRace() != getCommonData().getRace() || getController().isDueling(player) || ArenaService
                        .getInstance().isEnemy(getController().getOwner(), player))
                        && !player.getAdminNeutral();
        }

        /**
         * Summon enemies:<br>
         * - master not null and master is enemy<br>
         */
        @Override
        public boolean isEnemySummon(Summon summon)
        {
                return summon.getMaster() != null && isEnemyPlayer(summon.getMaster());
        }

        /**
         * Player-player friends:<br>
         * - not in duel<br>
         * - same race<br>
         *
         * @param player
         * @return
         */
        public boolean isFriend(Player player)
        {
                return (player.getCommonData().getRace() == getCommonData().getRace() && !getController().isDueling(player))
                        || player.getAdminNeutral();
        }

        @Override
        public String getTribe()
        {
                switch(getCommonData().getRace())
                {
                        case ELYOS:
                                return "PC";
                        default:
                                return "PC_DARK";
                }
        }

        @Override
        public boolean isAggressiveTo(Creature creature)
        {
                return creature.isAggroFrom(this);
        }

        @Override
        public boolean isAggroFrom(Npc npc)
        {
                //neutral dont get aggro
                if (getAdminNeutral())
                        return false;

                String currentTribe = npc.getTribe();

                // moved to Npc.java isAggresiveTo
                // npc's that are 10 or more levels lower don't get aggro on players
                return isAggroIconTo(currentTribe);
        }

        /**
         * Used in SM_NPC_INFO to check aggro irrespective to level
         *
         * @param npcTribe
         * @return
         */
        public boolean isAggroIconTo(String npcTribe)
        {
                switch(getCommonData().getRace())
                {
                        case ELYOS:
                                if(DataManager.TRIBE_RELATIONS_DATA.isGuardDark(npcTribe)
                                        || DataManager.TRIBE_RELATIONS_DATA.isGuardDrakan(npcTribe))
                                        return true;
                                return DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(npcTribe, "PC");
                        case ASMODIANS:
                                if(DataManager.TRIBE_RELATIONS_DATA.isGuardLight(npcTribe)
                                        || DataManager.TRIBE_RELATIONS_DATA.isGuardDrakan(npcTribe))
                                        return true;
                                return DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(npcTribe, "PC_DARK");
                }
                return false;
        }

        @Override
        protected boolean canSeeNpc(Npc npc)
        {
                return true; // TODO
        }

        @Override
        protected boolean canSeePlayer(Player player)
        {
                return player.getVisualState() <= getSeeState();
        }

        /**
         * @return the summon
         */
        public Summon getSummon()
        {
                return summon;
        }

        /**
         * @param summon
         *            the summon to set
         */
        public void setSummon(Summon summon)
        {
                this.summon = summon;
        }

        /**
         * @param new kisk to bind to (null if unbinding)
         */
        public void setKisk(Kisk newKisk)
        {
                this.kisk = newKisk;
        }

        /**
         * @return
         */
        public Kisk getKisk()
        {
                return this.kisk;
        }

        /**
         *
         * @param delayId
         * @return
         */
        public boolean isItemUseDisabled(int delayId)
        {
                if(itemCoolDowns == null || !itemCoolDowns.containsKey(delayId))
                        return false;

                Long coolDown = itemCoolDowns.get(delayId).getReuseTime();
                if(coolDown == null)
                        return false;

                if(coolDown < System.currentTimeMillis())
                {
                        itemCoolDowns.remove(delayId);
                        return false;
                }

                return true;
        }

        /**
         *
         * @param itemMask
         * @return
         */
        public long getItemCoolDown(int itemMask)
        {
                if(itemCoolDowns == null || !itemCoolDowns.containsKey(itemMask))
                        return 0;

                return itemCoolDowns.get(itemMask).getReuseTime();
        }

        /**
         * @return the itemCoolDowns
         */
        public Map<Integer, ItemCooldown> getItemCoolDowns()
        {
                return itemCoolDowns;
        }

        /**
         * @return isAdminNeutral value
         */
        public boolean getAdminNeutral()
        {
                return isAdminNeutral;
        }

        /**
         * @param newValue
         */
        public void setAdminNeutral(boolean newValue)
        {
                isAdminNeutral = newValue;
        }

        /**
         *
         * @param delayId
         * @param time
         * @param useDelay
         */
        public void addItemCoolDown(int delayId, long time, int useDelay)
        {
                if(itemCoolDowns == null)
                        itemCoolDowns = Collections.synchronizedMap(new HashMap<Integer, ItemCooldown>());

                itemCoolDowns.put(delayId, new ItemCooldown(time, useDelay));
        }

        /**
         *
         * @param itemMask
         */
        public void removeItemCoolDown(int itemMask)
        {
                if(itemCoolDowns == null)
                        return;
                itemCoolDowns.remove(itemMask);
        }

        /**
         * @return prices
         */
        public Prices getPrices()
        {
                return this.prices;
        }

        /**
         * @param isGagged
         *            the isGagged to set
         */
        public void setGagged(boolean isGagged)
        {
                this.isGagged = isGagged;
        }

        /**
         * @return the isGagged
         */
        public boolean isGagged()
        {
                return isGagged;
        }

        /**
         * @param isWhisperable
         *            the isWhisperable to set
         */
        public void setWhisperable(boolean isWhisperable)
        {
                this.isWhisperable = isWhisperable;
        }

        /**
         * @return the isWhisperable
         */
        public boolean isWhisperable()
        {
                return isWhisperable;
        }

        private class GeneralUpdateTask implements Runnable
        {
                private Player        player;

                private GeneralUpdateTask(Player player)
                {
                        this.player = player;
                }

                @Override
                public void run()
                {
                        try
                        {
                                DAOManager.getDAO(AbyssRankDAO.class).storeAbyssRank(player);
                                DAOManager.getDAO(PlayerSkillListDAO.class).storeSkills(player);
                                DAOManager.getDAO(PlayerQuestListDAO.class).store(player);
                                DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
                        }
                        catch(Exception ex)
                        {
                                log
                                        .error("Exception during periodic saving of player " + player.getName() + " " + ex.getCause() != null ? ex
                                                .getCause().getMessage()
                                                : "null");
                        }
                }
        }

        private class ItemUpdateTask implements Runnable
        {
                private Player        player;

                private ItemUpdateTask(Player player)
                {
                        this.player = player;
                }

                @Override
                public void run()
                {
                        try
                        {
                                DAOManager.getDAO(InventoryDAO.class).store(player);
                                DAOManager.getDAO(ItemStoneListDAO.class).save(player);
                        }
                        catch(Exception ex)
                        {
                                log
                                        .error("Exception during periodic saving of player items " + player.getName() + " " + ex.getCause() != null ? ex
                                                .getCause().getMessage()
                                                : "null");
                        }
                }
        }

        public void setPlayerAlliance(PlayerAlliance playerAlliance)
        {
                this.playerAlliance = playerAlliance;
        }

        private PlayerAlliance        playerAlliance;

        /**
         * @return
         */
        public PlayerAlliance getPlayerAlliance()
        {
                return playerAlliance;
        }

        /**
         * @return boolean value is in alliance
         */
        public boolean isInAlliance()
        {
                return (this.playerAlliance != null);
        }

        /**
         * @param object
         */

        public static String getChanName(int chanId)
        {
                switch(chanId)
                {
                        case CHAT_FIXED_ON_ASMOS:
                                return LanguageHandler.translate(CustomMessageId.CHANNEL_NAME_ASMOS);
                        case CHAT_FIXED_ON_ELYOS:
                                return LanguageHandler.translate(CustomMessageId.CHANNEL_NAME_ELYOS);
                        case CHAT_FIXED_ON_WORLD:
                                return LanguageHandler.translate(CustomMessageId.CHANNEL_NAME_WORLD);
                        case CHAT_FIXED_ON_BOTH:
                                return LanguageHandler.translate(CustomMessageId.CHANNEL_NAME_BOTH);
                }
                return "";
        }

        public static String getChanCommand(int chanId)
        {
                switch(chanId)
                {
                        case CHAT_FIXED_ON_ASMOS:
                                return "." + LanguageHandler.translate(CustomMessageId.CHANNEL_COMMAND_ASMOS);
                        case CHAT_FIXED_ON_ELYOS:
                                return "." + LanguageHandler.translate(CustomMessageId.CHANNEL_COMMAND_ELYOS);
                        case CHAT_FIXED_ON_WORLD:
                                return "." + LanguageHandler.translate(CustomMessageId.CHANNEL_COMMAND_WORLD);
                        case CHAT_FIXED_ON_BOTH:
                                return "." + LanguageHandler.translate(CustomMessageId.CHANNEL_COMMAND_BOTH);
                }
                return "";
        }

        public boolean isBannedFromWorld()
        {
                return bannedFromWorld;
        }

        public void setBannedFromWorld(String by, String reason, long duration, Date date)
        {
                bannedFromWorld = true;
                bannedFromWorldBy = by;
                bannedFromWorldDate = date;
                bannedFromWorldDuring = duration;
                bannedFromWorldReason = reason;
        }

        public boolean banFromWorld(String by, String reason, long duration)
        {
                if(isBannedFromWorld())
                {
                        return false;
                }
                else
                {
                        bannedFromWorld = true;
                        bannedFromWorldDate = Calendar.getInstance().getTime();
                        bannedFromWorldDuring = duration;
                        bannedFromWorldBy = by;
                        bannedFromWorldReason = reason;
                        PlayerWorldBanDAO dao = DAOManager.getDAO(PlayerWorldBanDAO.class);
                        if(!dao.addWorldBan(getObjectId(), by, bannedFromWorldDuring, bannedFromWorldDate, bannedFromWorldReason))
                        {
                                return false;
                        }

                        if(bannedFromWorldDuring > 0)
                        {
                                scheduleUnbanFromWorld();
                        }
                }
                return true;
        }

        public boolean unbanFromWorld()
        {
                bannedFromWorld = false;
                PlayerWorldBanDAO dao = DAOManager.getDAO(PlayerWorldBanDAO.class);
                cancelUnbanFromWorld();
                dao.removeWorldBan(getObjectId());
                return true;
        }

        public void scheduleUnbanFromWorld()
        {
                if(!isBannedFromWorld())
                {
                        throw new RuntimeException("scheduling unban task when not banned from "
                                + getChanCommand(CHAT_FIX_WORLD_CHANNEL));
                }
                cancelUnbanFromWorld();
                final int playerObjId = getObjectId();
                final String playerName = getName();
                final String adminName = bannedFromWorldBy;
                final long time = bannedFromWorldDuring;
                if(time > 0)
                {
                        final Date endDate = new Date(bannedFromWorldDate.getTime() + bannedFromWorldDuring);
                        taskToUnbanFromWorld = ThreadPoolManager.getInstance().schedule(new Runnable(){
                                public void run()
                                {
                                        World world = World.getInstance();
                                        Player player = world.findPlayer(playerName);
                                        Player admin = world.findPlayer(adminName);
                                        if(endDate.getTime() <= Calendar.getInstance().getTimeInMillis())
                                        {
                                                DAOManager.getDAO(PlayerWorldBanDAO.class).removeWorldBan(playerObjId);
                                        }
                                        if(player != null)
                                        {
                                                player.bannedFromWorld = false;
                                                PacketSendUtility.sendSysMessage(player, LanguageHandler
                                                        .translate(CustomMessageId.CHANNEL_BAN_ENDED));
                                        }
                                        if(admin != null)
                                        {
                                                PacketSendUtility.sendSysMessage(admin, LanguageHandler.translate(
                                                        CustomMessageId.CHANNEL_BAN_ENDED_FOR, playerName));
                                        }
                                }
                        }, time);
                }
        }

        private void cancelUnbanFromWorld()
        {
                if(taskToUnbanFromWorld != null)
                {
                        taskToUnbanFromWorld.cancel(false);
                        taskToUnbanFromWorld = null;
                }
        }

        public String getBannedFromWorldBy()
        {
                return bannedFromWorldBy;
        }

        public String getBannedFromWorldReason()
        {
                return bannedFromWorldReason;
        }

        public String getBannedFromWorldRemainingTime()
        {
                long elapsed = 0;
                if(bannedFromWorldDuring == 0)
                {
                        return "indeterminé";
                }
                else
                {
                        elapsed = bannedFromWorldDuring
                                - (Calendar.getInstance().getTimeInMillis() - bannedFromWorldDate.getTime());
                        return HumanTime.approximately(elapsed - (elapsed % 1000));
                }
        }

        public Account getPlayerAccount()
        {
                return playerAccount;
        }

        public long getLastZephyrInvokationSeconds()
        {
                return lastZephyrInvokationSeconds;
        }

        public void setLastZephyrInvokationSeconds(long seconds)
        {
                this.lastZephyrInvokationSeconds = seconds;
        }

        /**
         * @return the zephyrObjectId
         */
        public int getZephyrObjectId()
        {
                return zephyrObjectId;
        }

        /**
         * @param zephyrObjectId
         *            the zephyrObjectId to set
         */
        public void setZephyrObjectId(int zephyrObjectId)
        {
                this.zephyrObjectId = zephyrObjectId;
        }

        /**
         * @param editmode
         */
        public void setEditMode(boolean edit_mode)
        {
                this.edit_mode = edit_mode;
        }

        /**
         * @return editmode
         */
        public boolean isInEditMode()
        {
                return edit_mode;
        }

        private boolean        noExperienceGain        = false;

        public void setNoExperienceGain(boolean noExperienceGain)
        {
                this.noExperienceGain = noExperienceGain;
        }

        /**
         * @return
         */
        public boolean isNoExperienceGain()
        {
                return noExperienceGain;
        }

        /**
         * Methods pertaining to Windstreams
         *
         */
        public int getEnterWindstream()
        {
                return enterWindstream;
        }

        public void setEnterWindstream(int enterWindstream)
        {
                this.enterWindstream = enterWindstream;
        }

        /**
         * @return the xpBoost
         */
        public int getXpBoost()
        {
                return xpBoost;
        }

        /**
         * @param xpBoost
         *            the xpBoost to set
         */
        public void setXpBoost(int xpBoost)
        {
                this.xpBoost = xpBoost;
        }

        /**
         * @return in_arena
         */
        public boolean getInArena()
        {
                return in_arena;
        }

        public void setInArena(boolean in_arena)
        {
                this.in_arena = in_arena;
        }

        /**
         * @return in_darkpoeta . If player have DP counter displayed.
         */
        public boolean getInDarkPoeta()
        {
                return in_darkpoeta;
        }

        public void setInDarkPoeta(boolean in_darkpoeta)
        {
                this.in_darkpoeta = in_darkpoeta;
        }

        public boolean getInDredgion()
        {
                return in_dredgion;
        }
        public void setInDredgion(boolean in_dredgion)
        {
                this.in_dredgion = in_dredgion;
        }

        public int getInstancePVPKills()
        {
                return this.instancePVPKills;
        }
        public void setInstancePVPKills(int instancePVPKills)
        {
                this.instancePVPKills = instancePVPKills;
        }

        public int getInstanceBalaurKills()
        {
                return this.instanceBalaurKills;
        }
        public void setInstanceBalaurKills(int instanceBalaurKills)
        {
                this.instanceBalaurKills = instanceBalaurKills;
        }

        public int getInstanceCaptured()
        {
                return this.instanceCaptured;
        }
        public void setInstanceCaptured(int instanceCaptured)
        {
                this.instanceCaptured = instanceCaptured;
        }

        public int getInstancePlayerScore()
        {
                return this.instancePlayerScore;
        }
        public void setInstancePlayerScore(int instancePlayerScore)
        {
                this.instancePlayerScore = instancePlayerScore;
        }

        public int getInstancePlayerAP()
        {
                return this.instancePlayerAP;
        }
        public void setInstancePlayerAP(int instancePlayerAP)
        {
                this.instancePlayerAP = instancePlayerAP;
        }

        /**
         * @return
         */
        public boolean getQuestTimerOn()
        {
                return questTimerOn;
        }

        /**
         * @param b
         */
        public void setQuestTimerOn(boolean questTimerOn)
        {
                this.questTimerOn = questTimerOn;
        }

        /**
         * @param instanceMapId
         * @param CDEnd
         * @param instanceId
         */
        public void addInstanceCD(int instanceMapId, Timestamp CDEnd, int instanceId, int groupId)
        {
                InstanceCDs.put(instanceMapId, new InstanceCD(CDEnd, instanceId, groupId));
        }

        public InstanceCD getInstanceCD(int instanceMapId)
        {
                return InstanceCDs.get(instanceMapId);
        }

        public TIntObjectHashMap<InstanceCD> getInstanceCDs()
        {
                return InstanceCDs;
        }

        public void removeInstanceCD(int instanceMapId)
        {
                InstanceCDs.remove(instanceMapId);
        }

        public void setLastMessageTime()
        {
                this.lastMessageTime = System.currentTimeMillis();
        }

        public boolean canSayInChannel()
        {
                if (System.currentTimeMillis()-lastMessageTime < CustomConfig.CHANNEL_MESSAGE_INTERVAL*1000)
                {
                        PacketSendUtility.sendMessage(this, "You can't send messages often than one message in " + CustomConfig.CHANNEL_MESSAGE_INTERVAL + " seconds");
                        return false;
                }
                return true;
        }

        /**
         * @return the currentWorldId
         */
        public int getOldWorldId()
        {
                return oldWorldId;
        }

        /**
         * @param currentWorldId the currentWorldId to set
         */
        public void setOldWorldId(int currentWorldId)
        {
                this.oldWorldId = currentWorldId;
        }

}
