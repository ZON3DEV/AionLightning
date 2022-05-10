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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.PlayerPetsDAO;
import org.openaion.gameserver.model.gameobjects.player.PetFeedState;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.ToyPet;


/**
 * @author Sylar
 */
public class MySQL5PlayerPetsDAO extends PlayerPetsDAO
{
	private static final Logger					log					= Logger.getLogger(MySQL5PlayerDAO.class);

	@Override
	public void insertPlayerPet(Player player, int petId, int decoration, String name)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO player_pets(player_id, pet_id, decoration, name) VALUES(?, ?, ?, ?)");
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, petId);
			stmt.setInt(3, decoration);
			stmt.setString(4, name);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error inserting new pet #" + petId + "[" + name + "]", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public void removePlayerPet(Player player, int petId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM player_pets WHERE player_id = ? AND pet_id = ?");
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, petId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error removing pet #" + petId, e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public List<ToyPet> getPlayerPets(int playerId)
	{
		List<ToyPet> pets = new ArrayList<ToyPet>();
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM player_pets WHERE player_id = ?");
			stmt.setInt(1, playerId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				ToyPet pet = new ToyPet();
				pet.setName(rs.getString("name"));
				pet.setDecoration(rs.getInt("decoration"));
				pet.setPetId(rs.getInt("pet_id"));
				Timestamp ts = null;
				try
				{
					ts = rs.getTimestamp("birthday");
				}
				catch (Exception e) {}
				if (ts == null)
					ts = new Timestamp(System.currentTimeMillis());
				pet.setBirthDay(ts);
				pet.setFeedCount(rs.getInt("feed_count"));
				pet.setLoveCount(rs.getInt("love_count"));
				pet.setExp(rs.getInt("exp"));
				pet.setFeedState(PetFeedState.valueOf(rs.getString("feed_state")));
				pet.setCdStarted(rs.getLong("cd_started"));
				pets.add(pet);
			}
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error getting pets for " + playerId, e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return pets;
	}
	
	@Override
	public ToyPet getPlayerPet(int playerId, int petId)
	{
		ToyPet pet;
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM player_pets WHERE player_id = ? AND pet_id = ?");
			stmt.setInt(1, playerId);
			stmt.setInt(2, petId);
			ResultSet rs = stmt.executeQuery();
			if(rs.first())
			{
				pet = new ToyPet();
				pet.setName(rs.getString("name"));
				pet.setDecoration(rs.getInt("decoration"));
				pet.setPetId(rs.getInt("pet_id"));
				pet.setBirthDay(rs.getTimestamp("birthday"));
				pet.setFeedCount(rs.getInt("feed_count"));
				pet.setLoveCount(rs.getInt("love_count"));
				pet.setExp(rs.getInt("exp"));
				pet.setFeedState(PetFeedState.valueOf(rs.getString("feed_state")));
				pet.setCdStarted(rs.getLong("cd_started"));
			}
			else
			{
				pet = null;
			}
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error getting pets for " + playerId, e);
			pet = null;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return pet;
	}
	
	@Override
	public void renamePlayerPet(Player player, int petId, String petName)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE player_pets SET name = ? WHERE player_id = ? AND pet_id = ?");
			stmt.setString(1, petName);
			stmt.setInt(2, player.getObjectId());
			stmt.setInt(3, petId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error updating new pet name #" + petId + "[" + petName + "]", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}

	@Override
	public boolean savePet(Player player, ToyPet pet)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE player_pets SET feed_count = ?, love_count = ?, exp = ?, feed_state = ?, cd_started = ? WHERE player_id = ? AND pet_id = ?");
			stmt.setInt(1, pet.getFeedCount());
			stmt.setInt(2, pet.getLoveCount());
			stmt.setInt(3, pet.getExp());
			stmt.setString(4, pet.getFeedState().toString());
			stmt.setLong(5, pet.getCdStarted());
			stmt.setInt(6, player.getObjectId());
			stmt.setInt(7, pet.getPetId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error updating pet #" + pet.getPetId(), e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}

}
