package org.openaion.commons.versionning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;

/**
 * @author lord_rex
 * 
 */
public class Version
{
	private static final Logger	log	= Logger.getLogger(Version.class);
	private String				revision;
	private String				date;

	public Version()
	{
	}

	public Version(Class<?> c)
	{
		loadInformation(c);
	}

	public void loadInformation(Class<?> c)
	{
		File jarName = null;
		try
		{
			jarName = Locator.getClassSource(c);
			JarFile jarFile = new JarFile(jarName);

			Attributes attrs = jarFile.getManifest().getMainAttributes();

			setRevision(attrs);

			setDate(attrs);

		}
		catch(IOException e)
		{
			log.error("Unable to get Soft information\nFile name '"
				+ (jarName == null ? "null" : jarName.getAbsolutePath()) + "' isn't a valid jar", e);
		}

	}

	public void transferInfo(String jarName, String type, File fileToWrite)
	{
		try
		{
			if(!fileToWrite.exists())
			{
				log.error("Unable to Find File :" + fileToWrite.getName() + " Please Update your " + type);
				return;
			}
			// Open the JAR file
			JarFile jarFile = new JarFile("./" + jarName);
			// Get the manifest
			Manifest manifest = jarFile.getManifest();
			// Write the manifest to a file
			OutputStream fos = new FileOutputStream(fileToWrite);
			manifest.write(fos);
			fos.close();
		}
		catch(IOException e)
		{
			log.error("Error, " + e);
		}
	}

	public void setRevision(Attributes attrs)
	{
		String revision = attrs.getValue("Revision");

		if(revision != null)
			this.revision = revision;
		else
			this.revision = "Unknown Revision Number.";
	}

	public String getRevision()
	{
		return revision;
	}

	public void setDate(Attributes attrs)
	{
		String date = attrs.getValue("Date");

		if(date != null)
			this.date = date;
		else
			this.date = "Unknown Date Time.";
	}

	public String getDate()
	{
		return date;
	}
}
