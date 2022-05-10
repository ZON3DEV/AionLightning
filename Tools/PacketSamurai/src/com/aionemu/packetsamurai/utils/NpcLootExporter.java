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



package com.aionemu.packetsamurai.utils;

import java.util.List;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.SortedMap;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;

/**
 * @author Magenik
 */

public class NpcLootExporter {
	private List<DataPacket> packets;
	private String sessionName;
	private SortedMap<String, String> npcIdMap = new TreeMap<String, String>();
	private FastMap<String, FastList<Loot>> loots = new FastMap<String, FastList<Loot>>();

	public NpcLootExporter(List<DataPacket> packets, String sessionName) {
		this.packets = packets;
		this.sessionName = sessionName;
	}

	public void parse() {
		String fileName = "npc_loot_" + sessionName + ".xml";

		try {
			//Get NpcIDs
			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				if("SM_NPC_INFO".equals(name))
				{
					String objectId = "";
					String npcId = "";

					List<ValuePart> valuePartList = packet.getValuePartList();

					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("npcId".equals(partName))
						{
							npcId = valuePart.readValue();
						}
						else if("objectId".equals(partName))
						{
							objectId = valuePart.readValue();
						}
						
					}
					if(!"0".equals(objectId))
					{
						npcIdMap.put(objectId, npcId);
					}
				}
			}


			for (DataPacket packet : packets) {
				String packetName = packet.getName();

				if ("SM_LOOT_ITEMLIST".equals(packetName)) {
					Loot itemlist = new Loot();
					String objectId = "";
					FastList<ValuePart> valuePartList = new FastList<ValuePart>(packet.getValuePartList());
					for (ValuePart valuePart : valuePartList) {
						String partName = valuePart.getModelPart().getName();
						if ("itemId".equals(partName))
							itemlist.itemId = Integer.parseInt(valuePart.readValue());
						else if ("itemCount".equals(partName))
							itemlist.itemCount = Integer.parseInt(valuePart.readValue());
						else if ("objectId".equals(partName))
							objectId = valuePart.readValue();
					}
					if(!loots.containsKey(objectId) && !objectId.equals(""))
						loots.put(objectId, new FastList<Loot>());
					loots.get(objectId).add(itemlist);
				}
			}


			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));

			for (FastMap.Entry<String, FastList<Loot>> e = loots.head(), end = loots.tail(); (e = e.getNext()) != end;)
			{
				String npcId = "";
				String objectId = e.getKey();

				for(Entry<String, String> entry : npcIdMap.entrySet())
				{
					if(entry.getKey().equals(objectId))
						npcId = entry.getValue();
				}

				if(!npcId.equals(""))
				{
					StringBuilder sb = new StringBuilder();

					
					sb.append("	<!-- npcid= ").append(npcId).append(" -->\n");
					sb.append("	<npc_drops npcid=\"").append(npcId).append("\" objectId=\"").append(e.getKey()).append("\">\n");
					sb.append("		<drop_group>\n");

					for (int i = 1; i <= e.getValue().size(); i++)
					{
						Loot lsLoot = e.getValue().get(i - 1);
						sb.append("			<drop count=\"").append(i).append("\" item_id=\"").append(lsLoot.itemId).append("\" item_quantity=\"").append(lsLoot.itemCount).append("\" />\n");
					}

					sb.append("		</drop_group>\n");
					sb.append("	</npc_drops>\n");

					out.write(sb.toString());
				}

			}

			out.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		PacketSamurai.getUserInterface().log("NPC Loot Has Been Written Successful");
	}

	class Loot {
		int itemId;
		int itemCount;
	}
}