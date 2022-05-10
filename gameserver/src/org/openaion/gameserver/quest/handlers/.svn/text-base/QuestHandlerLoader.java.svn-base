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
package org.openaion.gameserver.quest.handlers;

import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;
import org.openaion.commons.scripting.classlistener.ClassListener;
import org.openaion.commons.scripting.classlistener.DefaultClassListener;
import org.openaion.commons.utils.ClassUtils;
import org.openaion.gameserver.quest.QuestEngine;


/**
 * @author MrPoke
 *
 */
public class QuestHandlerLoader extends DefaultClassListener implements ClassListener
{
	private static final Logger logger = Logger.getLogger(QuestHandlerLoader.class);


	public QuestHandlerLoader()
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postLoad(Class<?>[] classes)
	{
		for (Class<?> c : classes)
		{
			if (logger.isDebugEnabled())
				logger.debug("Load class " + c.getName());

			if (!isValidClass(c))
				continue;

			if (ClassUtils.isSubclass(c, QuestHandler.class))
			{
				try
				{
					Class<? extends QuestHandler> tmp = (Class<? extends QuestHandler>)c;
					if (tmp != null)
					{
						QuestHandler tmpo = tmp.newInstance();
						QuestEngine.getInstance().TEMP_HANDLERS.put(tmpo.getQuestId(), tmpo);
					}
				}
				catch(InstantiationException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch(IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// call onClassLoad()
		super.postLoad(classes);

	}

	@Override
	public void preUnload(Class<?>[] classes)
	{
		if (logger.isDebugEnabled())
			for (Class<?> c : classes)// debug messages
				logger.debug("Unload class " + c.getName());

		// call onClassUnload()
		super.preUnload(classes);

		QuestEngine.getInstance().clear();
	}

	public boolean isValidClass(Class<?> clazz)
	{
		final int modifiers = clazz.getModifiers();

		if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers))
			return false;

		if (!Modifier.isPublic(modifiers))
			return false;

		return true;
	}
}
