package org.openaion.gameserver.controllers;

import java.util.List;
import java.util.concurrent.Future;

import org.openaion.gameserver.controllers.movement.ActionObserver;
import org.openaion.gameserver.controllers.movement.StartMovingListener;
import org.openaion.gameserver.controllers.movement.ActionObserver.ObserverType;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.ChatType;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.chest.ChestTemplate;
import org.openaion.gameserver.model.templates.chest.KeyItem;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.services.DropService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author Dns (base AL)
 * 
 */
public class ChestController extends NpcController
{
	ChestTemplate	chestTemplate;
	private Player lastActor = null;
	private ChestState state = ChestState.IDLE;
	private Future<?> openingTask;
	
	private final int defaultUseTime = 3000;
	
	private enum ChestState
	{
		IDLE,
		OPENING
	}
	
	

	@Override
	public void setOwner(Creature owner)
	{
		super.setOwner(owner);
		chestTemplate = DataManager.CHEST_DATA.getChestTemplate(owner.getObjectTemplate().getTemplateId());
	}

	@Override
	public void onDialogRequest(final Player player)
	{
		if(chestTemplate == null || state == ChestState.OPENING)
			return;

		final List<KeyItem> keyItems = chestTemplate.getKeyItem();
		for(KeyItem keyItem : keyItems)
		{
			if(keyItem.getItemId() != 0 && player.getInventory().getItemCountByItemId(keyItem.getItemId()) < 1)
			{
				PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player,
					"I need a key to open this chest ! ", ChatType.NORMAL), true);
				return;
			}
		}
		
		state = ChestState.OPENING;
		
		player.getObserveController().attach(new StartMovingListener(){
			@Override
			public void moved()
			{
				cancelOpening(player);
			}
		});

		player.getObserveController().attach(new ActionObserver(ObserverType.ATTACKED){
			@Override
			public void attacked(Creature creature)
			{		
				cancelOpening(player);
			}
		});
		
		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
			defaultUseTime, 1));
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getOwner()
			.getObjectId()), true);
		
		openingTask = ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
					defaultUseTime, 0));
				getOwner().setTarget(player);
				lastActor = player;
				onDie(player);
				// Remove keys
				for(KeyItem keyItem : keyItems)
					if(keyItem.getItemId() != 0)
						player.getInventory().removeFromBagByItemId(keyItem.getItemId(), keyItem.getQuantity());				
			}
		}, defaultUseTime);
		
	}
	
	@Override
	public void doReward()
	{
		if (lastActor == null || getOwner() == null)
			return;
		
		DropService.getInstance().registerDrop(getOwner() , lastActor, lastActor.getLevel());
		DropService.getInstance().requestDropList(lastActor, getOwner().getObjectId());
		lastActor = null;
	}
	
	private void cancelOpening(Player player)
	{
		state = ChestState.IDLE;
		openingTask.cancel(false);
		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
			defaultUseTime, 0));
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0,
			getOwner().getObjectId()), true);
	}
}
