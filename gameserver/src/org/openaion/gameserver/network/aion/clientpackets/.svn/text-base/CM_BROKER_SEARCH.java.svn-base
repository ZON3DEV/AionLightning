package org.openaion.gameserver.network.aion.clientpackets;

import java.util.ArrayList;
import java.util.List;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.services.BrokerService;


/**
 * @author ginho1
 *
 */
public class CM_BROKER_SEARCH extends AionClientPacket
{
	@SuppressWarnings("unused")
	private int brokerId;
	private int sortType;
	private int page;
	private int mask;
	private int items_length;
	private List<Integer> items_id;

	public CM_BROKER_SEARCH(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		this.brokerId = readD();
		this.sortType = readC(); // 1 - name; 2 - level; 4 - totalPrice; 6 - price for piece
		this.page = readH();
		this.mask = readH();
		this.items_length = readH();
		this.items_id = new ArrayList<Integer>();
		for (int i = 0; i<this.items_length; i++)
			this.items_id.add(readD());
	}

	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();

		BrokerService.getInstance().showRequestedItems(player, mask, sortType, page, items_id, true);
	}
}
