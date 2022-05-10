package org.openaion.gameserver.controllers.movement;

/**
 * Contains all possible movement types. Its used by CM_MOVE, SM_MOVE and controller.
 *
 * @author -Nemesiss-
 * 
 */
public enum MovementType
{
	/**
	 * Movement by mouse.
	 */
	MOVEMENT_START_MOUSE(-32),
	/**
	 * Movement by keyboard.
	 */
	MOVEMENT_START_KEYBOARD(-64),
	/**
	 * Validation (movement by mouse).
	 */
	VALIDATE_MOUSE(-96),
	/**
	 * Validation (movement by keyboard).
	 */
	VALIDATE_KEYBOARD(-128),
	/**
	 * Validation (jump).
	 */
	VALIDATE_JUMP(8),
	/**
	 * Validation (jump while moving).
	 */
	VALIDATE_JUMP_WHILE_MOVING(72),
	/**
	 * Floating up while free gliding.
	 */
	MOVEMENT_GLIDE_UP(-124),
	/**
	 * Sinking down while free gliding.
	 */
	MOVEMENT_GLIDE_DOWN(-60),
	/**
	 * Click to move while gliding.
	 */
	// CM_MOVE only
	MOVEMENT_GLIDE_START_MOUSE(-28),
	/**
	 * Glide while moving by mouse.
	 */
	VALIDATE_GLIDE_MOUSE(-92),
	/**
	 * Movement stop.
	 */
	MOVEMENT_STOP(0),
	/**
	 * Npc base movement.
	 */
	// Two times a -28, but this one is for SM_MOVE only
	NPC_MOVEMENT_TYPE_I(-28),
	NPC_MOVEMENT_TYPE_II(-30),
	NPC_MOVEMENT_TYPE_III(-32),
	/**
	 * Predefined Route Npc movement.
	 */
	NPC_WALKROUTE_MOVEMENT_TYPE_I(-22),
	NPC_WALKROUTE_MOVEMENT_TYPE_II(-24),
	
	
	/**
	 * Misc.
	 */
	MOVEMENT_STAYIN_ELEVATOR(24),
	MOVEMENT_JUMPIN_ELEVATOR(-48), //sometimes not jump
	MOVEMENT_VALIDATEIN_ELEVATOR(-112), //unk
	MOVEMENT_MOVIN_ELEVATOR(-16),
	MOVEMENT_ON_ELEVATOR(16),
	MOVEMENT_GO_UPDOWN_ELEVATOR(-80),

	UNKNOWN(1);

	private int	typeId;

	/**
	 * Constructor.
	 * 
	 * @param typeId
	 */
	private MovementType(int typeId)
	{
		this.typeId = typeId;
	}

	/**
	 * Get id of this MovementType
	 * @return id.
	 */
	public int getMovementTypeId()
	{
		return typeId;
	}

	/**
	 * Return MovementType by id.
	 * @param id
	 * @return MovementType
	 */
	public static MovementType getMovementTypeById(int id)
	{
		for(MovementType mt : values())
		{
			if(mt.typeId == id)
				return mt;
		}
		return UNKNOWN;
	}
}