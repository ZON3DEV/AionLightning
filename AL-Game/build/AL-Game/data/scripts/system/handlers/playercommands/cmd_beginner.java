/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package playercommands;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author Falke_34
 */
public class cmd_beginner extends PlayerCommand {

	public cmd_beginner() {
		super("beginner");
	}

	@Override
	public void execute(final Player player, String... params) {
		if (player.getPlayerClass() == PlayerClass.ASSASSIN) {
			ItemService.addItem(player, 110301755, 1); // Dazzling Daevanion Jerkin
			ItemService.addItem(player, 113301724, 1); // Dazzling Daevanion Breeches
			ItemService.addItem(player, 112301632, 1); // Dazzling Daevanion Shoulderguards
			ItemService.addItem(player, 111301693, 1); // Dazzling Daevanion Vambrace
			ItemService.addItem(player, 114301761, 1); // Dazzling Daevanion Boots
			ItemService.addItem(player, 164002111, 100); // Greater Running Scroll (1 hour)
			ItemService.addItem(player, 190020135, 1); // Evolved Speckled Ailu Egg (1 hour)
		} else if (player.getPlayerClass() == PlayerClass.CHANTER) {
			ItemService.addItem(player, 110551086, 1); // Dazzling Daevanion Hauberk
			ItemService.addItem(player, 113501663, 1); // Dazzling Daevanion Chausses
			ItemService.addItem(player, 112501584, 1); // Dazzling Daevanion Spaulders
			ItemService.addItem(player, 111501645, 1); // Dazzling Daevanion Handguards
			ItemService.addItem(player, 114501673, 1); // Dazzling Daevanion Brogans
			ItemService.addItem(player, 164002111, 100); // Greater Running Scroll (1 hour)
			ItemService.addItem(player, 190020135, 1); // Evolved Speckled Ailu Egg (1 hour)
		} else if (player.getPlayerClass() == PlayerClass.CLERIC) {
			ItemService.addItem(player, 110551086, 1); // Dazzling Daevanion Hauberk
			ItemService.addItem(player, 113501663, 1); // Dazzling Daevanion Chausses
			ItemService.addItem(player, 112501584, 1); // Dazzling Daevanion Spaulders
			ItemService.addItem(player, 111501645, 1); // Dazzling Daevanion Handguards
			ItemService.addItem(player, 114501673, 1); // Dazzling Daevanion Brogans
			ItemService.addItem(player, 164002111, 100); // Greater Running Scroll (1 hour)
			ItemService.addItem(player, 190020135, 1); // Evolved Speckled Ailu Egg (1 hour)
		} else if (player.getPlayerClass() == PlayerClass.GLADIATOR) {
			ItemService.addItem(player, 110601551, 1); // Dazzling Daevanion Breastplate
			ItemService.addItem(player, 113601497, 1); // Dazzling Daevanion Greaves
			ItemService.addItem(player, 112601496, 1); // Dazzling Daevanion Shoulderplates
			ItemService.addItem(player, 111601514, 1); // Dazzling Daevanion Gauntlets
			ItemService.addItem(player, 114601504, 1); // Dazzling Daevanion Sabatons
			ItemService.addItem(player, 164002111, 100); // Greater Running Scroll (1 hour)
			ItemService.addItem(player, 190020135, 1); // Evolved Speckled Ailu Egg (1 hour)
		} else if (player.getPlayerClass() == PlayerClass.RANGER) {
			ItemService.addItem(player, 110301755, 1); // Dazzling Daevanion Jerkin
			ItemService.addItem(player, 113301724, 1); // Dazzling Daevanion Breeches
			ItemService.addItem(player, 112301632, 1); // Dazzling Daevanion Shoulderguards
			ItemService.addItem(player, 111301693, 1); // Dazzling Daevanion Vambrace
			ItemService.addItem(player, 114301761, 1); // Dazzling Daevanion Boots
			ItemService.addItem(player, 164002111, 100); // Greater Running Scroll (1 hour)
			ItemService.addItem(player, 190020135, 1); // Evolved Speckled Ailu Egg (1 hour)
		} else if (player.getPlayerClass() == PlayerClass.SORCERER) {
			ItemService.addItem(player, 110101756, 1); // Dazzling Daevanion Tunic
			ItemService.addItem(player, 111101581, 1); // Dazzling Daevanion Gloves
			ItemService.addItem(player, 113101593, 1); // Dazzling Daevanion Leggings
			ItemService.addItem(player, 112101531, 1); // Dazzling Daevanion Pauldrons
			ItemService.addItem(player, 114101627, 1); // Dazzling Daevanion Shoes
			ItemService.addItem(player, 164002111, 100); // Greater Running Scroll (1 hour)
			ItemService.addItem(player, 190020135, 1); // Evolved Speckled Ailu Egg (1 hour)
		} else if (player.getPlayerClass() == PlayerClass.SPIRIT_MASTER) {
			ItemService.addItem(player, 110101756, 1); // Dazzling Daevanion Tunic
			ItemService.addItem(player, 111101581, 1); // Dazzling Daevanion Gloves
			ItemService.addItem(player, 113101593, 1); // Dazzling Daevanion Leggings
			ItemService.addItem(player, 112101531, 1); // Dazzling Daevanion Pauldrons
			ItemService.addItem(player, 114101627, 1); // Dazzling Daevanion Shoes
			ItemService.addItem(player, 164002111, 100); // Greater Running Scroll (1 hour)
			ItemService.addItem(player, 190020135, 1); // Evolved Speckled Ailu Egg (1 hour)
		} else if (player.getPlayerClass() == PlayerClass.TEMPLAR) {
			ItemService.addItem(player, 110601551, 1); // Dazzling Daevanion Breastplate
			ItemService.addItem(player, 113601497, 1); // Dazzling Daevanion Greaves
			ItemService.addItem(player, 112601496, 1); // Dazzling Daevanion Shoulderplates
			ItemService.addItem(player, 111601514, 1); // Dazzling Daevanion Gauntlets
			ItemService.addItem(player, 114601504, 1); // Dazzling Daevanion Sabatons
			ItemService.addItem(player, 164002111, 100); // Greater Running Scroll (1 hour)
			ItemService.addItem(player, 190020135, 1); // Evolved Speckled Ailu Egg (1 hour)	
		} else if (player.getPlayerClass() == PlayerClass.BARD) {
			ItemService.addItem(player, 110101756, 1); // Dazzling Daevanion Tunic
			ItemService.addItem(player, 111101581, 1); // Dazzling Daevanion Gloves
			ItemService.addItem(player, 113101593, 1); // Dazzling Daevanion Leggings
			ItemService.addItem(player, 112101531, 1); // Dazzling Daevanion Pauldrons
			ItemService.addItem(player, 114101627, 1); // Dazzling Daevanion Shoes
			ItemService.addItem(player, 164002111, 100); // Greater Running Scroll (1 hour)
			ItemService.addItem(player, 190020135, 1); // Evolved Speckled Ailu Egg (1 hour)
		} else if (player.getPlayerClass() == PlayerClass.GUNNER) {
			ItemService.addItem(player, 110301753, 1); // Dazzling Daevanion Magic Jerkin
			ItemService.addItem(player, 113301722, 1); // Dazzling Daevanion Magic Breeches
			ItemService.addItem(player, 112301630, 1); // Dazzling Daevanion Magic Shoulderguards
			ItemService.addItem(player, 111301691, 1); // Dazzling Daevanion Magic Vambrace
			ItemService.addItem(player, 114301759, 1); // Dazzling Daevanion Magic Boots
			ItemService.addItem(player, 164002111, 100); // Greater Running Scroll (1 hour)
			ItemService.addItem(player, 190020135, 1); // Evolved Speckled Ailu Egg (1 hour)
		} else if (player.getPlayerClass() == PlayerClass.RIDER) {
			ItemService.addItem(player, 110551086, 1); // Dazzling Daevanion Hauberk
			ItemService.addItem(player, 113501663, 1); // Dazzling Daevanion Chausses
			ItemService.addItem(player, 112501584, 1); // Dazzling Daevanion Spaulders
			ItemService.addItem(player, 111501645, 1); // Dazzling Daevanion Handguards
			ItemService.addItem(player, 114501673, 1); // Dazzling Daevanion Brogans
			ItemService.addItem(player, 164002111, 100); // Greater Running Scroll (1 hour)
			ItemService.addItem(player, 190020135, 1); // Evolved Speckled Ailu Egg (1 hour)
		}
		player.setCommandUsed(true);

		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				player.setCommandUsed(false);
			}
		}, 500 * 500 * 100000);
		PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.DEFAULT_FINISH_MESSAGE));
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: .beginner ");
	}
}
