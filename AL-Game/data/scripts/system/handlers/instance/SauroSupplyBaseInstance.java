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

package instance;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

import java.util.Map;
import java.util.Set;

/**
 * @author Alcapwnd
 */
@InstanceID(301130000)
public class SauroSupplyBaseInstance extends GeneralInstanceHandler {

    Npc bossBrigadeGeneralSheba = null;
    Npc bossGuardCaptainAhuradim = null;
    Npc firstPortal = null;
    private boolean isStartTimer1 = false;
    private boolean isStartTimer2 = false;
    private Map<Integer, StaticDoor> doors;

    /**
     * Bonus Monster: He can appear in "5 different rooms" and give ancient
     * coins, ancient manastones, and skins. Swift military bases robbers
     * (Shulacks)
     */
    @Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        doors = instance.getDoors();
        if (Rnd.get(1, 2) == 1) {
			spawn(230846, 497.44034f, 410.62006f, 182.13792f, (byte) 90);
		}
		else {
			spawn(230845, 494.81485f, 401.50598f, 182.13792f, (byte) 90);
		}
		
		int chance = Rnd.get(1, 2);
		spawn(chance == 1 ? 230847 : 230848, 673.56476f, 350.30112f, 203.68098f, (byte) 0);
		spawn(chance == 1 ? 230848 : 230847, 673.5929f, 355.43643f, 203.68098f, (byte) 0);
		spawn(chance == 1 ? 230847 : 230848, 319.44083f, 388.2419f, 159.1054f, (byte) 0);
		spawn(chance == 1 ? 230848 : 230847, 320.11377f, 363.97287f, 159.13863f, (byte) 0);
		spawn(chance == 1 ? 230847 : 230848, 260.303f, 364.23767f, 159.13574f, (byte) 0);
		spawn(chance == 1 ? 230848 : 230847, 280.89667f, 327.7948f, 159.36792f, (byte) 0);
		spawn(chance == 1 ? 230847 : 230848, 299.76794f, 328.2231f, 159.36792f, (byte) 0);
		spawn(chance == 1 ? 230848 : 230847, 516.25977f, 460.57306f, 182.00262f, (byte) 0);
		spawn(chance == 1 ? 230847 : 230848, 582.1885f, 474.40613f, 191.13799f, (byte) 0);
		spawn(chance == 1 ? 230848 : 230847, 508.2988f, 528.0495f, 181.9969f, (byte) 0);
		spawn(chance == 1 ? 230847 : 230848, 470.9632f, 413.62903f, 181.98624f, (byte) 0);
		spawn(chance == 1 ? 230848 : 230847, 490.32245f, 392.86053f, 181.98306f, (byte) 0);
		spawn(chance == 1 ? 230847 : 230848, 488.26163f, 357.20752f, 182.0165f, (byte) 0);
		spawn(chance == 1 ? 230848 : 230847, 492.07034f, 357.97043f, 181.98257f, (byte) 0);
    }

    @Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
        int npcId = npc.getNpcId();
        Integer object = instance.getSoloPlayerObj();
        switch (npcId) {
            case 230847: // Secret Key Box
                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000179, 1)); // Danuar Stone Room Key
                break;
            case 230849:
			case 230850:
			case 230851:
			case 230852:
			case 230853:
			case 230854:
			case 230855:
			case 230856:
			case 230857:
			case 230858:
				if (Rnd.get(1, 100) < 3) {
					dropItems.add(DropRegistrationService.getInstance().regDropItem(1, object, npcId, 185000191, 1));
				}
				break;
        }
    }

    @Override
    public void onDie(Npc npc) {
        switch (npc.getObjectTemplate().getTemplateId()) {
            /**
             * Area 1: Guardroom And Rune Hall
             */
            case 230849: // Guard Captain Rohuka
                sendMsg(1401914);
                if (Rnd.get(1, 100) < 20) {
					spawn(802181, 600.03f, 351.22f, 202.91f, (byte) 0);
				}
                doors.get(383).setOpen(true);
                break;
            case 230851: // Chief Gunner Kurmata
                sendMsg(1401915);
                if (Rnd.get(1, 100) < 20) {
					spawn(802181, 634.28f, 496.86f, 202.53f, (byte) 0);
				}
                doors.get(59).setOpen(true);
                doors.get(382).setOpen(true);
                doors.get(387).setOpen(true);
                spawn(230797, 611.1872f, 452.91882f, 191.2776f, (byte) 39);
                spawn(230797, 610.7328f, 518.80884f, 191.2776f, (byte) 75);
                break;

            /**
             * Area 2: Rune Cloister And Logistic Base
             */
            case 233258: // Derakanak the reaver
                sendMsg(1401916);
                doors.get(372).setOpen(true);
                break;
            case 230850: // Research Teselik
                sendMsg(1401917);
                if (Rnd.get(1, 100) < 20) {
					spawn(802181, 473.77f, 330.28f, 181.79f, (byte) 0);
				}
                doors.get(375).setOpen(true);
                despawnNpc(getNpc(284455));
				despawnNpc(getNpc(284457));
				despawnNpc(getNpc(284687));
                break;

            /**
             * Area 3: Rune Bridge And Logistic Base Arsenal.
             */
            case 233255: // Gatekeeper Stranir
                sendMsg(1401918);
                if (Rnd.get(1, 100) < 20) {
					spawn(802181, 378.47f, 297.19f, 158.83f, (byte) 0);
				}
                doors.get(378).setOpen(true);
                break;
            case 230852: // Commander Ranodim
                sendMsg(1401919);
                if (Rnd.get(1, 100) < 20) {
					spawn(802181, 296.99f, 339.82f, 159.36f, (byte) 0);
				}
                doors.get(388).setOpen(true);
                break;

            /**
             * Area 4: Chiefs Chamber.
             */
            case 230791: // Sheban Legion Elite Assaulter
                sendMsg(1401920);
                doors.get(376).setOpen(true);
                break;
            case 233256:
				if (Rnd.get(1, 100) < 20) {
					spawn(802181, 266.56f, 397.63f, 156.83f, (byte) 0);
				}
				despawnNpc(getNpc(233285));
				despawnNpc(getNpc(233286));
				break;
            case 230853: // Chief Of Staff Moriata
                sendMsg(1401921);
                if (Rnd.get(1, 100) < 20) {
					spawn(802181, 154.59f, 430.45f, 150.77f, (byte) 0);
				}
                firstPortal = (Npc) spawn(730872, 131.85886f, 432.22598f, 151.66972f, (byte) 57, 3); // hidden passage
                break;
            case 284435:
			case 284455:
			case 284457:
			case 284687:
				despawnNpc(npc);
				break;
			case 233285:
			case 233286:
				despawnNpc(npc);
				break;

            /**
             * Area 5: Final Boss. 4.6: only 2 bosses left! ncsoft minimized the
             * keys are need for the two last bosses and removed the rest
             */
            case 230857: // Guard Captain Ahuradim's
            	if (Rnd.get(1, 100) < 20) {
					spawn(802181, 903.43f, 893.13f, 411.57f, (byte) 0);
				}
                spawn(801967, 703.4994f, 889.52856f, 411.5366f, (byte) 44);// Exit
                break;
            case 230858: // Brigade General Sheba's
            	if (Rnd.get(1, 100) < 20) {
					spawn(802181, 903.43f, 893.13f, 411.57f, (byte) 0);
				}
                spawn(801967, 900.43726f, 889.94135f, 411.5362f, (byte) 75);// Exit
                break;
        }
    }

    /**
     * After you choose the level of difficulty, the portal will remain open for
     * "5 mins" then disappear.
     */
    @Override
    public void onEnterZone(Player player, ZoneInstance zone) {
        if (zone.getAreaTemplate().getZoneName() == ZoneName.get("NO_GLIDE_AREA_BOSS_1")) {
            if (!isStartTimer1) {
                isStartTimer1 = true;
                System.currentTimeMillis();
                PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 303));
                firstPortal.getController().delete();
                final Npc PortalA = (Npc) spawn(730876, 131.85886f, 432.22598f, 151.66972f, (byte) 57);
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        sendMsg(1401813);
                        PortalA.getController().delete();
                        bossGuardCaptainAhuradim = (Npc) spawn(230857, 703.4994f, 889.52856f, 411.5366f, (byte) 44); // Guard Captain

                        SkillEngine.getInstance().applyEffectDirectly(19049, bossGuardCaptainAhuradim, bossGuardCaptainAhuradim, 0); // Devour
                        // Soul.
                    }
                }, 300000);
            }
        } else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("NO_GLIDE_AREA_BOSS_2")) {
            if (!isStartTimer2) {
                isStartTimer2 = true;
                System.currentTimeMillis();
                PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 304));
                firstPortal.getController().delete();
                final Npc PortalB = (Npc) spawn(730877, 131.85886f, 432.22598f, 151.66972f, (byte) 57);
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        sendMsg(1401814);
                        PortalB.getController().delete();
                        bossBrigadeGeneralSheba = (Npc) spawn(230858, 900.43726f, 889.94135f, 411.5362f, (byte) 75); // Brigade

                        SkillEngine.getInstance().applyEffectDirectly(19049, bossBrigadeGeneralSheba, bossBrigadeGeneralSheba, 0); // Devour Soul.
                    }
                }, 300000);
            }
        }
    }

    @Override
    public void onExitInstance(Player player) {
        TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
    }
    
    private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

    @Override
    public void onInstanceDestroy() {
        doors.clear();
    }
}