/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.model.siege;

import org.openaion.gameserver.controllers.ArtifactController;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.templates.VisibleObjectTemplate;
import org.openaion.gameserver.model.templates.siege.ArtifactTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;

import javolution.util.FastList;


/**
 * @author Sarynth
 *
 */
public class Artifact extends Npc
{
	private int spawnStaticId; 
	private int artifactId;
	private ArtifactProtector protector;
	
	private FastList<Integer> relatedSpawnedObjectsIds;
	private ArtifactTemplate template;

	public Artifact(int objId, ArtifactController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, int artifactId, int staticId)
	{
		super(objId, controller, spawn, objectTemplate);
		this.artifactId = artifactId;
		this.spawnStaticId = staticId; 
		this.relatedSpawnedObjectsIds = new FastList<Integer>();
	}
	
	public void setProtector(ArtifactProtector protector)
	{
		this.protector = protector;
		protector.setArtifact(this);
	}
	
	public int getLocationId()
	{
		return artifactId;
	}
	
	public int getStaticId()
	{
		return spawnStaticId;
	}
	
	public ArtifactProtector getProtector()
	{
		return protector;
	}
	
	public void registerRelatedSpawn(int objectId)
	{
		if(!relatedSpawnedObjectsIds.contains(objectId))
			relatedSpawnedObjectsIds.add(objectId);
	}
	
	public FastList<Integer> getRelatedSpawnIds()
	{
		return relatedSpawnedObjectsIds;
	}

	/**
	 * @return the template
	 */
	public ArtifactTemplate getTemplate()
	{
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(ArtifactTemplate template)
	{
		this.template = template;
	}
	
	public ArtifactController getController()
	{
		return (ArtifactController)super.getController();
	}
	
}
