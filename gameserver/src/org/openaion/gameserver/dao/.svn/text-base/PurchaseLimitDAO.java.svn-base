package org.openaion.gameserver.dao;

import org.openaion.commons.database.dao.DAO;
import org.openaion.gameserver.model.gameobjects.player.Player;


/**
 * @author ginho1
 *
 */
public abstract class PurchaseLimitDAO implements DAO
{
	@Override
	public final String getClassName()
	{
		 return PurchaseLimitDAO.class.getName();
	}

	public abstract void loadPurchaseLimit(Player player);
	public abstract void deleteAllPurchaseLimit();
	public abstract void savePurchaseLimit(Player player);
	public abstract int loadCountItem(int itemId);
}
