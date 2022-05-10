package org.openaion.commons.scripting.classlistener;

/**
 * This interface implements listener that is called post class load/before class unload.<br>
 * Default implementation is: {@link DefaultClassListener}
 * 
 * @author SoulKeeper
 */
public interface ClassListener
{
	/**
	 * This method is invoked after classes were loaded. As areguments are passes all loaded classes
	 * 
	 * @param classes
	 */
	public void postLoad(Class<?>[] classes);

	/**
	 * This method is invoked before class unloading. As argument are passes all loaded classes
	 * 
	 * @param classes
	 */
	public void preUnload(Class<?>[] classes);
}
