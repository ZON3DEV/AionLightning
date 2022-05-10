package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.CashShopManager.ShopCategory;


public class SM_INGAMESHOP extends AionServerPacket
{
	private final int type;
	private final ShopCategory[] categories;

	public SM_INGAMESHOP(int type, ShopCategory[] categories)
	{
		this.type = type;
		this.categories = categories;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, type);
		switch(type)
		{
			// TODO : Fix tabs values to something more dynamic
			
			// Top Tabs
			case 0:
				writeH(buf, 1);//Count
				writeD(buf, 25);//Tab Id
				writeS(buf, "Something");//name
				break;
			// Categories
			case 1:
				writeH(buf, categories.length);//Count
				for(ShopCategory category : categories)
				{
					writeD(buf, category.id);//id tab
					writeS(buf, category.name);//name
				}
				break;
		}
	}
}
