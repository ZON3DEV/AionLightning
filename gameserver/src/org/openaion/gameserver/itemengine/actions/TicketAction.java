package org.openaion.gameserver.itemengine.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import org.openaion.gameserver.services.CubeExpandService;
import org.openaion.gameserver.services.WarehouseService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author ginho1
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TicketAction")
public class TicketAction extends AbstractItemAction
{
	@XmlAttribute
	protected String function;
	@XmlAttribute
	protected int param;

	/**
	 * Gets the value of the function property.
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * Gets the value of the param property.
	 */
	public int getParam() {
		return param;
	}

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		if(function.equals("addCube"))
		{
			return (player.getCubeSize() < 9);
		}

		if(function.equals("addWharehouse"))
		{
			return (player.getWarehouseSize() < 9);
		}

		return false;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem)
	{
		Item item = player.getInventory().getItemByObjId(parentItem.getObjectId());

		if(item != null)
		{
			if(player.getInventory().removeFromBag(item, true))
			{
				if(function.equals("addCube"))
				{
					CubeExpandService.expand(player);
				}

				if(function.equals("addWharehouse"))
				{
					WarehouseService.expand(player);
				}

				PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(parentItem.getObjectId()));
			}
		}
	}

}
