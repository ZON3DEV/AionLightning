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

import javolution.util.FastList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;
import com.sun.org.apache.xpath.internal.XPathAPI;

/**
 * @author Magenik
 */

public class NpcGatherExporter {
	private FastList<DataPacket> packets;
	private FastList<GatherSpawn> spawns;
	private String sessionName;

	private int worldId = -1;

	public NpcGatherExporter(List<DataPacket> packets, String sessionName) {
		this.packets = new FastList<DataPacket>(packets);
		this.sessionName = sessionName;
		this.spawns = new FastList<GatherSpawn>();
	}

	public void parse() {
		String filename = "npc_gather_spawns_" + sessionName + ".xml";
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
				else if ("SM_GATHERABLE_INFO".equals(packetName))
				{
					GatherSpawn spawn = new GatherSpawn();
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
						else if ("templateId".equals(partName))
							spawn.templateId = Integer.parseInt(valuePart.readValue());
						else if ("staticId".equals(partName))
							spawn.staticId = Integer.parseInt(valuePart.readValue());
						else if ("objectId".equals(partName))
							spawn.objectId = Long.parseLong(valuePart.readValue());
						else if ("npcHeading".equals(partName))
							spawn.heading = Byte.parseByte(valuePart.readValue());
						spawn.worldId = this.worldId;
					}
					boolean exists = false;
					for (GatherSpawn n : spawns)
						if (n.objectId == spawn.objectId)
							exists = true;
					if (!exists)
						spawns.add(spawn);
				}
			}

			for (GatherSpawn n : spawns) {
				// Find or create a new spawn
				Element spawn;
				spawn = (Element) XPathAPI.selectNodeList(doc,
						String.format("//spawn[@map='%d' and @templateId='%d']", n.worldId, n.templateId)).item(0);
				if (spawn == null)
				{
					spawn = doc.createElement("spawn");
					spawn.setAttribute("map", "" + n.worldId);
					spawn.setAttribute("templateId", "" + n.templateId);
					root.appendChild(spawn);
				}
				if (XPathAPI.selectNodeList(spawn,
						String.format("//object[@x='%f' and @y='%f' and @z='%f']", n.x, n.y, n.z)).getLength() > 0)
					continue;
				Element object = doc.createElement("object");
				if(n.staticId != 0)
				object.setAttribute("static_id", "" + n.staticId);
				object.setAttribute("h", "" + n.heading);
				object.setAttribute("z", "" + n.z);
				object.setAttribute("y", "" + n.y);
				object.setAttribute("x", "" + n.x);

				spawn.appendChild(object);
				spawn.setAttribute("pool", "" + spawn.getChildNodes().getLength());
				spawn.setAttribute("interval", "" + 295);
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
				"The NPC Gather Has Been Written Successful in " + ((float) (System.currentTimeMillis() - start) / 1000) + "s");
	}

	class GatherSpawn
	{
		float x;
		float y;
		float z;
		byte heading;
		int templateId;
		int worldId;
		long objectId;
		int staticId;
	}
}
