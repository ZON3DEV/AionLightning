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

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


import javolution.util.FastList;


import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;
import com.sun.org.apache.xpath.internal.XPathAPI;

/**
 * @Original author ATracer - Kamui
 */

/**
 * @author editor Magenik
 */


public class NpcSpawnExporter
{
	private FastList<DataPacket> packets;
	private FastList<NpcSpawn> spawns;
	private String sessionName;

	private int worldId = -1;

	public NpcSpawnExporter(List<DataPacket> packets, String sessionName)
	{
		this.packets = new FastList<DataPacket>(packets);
		this.sessionName = sessionName;
		this.spawns = new FastList<NpcSpawn>();
	}

	public void parse()
	{
		String filename = "npc_spawns_" + sessionName + ".xml";
		Long start = System.currentTimeMillis();

		try
		{
			DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder build = dFact.newDocumentBuilder();
			Document doc = build.newDocument();
			Element root = doc.createElement("spawns");
			doc.appendChild(root);

			// Collect info about all seen NPCs
			for (DataPacket packet : packets)
			{
				String packetName = packet.getName();

				if ("SM_PLAYER_SPAWN".equals(packetName))
					this.worldId = Integer.parseInt(packet.getValuePartList().get(1).readValue());
				else if ("SM_NPC_INFO".equals(packetName))
				{
					NpcSpawn spawn = new NpcSpawn();
					FastList<ValuePart> valuePartList = new FastList<ValuePart>(packet.getValuePartList());
					for (ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if ("x".equals(partName))
							spawn.x = Float.parseFloat(valuePart.readValue());
						else if ("y".equals(partName))
							spawn.y = Float.parseFloat(valuePart.readValue());
						else if ("z".equals(partName))
							spawn.z = Float.parseFloat(valuePart.readValue());
						else if ("npcHeading".equals(partName))
							spawn.npcHeading = Byte.parseByte(valuePart.readValue());
						else if ("objectId".equals(partName))
							spawn.objectId = Long.parseLong(valuePart.readValue());
						else if ("npcId".equals(partName))
							spawn.npcId = Integer.parseInt(valuePart.readValue());
						else if ("spawnStaticId".equals(partName))
							spawn.spawnStaticId = Integer.parseInt(valuePart.readValue());
						spawn.worldId = this.worldId;
					}
					boolean exists = false;
					for (NpcSpawn n : spawns)
						if (n.objectId == spawn.objectId)
							exists = true;
					if (!exists)
						spawns.add(spawn);
				}
			}

			for (NpcSpawn n : spawns)
			{
				// Testing new spawn
				Element spawnmap = doc.createElement("spawn_map");
				spawnmap = (Element) XPathAPI.selectNodeList(doc, String.format("//spawn_map[@map_id='%d']", n.worldId)).item(0);
				if (spawnmap == null)
				{
					spawnmap = doc.createElement("spawn_map");
					spawnmap.setAttribute("map_id", "" + n.worldId);
					root.appendChild(spawnmap);
				}
				
				// Find or create a new spawn
				Element spawn;
				spawn = (Element) XPathAPI.selectNodeList(doc, String.format("//spawn[@npc_id='%d']", n.npcId)).item(0);

				if (spawn == null)
				{
					spawn = doc.createElement("spawn");
					spawn.setAttribute("npc_id", "" + n.npcId);
					spawnmap.appendChild(spawn);
				}

				if (XPathAPI.selectNodeList(spawn,
					String.format("//object[@x='%f' and @y='%f' and @z='%f']", n.x, n.y, n.z)).getLength() > 0)
				continue;
				Element object = doc.createElement("spot");
				if (n.spawnStaticId != 0)
				object.setAttribute("h", "" + n.npcHeading);
				object.setAttribute("walker_id", "" + n.objectId);
				object.setAttribute("x", "" + n.x);
				object.setAttribute("y", "" + n.y);
				object.setAttribute("z", "" + n.z);

				spawn.appendChild(object);
			}
			Transformer serializer = TransformerFactory.newInstance().newTransformer();
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
			serializer.transform(new DOMSource(doc), new StreamResult(new File(filename)));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		PacketSamurai.getUserInterface().log(
		"The NPC Spawns Data Has Been Written Successful in " + ((float) (System.currentTimeMillis() - start) / 1000) + "s");
	} 

	class NpcSpawn
	{
		float x;
		float y;
		float z;
		byte npcHeading;
		long objectId;
		int npcId;
		int worldId;
		int spawnStaticId;
	}
}