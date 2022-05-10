/**
 * This file is part of Aion X Emu <aionxemu.com>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package usercommands;

import java.sql.Connection;

import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.UserCommand;
import org.openaion.gameserver.utils.i18n.CustomMessageId;
import org.openaion.gameserver.utils.i18n.LanguageHandler;
import org.openaion.gameserver.model.gameobjects.player.AbyssRank;
import org.openaion.gameserver.model.account.Account;
import org.openaion.gameserver.services.CashShopManager;
import org.openaion.gameserver.network.aion.serverpackets.SM_INGAMESHOP_BALANCE;

/**
 * Author: Geekswordsman
 *
 */
public class ExchangeToll extends UserCommand {
    public ExchangeToll() {
        super("exchangetoll");
    }

    //Look up information only once for this instance of the command
    private int apExchangeRate = CustomConfig.TOLL_EXCHANGE_AP_RATE;
    private int kinahExchangeRate = CustomConfig.TOLL_EXCHANGE_KINAH_RATE;
    private String exchangeRestriction = CustomConfig.TOLL_EXCHANGE_RESTRICTION.toLowerCase();

    @Override
    public void executeCommand(Player player, String params) {
            if (player == null) {
                    //This should not happen!
                    return;
            }

        if (!CustomConfig.TOLL_EXCHANGE_ENABLED) {
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_DISABLED));
            return;
        }

        //Verify exchangeRestriction is set.  An invalid parameter will cause it to default to none (no restriction on exchange)
        if (exchangeRestriction == null && (!exchangeRestriction.equals("none") && !exchangeRestriction.equals("ap") && !exchangeRestriction.equals("kinah")))
                exchangeRestriction = "none";

        if (params == null || params == "") {
                if (exchangeRestriction.equals("kinah"))
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_KINAH_SYNTAX, kinahExchangeRate));
                else if (exchangeRestriction.equals("ap"))
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_AP_SYNTAX, apExchangeRate));
                else
                        PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_SYNTAX, apExchangeRate, kinahExchangeRate));
                return;
        }

        int shopMoneyToGain = 0;

        if (params.toLowerCase().indexOf("ap") != -1) {
                if (exchangeRestriction.equals("kinah")) {
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_SYNTAX, kinahExchangeRate));
                    return;
                }

            String[] args = params.split(" ");

                AbyssRank rank = player.getAbyssRank();
                int currentAP = rank.getAp();

                int apToExchange = calculateAPToExchange(player, args, currentAP);
                if (apToExchange == -1)
                        return;
                if (apToExchange < 0)
                        return;
                else if (apToExchange == 0) {
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_GAINING_TOLL_VIA_AP, Integer.parseInt(args[1]), apExchangeRate));
                        return;
                }

                shopMoneyToGain = (apToExchange / apExchangeRate);

                player.getCommonData().setAp(currentAP - apToExchange);
        } else {
                //Exchange Amount into ShopMoney (default is Kinah, but overridden if restriction is AP
                int baseToExchange = Integer.parseInt(params);
                if (exchangeRestriction.equals("ap")) {
                    AbyssRank rank = player.getAbyssRank();
                    int currentAP = rank.getAp();

                    int apToExchange = calculateAPToExchange(player, baseToExchange, currentAP);
                    if (apToExchange == -1)
                            return;
                    if (apToExchange < 0)
                            return;
                    else if (apToExchange == 0) {
                        PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_GAINING_TOLL_VIA_AP, baseToExchange, apExchangeRate));
                            return;
                    }

                    shopMoneyToGain = (apToExchange / apExchangeRate);

                    player.getCommonData().setAp(currentAP - apToExchange);
                } else {
                        int kinahToExchange = calculateKinahToExchange(player, baseToExchange);
                        if (kinahToExchange == -1)
                                return;
                        if (kinahToExchange < 0)
                                return;
                        else if (kinahToExchange == 0) {
                                PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_GAINING_TOLL_VIA_KINAH, baseToExchange, kinahExchangeRate));
                                return;
                        }

                        shopMoneyToGain = (kinahToExchange / kinahExchangeRate);

                        player.getInventory().decreaseKinah(kinahToExchange);
                }
        }

        Account playerAccount = player.getPlayerAccount();
        if (playerAccount != null) {
		int currentShopMoney = playerAccount.getShopMoney();
                playerAccount.SetShopMoney(currentShopMoney + shopMoneyToGain);
        }

            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_SUCCESS, shopMoneyToGain));
	    CashShopManager.getInstance().increaseCredits(player, shopMoneyToGain);		
            PacketSendUtility.sendPacket(player, new SM_INGAMESHOP_BALANCE());
    }

    private int calculateAPToExchange(Player player, int apToExchange, int accountAP) {
            if (accountAP < apToExchange) {
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_ENOUGH_AP));
                    return -1;
            }

            //Only convert in even numbers and don't steal from the player!
            int difference = apToExchange % apExchangeRate;
            apToExchange -= difference;

            return apToExchange;
    }

    private int calculateAPToExchange(Player player, String[] args, int accountAP) {
            //Exchange AP into ShopMoney.
            if (args.length < 2) {
                    //Must specify the amount of AP to use
                PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_SYNTAX, apExchangeRate, kinahExchangeRate));
                return -1;
            }

            int apToExchange = Integer.parseInt(args[1]);
            if (accountAP < apToExchange) {
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_ENOUGH_AP));
                    return -1;
            }

            //Only convert in even numbers and don't steal from the player!
            int difference = apToExchange % apExchangeRate;
            apToExchange -= difference;

            return apToExchange;
    }

    private int calculateKinahToExchange(Player player, int kinahToExchange) {
            //Does the player even have the Kinah specified?
            if (player.getInventory().getKinahCount() < kinahToExchange) {
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_ENOUGH_KINAH));
                    return -1;
            }

            //Only convert in even numbers and don't steal from the player!
            int difference = kinahToExchange % kinahExchangeRate;
            kinahToExchange -= difference;

            return kinahToExchange;
    }

}