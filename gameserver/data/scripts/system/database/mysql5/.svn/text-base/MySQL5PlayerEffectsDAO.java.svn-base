/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.dao.PlayerEffectsDAO;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.skill.model.Effect;


/**
 * @author ATracer
 *
 */
public class MySQL5PlayerEffectsDAO extends PlayerEffectsDAO
{
	public static final String INSERT_QUERY = "INSERT INTO `player_effects` (`player_id`, `skill_id`, `delay_id`, `skill_lvl`, `current_time`, `reuse_delay`) VALUES (?,?,?,?,?,?)";
	public static final String DELETE_QUERY = "DELETE FROM `player_effects` WHERE `player_id`=?";
	public static final String SELECT_QUERY = "SELECT `skill_id`, `delay_id`, `skill_lvl`, `current_time`, `reuse_delay` FROM `player_effects` WHERE `player_id`=?";

	private static final Logger log = Logger.getLogger(MySQL5PlayerEffectsDAO.class);
	
	@Override
	public void loadPlayerEffects(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			int timeLaunched = 0;
			int skillLaunched = 0;
			int skillLvlLaunched = 0;
			while(rset.next())
			{
				int skillId = rset.getInt("skill_id");
				int delayId = rset.getInt("delay_id");
				int skillLvl = rset.getInt("skill_lvl");
				int elapsedTime = rset.getInt("current_time");//time till end
				long reuseDelay = rset.getLong("reuse_delay");
				
				if(reuseDelay > System.currentTimeMillis())
					player.setSkillCoolDown(delayId, reuseDelay);
				if(elapsedTime > 0)
				{
					if (CustomConfig.ABYSS_XFORM_DURATION_AFTER_LOGOUT)
					{
						//custom for abyss xforms, duration is counting even after log out
						if (skillId >= 11885 && skillId <= 11894)
						{
							elapsedTime = (int)(reuseDelay - System.currentTimeMillis() - 110*60*1000);
							if (elapsedTime < 0 || elapsedTime > 10*60*1000)
								continue;
							else
								timeLaunched = elapsedTime;
						}
						else if(skillId >= 11907 && skillId <= 11916 )
						{
							skillLaunched = skillId;
							skillLvlLaunched = skillLvl;
							continue;
						}
					}
					
					player.getEffectController().addSavedEffect(skillId, skillLvl, elapsedTime);
				}
				
			}
		
			if (CustomConfig.ABYSS_XFORM_DURATION_AFTER_LOGOUT)
			{
				//add launched skill for abyss xform
				if (timeLaunched > 0 && skillLaunched != 0)
				{
					player.getEffectController().addSavedEffect(skillLaunched, skillLvlLaunched, timeLaunched);
				}
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		player.getEffectController().broadCastEffects();
	}

	@Override
	public void storePlayerEffects(final Player player)
	{
		deletePlayerEffects(player);
		Iterator<Effect> iterator = player.getEffectController().iterator();		
		
		while(iterator.hasNext())
		{
			final Effect effect = iterator.next();
			final int elapsedTime = effect.getElapsedTime();
			
			if(elapsedTime < 60000)
				continue;
			
			Connection con = null;
			try
			{
				con = DatabaseFactory.getConnection();
				PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
				stmt.setInt(1, player.getObjectId());
				stmt.setInt(2, effect.getSkillId());
				stmt.setInt(3, effect.getSkillTemplate().getDelayId());
				stmt.setInt(4, effect.getSkillLevel());
				stmt.setInt(5, elapsedTime);//time till end
				
				long reuseTime = player.getSkillCoolDown(effect.getSkillTemplate().getDelayId());
				player.removeSkillCoolDown(effect.getSkillTemplate().getDelayId());
				
				stmt.setLong(6, reuseTime);
				stmt.execute();
			}
			catch(Exception e)
			{
				log.error(e);
			}
			finally
			{
				DatabaseFactory.close(con);
			}
		}
		
		final Map<Integer, Long> cooldowns = player.getSkillCoolDowns();
		if(cooldowns != null)
		{
			for(Map.Entry<Integer, Long> entry : cooldowns.entrySet())
			{
				final int delayId = entry.getKey();
				final long reuseTime = entry.getValue();
				if(reuseTime - System.currentTimeMillis() < 60000)
					continue;
				
				Connection con = null;
				try
				{
					con = DatabaseFactory.getConnection();
					PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
					stmt.setInt(1, player.getObjectId());
					stmt.setInt(2, 0);
					stmt.setInt(3, delayId);
					stmt.setInt(4, 0);
					stmt.setInt(5, 0);											
					stmt.setLong(6, reuseTime);
					stmt.execute();
				}
				catch(Exception e)
				{
					log.error(e);
				}
				finally
				{
					DatabaseFactory.close(con);
				}
			}
		}
	}
	
	private void deletePlayerEffects(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, player.getObjectId());
			stmt.execute();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean supports(String arg0, int arg1, int arg2)
	{
		return MySQL5DAOUtils.supports(arg0, arg1, arg2);
	}
}
