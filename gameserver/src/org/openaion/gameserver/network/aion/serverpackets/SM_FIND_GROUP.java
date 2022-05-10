package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.Collection;

import org.openaion.gameserver.model.gameobjects.LFGApplyGroup;
import org.openaion.gameserver.model.gameobjects.LFGRecruitGroup;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.LGFService;


/**
 * @author ginho1
 */
public class SM_FIND_GROUP extends AionServerPacket
{
	private int type;
	private Player player;
	
	public SM_FIND_GROUP(int type, Player player)
	{
		this.type = type;
		this.player = player;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, type);

		switch(type)
		{
			//recruit group
			case 0:
				Collection<LFGRecruitGroup> playerRecruitGroups = LGFService.getInstance().geRecruitGroup(player.getCommonData().getRace());

				writeH(buf, playerRecruitGroups.size());//count1
				writeH(buf, playerRecruitGroups.size());//count2

				writeD(buf, (int)System.currentTimeMillis());

				for(LFGRecruitGroup playerRecruitGroup : playerRecruitGroups)
				{
					Player pl = playerRecruitGroup.getPlayer();
					writeD(buf, pl.getObjectId());//playerID

					if(pl.isInGroup())
						writeD(buf, pl.getPlayerGroup().getGroupId());
					else
						writeD(buf, 0);
					
					writeC(buf, playerRecruitGroup.getGroupType());//0 to group 1 to alliance
					writeS(buf, playerRecruitGroup.getApplyString());
					writeS(buf, pl.getName());//playerName
					writeC(buf, pl.getPlayerGroup().getMembers().size());//groupSize
					writeC(buf, pl.getLevel());//level
					writeC(buf, playerRecruitGroup.getMaxLevel());//max level
					writeD(buf, (int)playerRecruitGroup.getCreationTime());

					//expire in 1 hour
					if((System.currentTimeMillis() - playerRecruitGroup.getCreationTime()) > 3600000)
						LGFService.getInstance().removeApplyGroup(pl.getObjectId());
				}
			break;
			//remove recruit group
			case 1:
					writeD(buf, player.getObjectId());
					if(player.isInGroup())
						writeD(buf, player.getPlayerGroup().getGroupId());
					else
						writeD(buf, 0);
					writeC(buf, 0);
					writeH(buf, 1);
			break;
			//apply for group
			case 4:
				Collection<LFGApplyGroup> playerApplyGroups = LGFService.getInstance().geApplyGroup(player.getCommonData().getRace());

				writeH(buf, playerApplyGroups.size());//count1
				writeH(buf, playerApplyGroups.size());//count2

				writeD(buf, (int)System.currentTimeMillis());

				for(LFGApplyGroup playerApplyGroup : playerApplyGroups)
				{
					Player pl = playerApplyGroup.getPlayer();
					writeD(buf, pl.getObjectId());//playerID
					writeC(buf, playerApplyGroup.getGroupType());//0 to group 1 to alliance
					writeS(buf, playerApplyGroup.getApplyString());
					writeS(buf, pl.getName());//playerName
					writeC(buf, pl.getPlayerClass().getClassId());//class
					writeC(buf, pl.getLevel());//level
					writeD(buf, (int)playerApplyGroup.getCreationTime());

					//expire in 1 hour
					if((System.currentTimeMillis() - playerApplyGroup.getCreationTime()) > 3600000)
						LGFService.getInstance().removeApplyGroup(pl.getObjectId());
				}
			break;
			//remove apply for group
			case 5:
					writeD(buf, player.getObjectId());
					writeH(buf, 722);
					writeC(buf, 0);
			break;
		}
	}
}
