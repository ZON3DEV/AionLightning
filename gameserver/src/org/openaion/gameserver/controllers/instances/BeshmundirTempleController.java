package org.openaion.gameserver.controllers.instances;

import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.controllers.NpcController;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.NpcTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;

/**
 * @author Ritsu
 *
 */
public class BeshmundirTempleController extends NpcController
{
    //private VisibleObject target = null;
    Npc npc = getOwner();
    
    @Override
    public void onDialogRequest(final Player player){
        getOwner().getAi().handleEvent(Event.TALK);
        
        NpcTemplate npctemplate = DataManager.NPC_DATA.getNpcTemplate(getOwner().getNpcId());
        if (npctemplate.getNameId() == 354971)
        {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
            return;
        }

        switch (getOwner().getNpcId()){
            case 730275: {
                ThreadPoolManager.getInstance().schedule(new Runnable(){
                    @Override
                    public void run(){
                        final int defaultUseTime = 3000;
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),getOwner().getObjectId(), defaultUseTime, 1));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getOwner().getObjectId()), true);
                        ThreadPoolManager.getInstance().schedule(new Runnable(){
                            @Override
                            public void run(){
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, getOwner().getObjectId()), true);
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(), defaultUseTime, 0));
                                PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 443));
                                ThreadPoolManager.getInstance().schedule(new Runnable(){
                                    @Override
                                    public void run(){
                                        TeleportService.teleportTo(player, 300170000, 528.27496f, 1345.001f, 223.52919f, 14);
                                    }
                                }, 35000);
                            }
                        }, defaultUseTime);
                    }
                }, 0);
            return;
            }
        }
    }
    
    @Override
    public void onDialogSelect(int dialogId, final Player player, int questId)
    {
        Npc npc = getOwner();

        if (dialogId == 10000 && (npc.getNpcId() == 799517))//Boatman
        {
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 448));
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            ThreadPoolManager.getInstance().schedule(new Runnable(){
                @Override
                        public void run(){
                            TeleportService.teleportTo(player, 300170000, 958.45233f, 430.4892f, 219.80301f, 0);
                        }
            }, 23000);
                return;
        }
        else if (dialogId == 10000 && (npc.getNpcId() == 799518))//Boatman
        {
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 449));
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
                    public void run(){
                        TeleportService.teleportTo(player, 300170000, 822.0199f, 465.1819f, 220.29918f, 0);
                    }
        }, 23000);
                return;
        }
        else if (dialogId == 10000 && (npc.getNpcId() == 799519))//Boatman
        {
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 450));
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
                    public void run(){
                        TeleportService.teleportTo(player, 300170000, 777.1054f, 300.39005f, 219.89926f, 94);
                    }
        }, 23000);
                return;
        }
        else if (dialogId == 10000 && (npc.getNpcId() == 799520))//Boatman
        {
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 451));
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
                    public void run(){
                        TeleportService.teleportTo(player, 300170000, 942.3092f, 270.91855f, 219.86185f, 86);
                    }
        }, 23000);
                return;
        }
    }
}