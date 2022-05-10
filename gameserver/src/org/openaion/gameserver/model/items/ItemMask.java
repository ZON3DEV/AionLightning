package org.openaion.gameserver.model.items;

//added by Blackhive original credits to xTr

public class ItemMask
{
	//I have renamed masks for easier readability, client names in commentary if they are different
	public static final int LIMIT_ONE = 1;
	public static final int TRADEABLE = (1 << 1);//can_exchange
	public static final int SELLABLE = (1 << 2);//can_sell_to_npc
	public static final int STORABLE_IN_WH = (1 << 3);//can_deposit_to_character_warehouse
	public static final int STORABLE_IN_AWH = (1 << 4);//can_deposit_to_account_warehouse
	public static final int STORABLE_IN_LWH = (1 << 5);//can_deposit_to_guild_warehouse
	public static final int BREAKABLE = (1 << 6);
	public static final int SOUL_BOUND = (1 << 7);//soul_bind
	public static final int REMOVE_LOGOUT = (1 << 8);//remove when logout, temporary items
	public static final int NO_ENCHANT = (1 << 9);// 1 = cannot be enchanted
	public static final int CAN_PROC_ENCHANT = (1 << 10);// ???
	public static final int CAN_COMPOSITE_WEAPON = (1 << 11);// if fusion is allowed
	public static final int REMODELABLE = (1 << 12);//cannot_changeskin == 0
	public static final int CAN_SPLIT = (1 << 13);
	public static final int DELETABLE = (1 << 14);//item_drop_permitted
	public static final int DYEABLE = (1 << 15);//can_dye
}
