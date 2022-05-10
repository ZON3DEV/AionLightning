/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion;

import java.nio.ByteBuffer;
import java.util.List;

import org.openaion.gameserver.model.account.PlayerAccountData;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.PlayerAppearance;
import org.openaion.gameserver.model.gameobjects.player.PlayerCommonData;
import org.openaion.gameserver.model.items.GodStone;
import org.openaion.gameserver.model.items.ItemSlot;


/**
 * 
 * @author AEJTester
 * @author Nemesiss
 * @author Niato
 */
public abstract class PlayerInfo extends AionServerPacket
{
	protected PlayerInfo()
	{

	}

	protected void writePlayerInfo(ByteBuffer buf, PlayerAccountData accPlData)
	{
		PlayerCommonData pbd = accPlData.getPlayerCommonData();
		final int raceId = pbd.getRace().getRaceId();
		final int genderId = pbd.getGender().getGenderId();
		final PlayerAppearance playerAppearance = accPlData.getAppereance();
		writeD(buf, pbd.getPlayerObjId());
		writeS(buf, pbd.getName());
		writeB(buf, new byte[52 - (pbd.getName().length() * 2 + 2)]);
		writeD(buf, genderId);
		writeD(buf, raceId);
		writeD(buf, pbd.getPlayerClass().getClassId());

		writeD(buf, playerAppearance.getVoice());

		writeD(buf, playerAppearance.getSkinRGB());
		writeD(buf, playerAppearance.getHairRGB());
		writeD(buf, playerAppearance.getEyeRGB());
		writeD(buf, playerAppearance.getLipRGB());

		writeC(buf, playerAppearance.getFace());
		writeC(buf, playerAppearance.getHair());
		writeC(buf, playerAppearance.getDecoration());
		writeC(buf, playerAppearance.getTattoo());
		
		writeC(buf, playerAppearance.getFaceContour());
		writeC(buf, playerAppearance.getExpression());

		writeC(buf, 6);// always 6 - 2.5.x

		writeC(buf, playerAppearance.getJawLine());

		writeC(buf, playerAppearance.getForehead());

		writeC(buf, playerAppearance.getEyeHeight());
		writeC(buf, playerAppearance.getEyeSpace());
		writeC(buf, playerAppearance.getEyeWidth());
		writeC(buf, playerAppearance.getEyeSize());
		writeC(buf, playerAppearance.getEyeShape());
		writeC(buf, playerAppearance.getEyeAngle());

		writeC(buf, playerAppearance.getBrowHeight());
		writeC(buf, playerAppearance.getBrowAngle());
		writeC(buf, playerAppearance.getBrowShape());

		writeC(buf, playerAppearance.getNose());
		writeC(buf, playerAppearance.getNoseBridge());
		writeC(buf, playerAppearance.getNoseWidth());
		writeC(buf, playerAppearance.getNoseTip());

		writeC(buf, playerAppearance.getCheeks());
		writeC(buf, playerAppearance.getLipHeight());
		writeC(buf, playerAppearance.getMouthSize());
		writeC(buf, playerAppearance.getLipSize());
		writeC(buf, playerAppearance.getSmile());
		writeC(buf, playerAppearance.getLipShape());

		writeC(buf, playerAppearance.getChinHeight());
		writeC(buf, playerAppearance.getCheekBones());

		writeC(buf, playerAppearance.getEarShape());
		writeC(buf, playerAppearance.getHeadSize());

		writeC(buf, playerAppearance.getNeck());
		writeC(buf, playerAppearance.getNeckLength());
		writeC(buf, playerAppearance.getShoulderSize());

		writeC(buf, playerAppearance.getTorso());
		writeC(buf, playerAppearance.getChest());
		writeC(buf, playerAppearance.getWaist());
		writeC(buf, playerAppearance.getHips());

		writeC(buf, playerAppearance.getArmThickness());
		writeC(buf, playerAppearance.getHandSize());
		
		writeC(buf, playerAppearance.getLegThickness());
		writeC(buf, playerAppearance.getFootSize());
		
		writeC(buf, playerAppearance.getFacialRatio());
		writeC(buf, 0x00); // 0x00
		
		writeC(buf, playerAppearance.getArmLength());
		writeC(buf, playerAppearance.getLegLength());
		
		writeC(buf, playerAppearance.getShoulders());
		writeC(buf, playerAppearance.getFaceShape());
		
		writeC(buf, 0x00); // always 0 may be acessLevel
		
		writeC(buf, 0x00); // always 0 - unk -17
		writeC(buf, 0x00); // added 119

		writeF(buf, playerAppearance.getHeight());
		int raceSex = 100000 + raceId * 2 + genderId;
		writeD(buf, raceSex);
		writeD(buf, pbd.getPosition().getMapId());//mapid for preloading map
		writeF(buf, pbd.getPosition().getX());
		writeF(buf, pbd.getPosition().getY());
		writeF(buf, pbd.getPosition().getZ());
		writeD(buf, pbd.getPosition().getHeading());
		writeD(buf, pbd.getLevel());// lvl confirmed
		writeD(buf, pbd.getTitleId());

		if(!accPlData.isLegionMember())
		{
			byte[] somebyte = new byte[92];
			writeB(buf, somebyte);
		}else{
			writeD(buf, accPlData.getLegion().getLegionId());
			writeS(buf, accPlData.getLegion().getLegionName());
			byte[] somebyte = new byte[86/*-2byte*/- (accPlData.getLegion().getLegionName().length() * 2)];
			writeB(buf, somebyte);
		}

		int itemsDataSize = 0;

		List<Item> items = accPlData.getEquipment();

		for(Item item : items)
		{
			if(itemsDataSize >= 208)
				break;

			if(item.getItemTemplate().isArmor() || item.getItemTemplate().isWeapon())
			{

				if(item.getItemTemplate().getItemSlot() <= ItemSlot.PANTS.getSlotIdMask())
				{
					writeC(buf, 1);
					writeD(buf, item.getItemSkinTemplate().getTemplateId());
					GodStone godStone = item.getGodStone();
					writeD(buf, godStone != null ? godStone.getItemId() : 0);
					writeD(buf, item.getItemColor());

					itemsDataSize += 13;
				}
			}
		}

		writeB(buf, new byte[208 - itemsDataSize]);
		writeD(buf, accPlData.getDeletionTimeInSeconds());
		writeD(buf, 0x00);//display helmet 0 show, 10005 dont show
	}
}
