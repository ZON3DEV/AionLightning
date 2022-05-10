/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;

import static org.apache.commons.io.filefilter.FileFilterUtils.and;
import static org.apache.commons.io.filefilter.FileFilterUtils.makeSVNAware;
import static org.apache.commons.io.filefilter.FileFilterUtils.notFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.prefixFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.suffixFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.log4j.Logger;
import org.openaion.gameserver.GameServerError;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.NpcData;
import org.openaion.gameserver.dataholders.PortalData;
import org.openaion.gameserver.dataholders.QuestScriptsData;
import org.openaion.gameserver.dataholders.QuestsData;
import org.openaion.gameserver.dataholders.SkillData;
import org.openaion.gameserver.dataholders.SpawnsData;
import org.openaion.gameserver.dataholders.StaticData;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.templates.NpcTemplate;
import org.openaion.gameserver.model.templates.portal.PortalTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnGroup;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.skill.model.SkillTemplate;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;
import org.openaion.gameserver.utils.chathandlers.ChatHandlers;
import org.openaion.gameserver.world.World;
import org.xml.sax.SAXException;


/**
 * @author MrPoke
 * 
 */
public class Reload extends AdminCommand
{
	private static final Logger	log	= Logger.getLogger(Reload.class);

	public Reload()
	{
		super("reload");
	}
	
	public enum ReloadType
	{
		UNKNOWN,
		COMMAND,
		NPC,
		PORTAL,
		QUEST,
		SHOP,
		SKILL,
		SPAWN
	}
	
