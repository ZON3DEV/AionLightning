package org.openaion.gameserver.skill.model;

/**
 * @author kecimis
 */
public class DashParam
{
	private float x;
    private float y;
    private float z;
    private int heading;
    private DashType dashType = DashType.NONE;
    
    public DashParam(DashType dashType, float x, float y, float z, int heading)
    {
    	this.dashType = dashType;
    	this.x = x;
    	this.y = y;
    	this.z = z;
    	this.heading = heading;
    }
    
    public int getType()
    {
    	return dashType.getTypeId();
    }
    
    public float getX()
    {
    	return this.x;
    }
    public float getY()
    {
    	return this.y;
    }
    public float getZ()
    {
    	return this.z;
    }
    public int getHeading()
    {
    	return this.heading;
    }
    
	public enum DashType
	{
		/**
		 * Dash Type
		 * 
		 * 1 : teleport to back (1463) 2 : dash (816) 4 : assault (803)
		 */
		NONE(0),
		RANDOMMOVELOC(1),
		DASH(2),
		MOVEBEHIND(4);

		private int	id;

		private DashType(int id)
		{
			this.id = id;
		}

		public int getTypeId()
		{
			return id;
		}

		public static DashType getDashTypeById(int id)
		{
			for(DashType dashType : values())
			{
				if(dashType.getTypeId() == id)
					return dashType;
			}
			return NONE;
		}

	}
}
