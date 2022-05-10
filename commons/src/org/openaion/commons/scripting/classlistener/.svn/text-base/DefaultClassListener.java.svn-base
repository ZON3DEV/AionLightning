package org.openaion.commons.scripting.classlistener;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;
import org.openaion.commons.scripting.metadata.OnClassLoad;
import org.openaion.commons.scripting.metadata.OnClassUnload;


/**
 * @author SoulKeeper
 */
public class DefaultClassListener implements ClassListener
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(DefaultClassListener.class);

	@Override
	public void postLoad(Class<?>[] classes)
	{
		for(Class<?> c : classes)
		{
			doMethodInvoke(c.getDeclaredMethods(), OnClassLoad.class);
		}
	}

	@Override
	public void preUnload(Class<?>[] classes)
	{
		for(Class<?> c : classes)
		{
			doMethodInvoke(c.getDeclaredMethods(), OnClassUnload.class);
		}
	}

	/**
	 * Actually invokes method where given annotation class is present. Only static methods can be invoked
	 * 
	 * @param methods
	 *            Methods to scan for annotations
	 * @param annotationClass
	 *            class of annotation to search for
	 */
	protected final void doMethodInvoke(Method[] methods, Class<? extends Annotation> annotationClass)
	{
		for(Method m : methods)
		{
			if(!Modifier.isStatic(m.getModifiers()))
				continue;

			boolean accessible = m.isAccessible();
			m.setAccessible(true);

			if(m.getAnnotation(annotationClass) != null)
			{
				try
				{
					m.invoke(null);
				}
				catch(IllegalAccessException e)
				{
					log.error("Can't access method " + m.getName() + " of class " + m.getDeclaringClass().getName(), e);
				}
				catch(InvocationTargetException e)
				{
					log.error("Can't invoke method " + m.getName() + " of class " + m.getDeclaringClass().getName(), e);
				}
			}

			m.setAccessible(accessible);
		}
	}
}
