package quest.morheim;
import java.util.Collection;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.WorldMapInstance;


/**
 * @author Atomics @edit by Mcrizza
 * Completed by Bio/Dreamworks -.-!
 */
public class _2041HoldTheFrontLine extends QuestHandler
{
	
	private final static int	questId	= 2041;
	private final static int[]	npcIds	= { 204301, 204403, 204432, 700183 };
	private int instanceId = 0;
	private Creature kargateNpc;
	
	public _2041HoldTheFrontLine()
	{
		super( questId );
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.addOnDie(questId);
		for(int npcId : npcIds)
			qe.setNpcQuestData( npcId ).addOnTalkEvent( questId );
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		int [] quests = {2031, 2032, 2033, 2034, 2035, 2036, 2037, 2038, 2039, 2040};
		return defaultQuestOnLvlUpEvent(env, quests, true);
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		final int var = qs.getQuestVarById( 0 );
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if( qs.getStatus() == QuestStatus.REWARD )
		{
			if( targetId == 204301 ) {
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 2375);
				return defaultQuestEndDialog(env);
			}
		}
		else if( qs.getStatus() != QuestStatus.START )
			return false;

		switch( targetId )
		{
			case 204301:
				switch( env.getDialogId() )
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1011);
					case 10000:
						if(var == 0)
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							return sendQuestDialog(env, 0);
						}
					default:
						return false;
				}
			case 204403:
				switch( env.getDialogId() )
				{
					case 26:
						if(var == 1)
							return sendQuestDialog(env, 1352);
					case 10001:
						if(var == 1)
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							return sendQuestDialog(env, 0);
						}
					default:
						return false;
				}
			case 204432:
				switch( env.getDialogId() )
				{
					case 26:
						if(var == 2)
							return sendQuestDialog(env, 1693);
						else if(var == 4)
							return sendQuestDialog(env, 2034);
					case 10002:
						if(var == 2 && player.getPlayerGroup() != null)
						{
							updateParty(player, 2, 3, env);
							kargateNpc = (Creature) env.getVisibleObject();
							
							ThreadPoolManager.getInstance().schedule(new Runnable()
							{
								@Override
								public void run()
								{
									if(!kargateNpc.getLifeStats().isAlreadyDead())
									{
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213575, (float) 242.6, (float) 267, (float) 229.5, (byte) 93, true))), (float) 275, (float) 181.75, (float) 204.78);
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213575, (float) 259.6, (float) 268.24, (float) 229.37, (byte) 93, true))), (float) 268.17, (float) 182.61, (float) 205.14);
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213576, (float) 255.15, (float) 264.7, (float) 228.66, (byte) 93, true))), (float) 274.54, (float) 187.76, (float) 205.71); 
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213576, (float) 249.38, (float) 262.78, (float) 228.6, (byte) 93, true))), (float) 269, (float) 188.9, (float) 206.1);
									}
									else
										updateParty(player, -1, 0, env);
										
								}
							}, 1000);
							
							ThreadPoolManager.getInstance().schedule(new Runnable()
							{
								@Override
								public void run()
								{
									if(!kargateNpc.getLifeStats().isAlreadyDead())
									{
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213575, (float) 242.6, (float) 267, (float) 229.5, (byte) 93, true))), (float) 275, (float) 181.75, (float) 204.78);
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213578, (float) 259.6, (float) 268.24, (float) 229.37, (byte) 93, true))), (float) 268.17, (float) 182.61, (float) 205.14);
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213576, (float) 255.15, (float) 264.7, (float) 228.66, (byte) 93, true))), (float) 274.54, (float) 187.76, (float) 205.71);
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213576, (float) 249.38, (float) 262.78, (float) 228.6, (byte) 93, true))), (float) 269, (float) 188.9, (float) 206.1);
									}
									else
										updateParty(player, -1, 0, env);
								}
							}, 61000);
							
							ThreadPoolManager.getInstance().schedule(new Runnable()
							{
								@Override
								public void run()
								{
									if(!kargateNpc.getLifeStats().isAlreadyDead())
									{
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213575, (float) 242.6, (float) 267, (float) 229.5, (byte) 93, true))), (float) 275, (float) 181.75, (float) 204.78);
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213578, (float) 259.6, (float) 268.24, (float) 229.37, (byte) 93, true))), (float) 268.17, (float) 182.61, (float) 205.14);
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213579, (float) 255.15, (float) 264.7, (float) 228.66, (byte) 93, true))), (float) 274.54, (float) 187.76, (float) 205.71);
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213576, (float) 249.38, (float) 262.78, (float) 228.6, (byte) 93, true))), (float) 269, (float) 188.9, (float) 206.1);
									}
									else
										updateParty(player, -1, 0, env);
								}
							}, 121000);
							
							ThreadPoolManager.getInstance().schedule(new Runnable()
							{
								@Override
								public void run()
								{
									if(!kargateNpc.getLifeStats().isAlreadyDead())
									{
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213577, (float) 242.6, (float) 267, (float) 229.5, (byte) 93, true))), (float) 275, (float) 181.75, (float) 204.78);
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213578, (float) 259.6, (float) 268.24, (float) 229.37, (byte) 93, true))), (float) 268.17, (float) 182.61, (float) 205.14);
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213579, (float) 255.15, (float) 264.7, (float) 228.66, (byte) 93, true))), (float) 274.54, (float) 187.76, (float) 205.71);
										moveTo(((Creature)(QuestService.addNewSpawn(320040000, player.getInstanceId(), 213576, (float) 249.38, (float) 262.78, (float) 228.6, (byte) 93, true))), (float) 269, (float) 188.9, (float) 206.1);
									}
									else
										updateParty(player, -1, 0, env);
								}
							}, 181000);

							ThreadPoolManager.getInstance().schedule(new Runnable()
							{
								@Override
								public void run()
								{
									if(!kargateNpc.getLifeStats().isAlreadyDead())
										updateParty(player, 3, 4, env);
									else
										updateParty(player, -1, 0, env);
								}
							}, 240000);
							return sendQuestDialog(env, 0);
						}
					case 10003:
						if(var == 4)
						{
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 0);
						}
					default:
						return false;
				}
			case 700183:
				if(var == 2 && player.getPlayerGroup() != null )
				{
					final int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
					//create instance
					WorldMapInstance newInstance = InstanceService.getRegisteredInstance(320040000, player.getPlayerGroup().getGroupId());
					if (newInstance == null)
					{
						newInstance = InstanceService.getNextAvailableInstance(320040000);
						InstanceService.registerGroupWithInstance(newInstance, player.getPlayerGroup());
						instanceId = newInstance.getInstanceId();
					}
					
					ThreadPoolManager.getInstance().schedule(new Runnable()
					{
						@Override
						public void run()
						{
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
							updateQuestStatus(env);
							TeleportService.teleportTo(player, 320040000, instanceId,(float) 274.51,(float) 168.6,(float) 204.3, 0);
						}
					}, 3000);
					return false;
				}
				else if (var == 2 && player.getPlayerGroup() == null)//TODO: find proper message, just temp fix
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENTER_ONLY_PARTY_DON);
				default:
					return false;
		}
					
	}
	
	@Override
	public boolean onDieEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		int var = qs.getQuestVars().getQuestVars();
		if(var == 3 || var == 4)
		{
			qs.setQuestVar(2);
			updateQuestStatus(env);
		}

		return false;
	}
	
		@Override
	public boolean onEnterWorldEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;
		
		if(qs.getStatus() == QuestStatus.START)
		{
			int var = qs.getQuestVars().getQuestVars();
			if(var == 3 || var == 4)
			{
				if(player.getWorldId() != 320040000)
				{
					qs.setQuestVar(2);
					updateQuestStatus(env);
				}
			}
		}
		
		return false;
	}
	
	private void updateParty(Player player, int reqVar, int var, QuestCookie env)
	{							
		Collection<Player> members = player.getPlayerGroup().getMembers();
		for (Player member : members)
		{
			if (member.getWorldId() == 320040000)
			{
				QuestState qs1 = member.getQuestStateList().getQuestState(questId);
				if (qs1 != null && qs1.getStatus() == QuestStatus.START && (qs1.getQuestVarById(0) == reqVar || qs1.getQuestVarById(0) == -1))
				{
					if(var == 3)
					{
						if(player == member)
							QuestService.questTimerStart(env, 242);// Real timer
						else
							PacketSendUtility.sendPacket(member, new SM_QUEST_ACCEPTED(questId, 242));// Dummy timer
					}
					else if(var == 4 || reqVar == -1)
					{						
						if(player == member)
							QuestService.questTimerEnd(env);
						else
							PacketSendUtility.sendPacket(member, new SM_QUEST_ACCEPTED(questId, 0));
						
						if(var == 4)
							PacketSendUtility.sendPacket(member, new SM_PLAY_MOVIE(0, 158));
					}

					qs1.setStatus(QuestStatus.START);
					qs1.setQuestVar(var);
					PacketSendUtility.sendPacket(member, new SM_QUEST_ACCEPTED(2, questId, qs1.getStatus(), qs1.getQuestVars().getQuestVars()));
				}
			}
		}
	}

	private void moveTo(final Creature Attacker, float x, float y, float z)
	{
		PacketSendUtility.broadcastPacket((Npc) Attacker, new SM_EMOTION((Npc) Attacker, EmotionType.ATTACKMODE));
		Attacker.getMoveController().setNewDirection(x, y, z);
		Attacker.getMoveController().schedule();

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				if(Attacker != null && !Attacker.getLifeStats().isAlreadyDead())
					Attacker.getMoveController().stop();
			}
		}, 25000);
	}
}
