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
package org.openaion.gameserver.model.gameobjects.player;

import org.openaion.gameserver.GameServer;
import org.openaion.gameserver.configs.main.PricesConfig;
import org.openaion.gameserver.configs.main.SiegeConfig;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.siege.Influence;

/**
 * @author Sarynth
 *
 * Used to get prices for the player.
 *  - Packets: SM_PRICES, SM_TRADELIST, SM_SELL_ITEM
 *  - Services: Godstone socket, teleporter, other fees.
 * TODO: Add Player owner; value and check for PremiumRates or faction price influence.
 */
public class Prices
{
	
	/**
	 * 
	 */
	public Prices()
	{
		
	}
	
	/**
	 * Used in SM_PRICES
	 * @return buyingPrice
	 */
	public int getGlobalPrices(Race playerRace)
	{
		int defaultPrices = PricesConfig.DEFAULT_PRICES;
		
		if(!SiegeConfig.SIEGE_ENABLED)
			return defaultPrices;
		
		int computedPrice = defaultPrices;
		double ratio = GameServer.getRatiosFor(playerRace);
		if(ratio < 50.0)
			computedPrice = (int) Math.round(defaultPrices - ((50 - ratio)/2));
		else if(ratio > 50.0)
			computedPrice = (int) Math.round(((ratio - 50)/2) + defaultPrices);
		
		return computedPrice;

	}

	/**
	 * Used in SM_PRICES
	 * @return
	 */
	public int getGlobalPricesModifier()
	{
		return PricesConfig.DEFAULT_MODIFIER;
	}

	/**
	 * Used in SM_PRICES
	 * @return taxes
	 */
	public int getTaxes(Race playerRace)
	{
		int defaultTax = PricesConfig.DEFAULT_TAXES;
		
		if(!SiegeConfig.SIEGE_ENABLED)
			return defaultTax;
		
		float influenceValue = 0;
		switch(playerRace)
		{
			case ASMODIANS: influenceValue = Influence.getInstance().getAsmos(); break;
			case ELYOS: influenceValue = Influence.getInstance().getElyos(); break;
			default: influenceValue = 0.5f; break;
		}
		return defaultTax + (Math.round(10 - (influenceValue * 10)));
	}
	
	/**
	 * Used in SM_TRADELIST.
	 * @return buyPriceModifier
	 */
	public int getVendorBuyModifier()
	{
		return PricesConfig.VENDOR_BUY_MODIFIER;
	}

	/**
	 * Used in SM_SELL_ITEM
	 *  - Can be unique per NPC!
	 * @return sellingModifier
	 */
	public int getVendorSellModifier(Race playerRace)
	{
		return (int)(PricesConfig.VENDOR_SELL_MODIFIER);
	}

	/**
	 * @param basePrice
	 * @return modifiedPrice
	 */
	public long getPriceForService(long basePrice, Race playerRace)
	{
		// Tricky. Requires multiplication by Prices, Modifier, Taxes
		// In order, and round down each time to match client calculation.
		return (long)((long)((long)(basePrice *
			this.getGlobalPrices(playerRace) / 100D) *
			this.getGlobalPricesModifier() / 100D) *
			this.getTaxes(playerRace) / 100D);
	}

	/**
	 * @param requiredKinah
	 * @return modified requiredKinah
	 */
	public long getKinahForBuy(long requiredKinah, Race playerRace)
	{
		// Requires double precision for 2mil+ kinah items
		return (long)((long)((long)((long)(requiredKinah *
			this.getVendorBuyModifier() / 100.0D) *
			this.getGlobalPrices(playerRace) / 100.0D) *
			this.getGlobalPricesModifier() / 100.0D) *
			this.getTaxes(playerRace) / 100.0D);
	}

	/**
	 * @param kinahReward
	 * @return
	 */
	public long getKinahForSell(long kinahReward, Race playerRace)
	{
		return (long)(kinahReward * this.getVendorSellModifier(playerRace) / 100D);
	}

}
