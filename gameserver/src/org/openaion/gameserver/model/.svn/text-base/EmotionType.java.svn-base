package org.openaion.gameserver.model;

/**
 * @author lyahim edit LokiReborn,Dns
 *
 */
public enum EmotionType
{
	UNK(-1),
	SELECT_TARGET(0),
	JUMP(1),
	SIT(2),
	STAND(3),
	CHAIR_SIT(4),
	CHAIR_UP(5),
	START_FLYTELEPORT(6),
	LAND_FLYTELEPORT(7),
	WINDSTREAM(8),
	WINDSTREAM_END(9),
	WINDSTREAM_BOOST(11),
	FLY(13),
	LAND(14),
	DIE(18),
	RESURRECT(19),
	EMOTE(21),
	END_DUEL(22),
	ATTACKMODE(24),
	NEUTRALMODE(25),
	WALK(26),
	RUN(27),
	SWITCH_DOOR(31),
	START_EMOTE(32),
	OPEN_PRIVATESHOP(33),
	CLOSE_PRIVATESHOP(34),
	START_EMOTE2(35), //why have 2 code? Also used by NPC walk
	POWERSHARD_ON(36),
	POWERSHARD_OFF(37),
	ATTACKMODE2(38), //why have 2 code?
	NEUTRALMODE2(39), //why have 2 code?
	START_LOOT(40),
	END_LOOT(41),
	START_QUESTLOOT(42),
	END_QUESTLOOT(43),
	PET_FEEDING(50),
	PET_FEEDING2(51);
	
	private int id;
	
	private EmotionType(int id)
	{
		this.id = id;
	}
	
	public int getTypeId()
	{
		return id;
	}
	
	
	public static EmotionType getEmotionTypeById(int id)
	{
		for(EmotionType emotionType : values())
		{
			if(emotionType.getTypeId() == id)
				return emotionType;
		}
		return UNK;
	}
	
}