	private void sendSyntax(Player admin)
	{
		String message = "syntax: //reload <";
		for(ReloadType type : ReloadType.values())
		{
			message += type.name().toLowerCase() + "|";
		}
		message = message.substring(0, message.length() - 1);
		message += ">";
		PacketSendUtility.sendMessage(admin, message);
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_RELOAD)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params == null || params.length != 1)
		{
			sendSyntax(admin);
			return;
		}
		
		ReloadType reloadType = ReloadType.UNKNOWN;
		try
		{
			reloadType = ReloadType.valueOf(params[0].toUpperCase());
		}
		catch (Exception e)
		{ }
		
		switch(reloadType)
		{
			case COMMAND:
				try
				{
					ChatHandlers.getInstance().reloadChatHandlers();
					PacketSendUtility.sendMessage(admin, "Admin/user commands reloaded successfully!");
				}
				catch(GameServerError e)
				{
					PacketSendUtility.sendMessage(admin, "Admin/user commands failed to reload! Keeping last version ...");
				}
				break;
			case NPC:
				File npcXml = new File("./data/static_data/npcs/npc_templates.xml");
				List<NpcTemplate> npcTemplates = new ArrayList<NpcTemplate>();
				try
				{
					JAXBContext jc = JAXBContext.newInstance(StaticData.class);
					Unmarshaller un = jc.createUnmarshaller();
					un.setSchema(getSchema("./data/static_data/static_data.xsd"));
					
					NpcData data = (NpcData)un.unmarshal(npcXml);
					if(data != null && data.getTemplates() != null)
						npcTemplates.addAll(data.getTemplates());
				}
				catch(Exception e)
				{
					PacketSendUtility.sendMessage(admin, "NPC templates reload failed! Keeping last version ...");
					log.error(e);
					return;
				}
				
				if(npcTemplates.size() > 0)
				{
					DataManager.NPC_DATA.setTemplates(npcTemplates);
					
					PacketSendUtility.sendMessage(admin, "NPC templates reloaded successfuly, now updating World to reflect changes ...!");
					
					World.getInstance().doOnAllNpcs(new Executor<Npc>(){
						
						@Override
						public boolean run(Npc object)
						{
							NpcTemplate newTemplate = DataManager.NPC_DATA.getNpcTemplate(object.getNpcId());
							if(newTemplate != null)
							{
								object.setObjectTemplate(newTemplate);
							}
							return true;
						}
					}, true);
					
					PacketSendUtility.sendMessage(admin, "Complete !");
					
				}
				else
					PacketSendUtility.sendMessage(admin, "NPC templates reload failed! Keeping last version ...");
				
				break;
			case PORTAL:
				File portalDir = new File("./data/static_data/portals");
				List<PortalTemplate> portalTemplates = new ArrayList<PortalTemplate>();
				try
				{
					JAXBContext jc = JAXBContext.newInstance(StaticData.class);
					Unmarshaller un = jc.createUnmarshaller();
					un.setSchema(getSchema("./data/static_data/static_data.xsd"));
					
					for (File file : listFiles(portalDir, true))
					{
						PortalData data = (PortalData)un.unmarshal(file);
						if (data != null && data.getPortals() != null)
							portalTemplates.addAll(data.getPortals());
					}
					
				}
				catch (Exception e)
				{
					PacketSendUtility.sendMessage(admin, "Portals reload failed! Keeping last version ...");
					log.error(e);
					return;
				}
				if(portalTemplates.size() > 0)
				{
					DataManager.PORTAL_DATA.setPortals(portalTemplates);
					PacketSendUtility.sendMessage(admin, "Portals reloaded successfuly!");
				}
				else
					PacketSendUtility.sendMessage(admin, "Portals reload failed! Keeping last version ...");
				break;
			case QUEST:
				File questXml = new File("./data/static_data/quest_data/quest_data.xml");
				File questDir = new File("./data/static_data/quest_script_data");
				
				QuestsData newQuestData;
				ArrayList<QuestScriptsData> newScriptData = new ArrayList<QuestScriptsData>();
				
				try
				{
					JAXBContext jc = JAXBContext.newInstance(StaticData.class);
					Unmarshaller un = jc.createUnmarshaller();
					un.setSchema(getSchema("./data/static_data/static_data.xsd"));
					newQuestData = (QuestsData) un.unmarshal(questXml);
					for (File file : listFiles(questDir, true))
					{
						QuestScriptsData data = ((QuestScriptsData)un.unmarshal(file));
						if (data != null)
							if (data.getData() != null)
								newScriptData.add(data);
					}
				}
				catch (Exception e)
				{
					PacketSendUtility.sendMessage(admin, "Quests reload failed! Keeping last version ...");
					log.error(e);
					return;
				}
				
				if(newQuestData != null)
				{
					try
					{
						DataManager.QUEST_DATA.setQuestsData(newQuestData.getQuestsData());
						QuestScriptsData questScriptsData = DataManager.QUEST_SCRIPTS_DATA;
						questScriptsData.getData().clear();
						for(QuestScriptsData qsd : newScriptData)
						{
							questScriptsData.getData().addAll(qsd.getData());
						}
						QuestEngine.getInstance().load(true);
						PacketSendUtility.sendMessage(admin, "Quests reloaded successfuly!");
					}
					catch(GameServerError e)
					{
						PacketSendUtility.sendMessage(admin, "Quests reload failed! Keeping last version ...");
						log.error(e);
						return;
					}
				}
				break;
			case SKILL:
				File skillDir = new File("./data/static_data/skills");
				List<SkillTemplate> skillTemplates = new ArrayList<SkillTemplate>();
				try
				{
					JAXBContext jc = JAXBContext.newInstance(StaticData.class);
					Unmarshaller un = jc.createUnmarshaller();
					un.setSchema(getSchema("./data/static_data/static_data.xsd"));
					
					for (File file : listFiles(skillDir, true))
					{
						SkillData data = (SkillData)un.unmarshal(file);
						if (data != null)
							skillTemplates.addAll(data.getSkillTemplates());
					}
				}
				catch (Exception e)
				{
					PacketSendUtility.sendMessage(admin, "Skills reload failed! Keeping last version ...");
					log.error(e);
					return;
				}

				if(skillTemplates.size() > 0)
				{
					DataManager.SKILL_DATA.setSkillTemplates(skillTemplates);
					PacketSendUtility.sendMessage(admin, "Skills reloaded successfuly!");
				}
				else
					PacketSendUtility.sendMessage(admin, "Skills reload failed! Keeping last version ...");
				break;
			case SPAWN:
				File spawnDir = new File("./data/static_data/spawns");
				List<SpawnGroup> spawnTemplates = new ArrayList<SpawnGroup>();
				try
				{
					JAXBContext jc = JAXBContext.newInstance(StaticData.class);
					Unmarshaller un = jc.createUnmarshaller();
					un.setSchema(getSchema("./data/static_data/static_data.xsd"));
					
					for (File file : listFiles(spawnDir, true))
					{
						SpawnsData data = (SpawnsData)un.unmarshal(file);
						if (data != null && data.getSpawnGroups() != null)
							spawnTemplates.addAll(data.getSpawnGroups());
					}
				}
				catch (Exception e)
				{
					PacketSendUtility.sendMessage(admin, "Spawns reload failed! Keeping last version ...");
					log.error(e);
					return;
				}
				
				if(spawnTemplates.size() > 0)
				{
					DataManager.SPAWNS_DATA.setSpawns(spawnTemplates);
					PacketSendUtility.sendMessage(admin, "Spawns reloaded successfuly!");
				}
				else
					PacketSendUtility.sendMessage(admin, "Spawns reload failed with empty object! Keeping last version ...");
				break;
			default:
				sendSyntax(admin);
				return;
		}
	}

	private Schema getSchema(String xml_schema)
	{
		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try
		{
			schema = sf.newSchema(new File(xml_schema));
		}
		catch (SAXException saxe)
		{
			throw new Error("Error while getting schema", saxe);
		}

		return schema;
	}

	private Collection<File> listFiles(File root, boolean recursive)
	{
		IOFileFilter dirFilter = recursive ? makeSVNAware(HiddenFileFilter.VISIBLE) : null;

		return FileUtils.listFiles(root, and(and(notFileFilter(prefixFileFilter("new")),
			suffixFileFilter(".xml")), HiddenFileFilter.VISIBLE), dirFilter);
	}
}