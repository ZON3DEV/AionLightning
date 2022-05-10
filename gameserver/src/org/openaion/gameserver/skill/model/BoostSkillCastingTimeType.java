package org.openaion.gameserver.skill.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author kecimis
 *
 */

@XmlType(name = "BoostSkillCastingTimeType")
@XmlEnum
public enum BoostSkillCastingTimeType
{
	SUMMONTRAP,
	SUMMON,
	SUMMONHOMING,
	HEAL,
	ATTACK,
	NONE
}