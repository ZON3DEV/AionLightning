package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_FIND_GROUP;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.LGFService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.World;


/**
 * 
 * @author ginho1
 * 
 */
public class CM_FIND_GROUP extends AionClientPacket
{
	private int type;
	private int playerID;
	private String applyString;
	private int groupType;
	@SuppressWarnings("unused")
	private int groupID;
	
	public CM_FIND_GROUP(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		type = readC();//0 recruit group members 4 apply for group

		switch(type)
		{
			//remove recruit group
			case 1:
				playerID = readD();
				groupID = readD();
				applyString = readS();
				groupType = readC();
			break;
			//send recruit group
			case 2:
				playerID = readD();
				applyString = readS();
				groupType = readC();
			break;
			//update recruit group or apply for group
			case 3:
				playerID = readD();
				groupID = readD();
				applyString = readS();
				groupType = readC();			
			break;
			//remove apply for group
			case 5:
				playerID = readD();
			break;
			//send apply for group
			case 6:
				playerID = readD();
				applyString = readS();
				groupType = readC();
			break;
			//update apply for group
			case 7:
				playerID = readD();
				applyString = readS();
				groupType = readC();
			break;
		}
	}

	/**
	 * {@inheritDoc}n
	 */
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();

		switch(type)
		{
			//recruit group
			case 0:
				PacketSendUtility.sendPacket(player, new SM_FIND_GROUP(type, player));
			break;
			//remove recruit group
			case 1:
				if(player.getObjectId() == playerID)
				{
					LGFService.getInstance().removeRecruitGroup(playerID);

					World.getInstance().doOnAllPlayers(new Executor<Player>(){
						@Override
						public boolean run(Player p)
						{
							if(p.getCommonData().getRace() == player.getCommonData().getRace())
							{
								PacketSendUtility.sendPacket(p, new SM_FIND_GROUP(type, player));
							}
							return true;
						}
					});
				}
			break;
			//send recruit group
			case 2:
				if(!player.isInGroup())
					return;

				if(LGFService.getInstance().addRecruitGroup(playerID, applyString, groupType, 55, player))
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400392));
				else
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400394));
			break;
			//send update recruit group
			case 3:
				if(player.getObjectId() == playerID)
				{
					LGFService.getInstance().removeRecruitGroup(playerID);
					LGFService.getInstance().addRecruitGroup(playerID, applyString, groupType, 55, player);
				}
			break;
			//apply for group
			case 4:
				PacketSendUtility.sendPacket(player, new SM_FIND_GROUP(type, player));
			break;
			//remove apply for group
			case 5:
				if(player.getObjectId() == playerID)
				{
					LGFService.getInstance().removeApplyGroup(playerID);

					World.getInstance().doOnAllPlayers(new Executor<Player>(){
						@Override
						public boolean run(Player p)
						{
							if(p.getCommonData().getRace() == player.getCommonData().getRace())
							{
								PacketSendUtility.sendPacket(p, new SM_FIND_GROUP(type, player));
							}
							return true;
						}
					});
				}
			break;
			//send apply for group
			case 6:
				if(LGFService.getInstance().addApplyGroup(playerID, applyString, groupType, player))
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400393));
				else
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400394));
			break;
			//update apply for group
			case 7:
				if(player.getObjectId() == playerID)
				{
					LGFService.getInstance().removeApplyGroup(playerID);
					LGFService.getInstance().addApplyGroup(playerID, applyString, groupType, player);
				}
			break;
		}
	}
}
