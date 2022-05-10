package org.openaion.loginserver.controller;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openaion.loginserver.model.FloodIP;



/**
 * Class that controlls all flood
 * 
 * @author Divinity
 */
public class FloodController
{
	/**
	 * Logger for this class.
	 */
	@SuppressWarnings("unused")
	private static final Logger		log	= Logger.getLogger(FloodController.class);

	private static ArrayList<FloodIP> IPs = new ArrayList<FloodIP>();
	
	public static void addIP(String IP)
	{
		IPs.add(new FloodIP(IP));
	}
	
	public static boolean checkFlood(String IP)
	{
		boolean found	= false;
		int 	i		= 0;
		
		while (!found && i<IPs.size())
		{
			if (IPs.get(i).getIP().equals(IP))
				found = true;
			else
				i++;
		}
		
		return IPs.get(i).checkFlood();
	}
	
	public static boolean exist(String IP)
	{
		for (FloodIP fIP : IPs)
			if (fIP.getIP().equals(IP))
				return true;
				
		return false;
	}
	
	public static int getIP(String ip)
	{
		boolean found	= false;
		int 	i		= 0;
		
		while (!found && i<IPs.size())
		{
			if (IPs.get(i).getIP().equals(ip))
				found = true;
			else
				i++;
		}
		
		return i;
	}
	
	public static void addConnection(String IP)
	{
		IPs.get(getIP(IP)).addConnection();
	}
	
	public static int getConnection(String IP)
	{
		return IPs.get(getIP(IP)).size();
	}
}
