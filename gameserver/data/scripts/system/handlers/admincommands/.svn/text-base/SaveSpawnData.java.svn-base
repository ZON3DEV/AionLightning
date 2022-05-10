/*
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
package admincommands;

import java.io.File;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.SpawnsData;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.WorldMapTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnGroup;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;
import org.xml.sax.SAXException;


/**
 * @author Luno
 * @author ATracer (xml version)
 * 
 */

public class SaveSpawnData extends AdminCommand
{

	private static Logger log = Logger.getLogger(SaveSpawnData.class);

	public SaveSpawnData()
	{
		super("save_spawn");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_SAVESPAWNDATA)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try
		{
			schema = sf.newSchema(new File("./data/static_data/spawns/spawns.xsd"));
		}
		catch (SAXException e1)
		{
			log.error(e1.getCause());
			PacketSendUtility.sendMessage(admin, "Unexpected error occured during saving");
			return;
		}
		
		boolean isAllSave = params.length == 1 && "all".equalsIgnoreCase(params[0]);
		
		SpawnsData spawnsData = DataManager.SPAWNS_DATA;
		for (WorldMapTemplate template : DataManager.WORLD_MAPS_DATA)
		{
			List<SpawnGroup> spawnsForWorld = null;
			if (isAllSave)
				spawnsForWorld = spawnsData.getSpawnsForWorld(template.getMapId());
			else
				spawnsForWorld = spawnsData.getNewSpawnsForWorld(template.getMapId());

			if (spawnsForWorld != null && spawnsForWorld.size() > 0)
			{
				SpawnsData data = new SpawnsData();
				data.getSpawnGroups().addAll(spawnsForWorld);

				File xml = new File("./data/static_data/spawns/new/" + template.getMapId() + ".xml");
				
				JAXBContext jc;
				Marshaller marshaller;
				try
				{
					jc = JAXBContext.newInstance(SpawnsData.class);
					marshaller = jc.createMarshaller();
					marshaller.setSchema(schema);
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					marshaller.marshal(data, xml);
				}
				catch (JAXBException e)
				{
					log.error(e.getCause());
					PacketSendUtility.sendMessage(admin, "Unexpected error occured during saving");
					return;
				}
			}
		}
		PacketSendUtility.sendMessage(admin, "Spawn data was saved into /static_data/spawns/new folder");
	}
}
