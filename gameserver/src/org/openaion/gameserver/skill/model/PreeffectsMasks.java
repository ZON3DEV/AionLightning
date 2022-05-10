package org.openaion.gameserver.skill.model;

import javolution.util.FastList;

/**
 * @author kecimis
 *
 */

public enum PreeffectsMasks
{
	NONE(0),
	e1(1),
	e2(2),
	e1_2(3),
	e3(4),
	e1_2_3(7);
	

	private PreeffectsMasks(int mask)
	{
	}
	
	public static FastList<Integer> getPositions(int mask)
	{
		FastList<Integer> positions = new FastList<Integer>();
		
		switch(mask)
		{
			case 1:
				positions.add(1);
				break;
			case 2:
				positions.add(2);
				break;
			case 3:
				positions.add(1);
				positions.add(2);
				break;
			case 4:
				positions.add(3);
				break;
			case 7:
				positions.add(1);
				positions.add(2);
				positions.add(3);
				break;
			default:
				return null;
		}
		
		return positions;
	}
}
