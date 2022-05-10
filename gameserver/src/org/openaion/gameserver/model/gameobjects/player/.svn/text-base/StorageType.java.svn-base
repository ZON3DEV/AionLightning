/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openaion.gameserver.model.gameobjects.player;

/**
 *
 * @author kosyachok
 */
public enum StorageType
{
	CUBE(0, 109),
	REGULAR_WAREHOUSE(1, 104),
	ACCOUNT_WAREHOUSE(2, 17),
	LEGION_WAREHOUSE(3, 25),
	PET_BAG_6(32, 6),
	PET_BAG_12(33, 12),
	PET_BAG_18(34, 18),
	PET_BAG_24(35, 24),
	BROKER(126),
	MAILBOX(127);

	private int id;
	private int limit;
	
	private StorageType(int id)
	{
		this.id = id;
	}
	
	private StorageType(int id, int limit)
	{
		this.id = id;
		this.limit = limit;
	}

	public int getId()
	{
		return id;
	}
	
	public int getLimit()
	{
		return limit;
	}
	
	public static StorageType getStorageTypeById(int id)
	{
		for(StorageType st : values())
		{
			if(st.id == id)
				return st;
		}
		return null;
	}
}
