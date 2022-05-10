package org.openaion.gameserver.model.gameobjects;

import org.openaion.gameserver.ai.npcai.TotemAi;
import org.openaion.gameserver.controllers.NpcController;
import org.openaion.gameserver.controllers.NpcWithCreatorController;
import org.openaion.gameserver.model.templates.VisibleObjectTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author kecimis
 *
 */
public class Totem extends NpcWithCreator
{
	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public Totem(int objId, NpcController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
	}
	
	@Override
	public NpcWithCreatorController getController()
	{
		return (NpcWithCreatorController) super.getController();
	}
	public Totem getOwner()
	{
		return (Totem)this;
	}
	@Override
	public void initializeAi()
	{
		this.ai = new TotemAi();
		ai.setOwner(this);
	}
	
	/**
	 * @return NpcObjectType.TOTEM
	 */
	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.TOTEM;
	}
}