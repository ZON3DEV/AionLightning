/*
 * This file is part of aion-unique <aion-unique.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.PlayerAppearanceDAO;
import org.openaion.gameserver.model.gameobjects.player.PlayerAppearance;


/**
 * Class that is responsible for loading/storing {@link org.openaion.gameserver.model.gameobjects.player.PlayerAppearance} in mysql5
 * 
 * @author SoulKeeper, AEJTester, srx47
 */
public class MySQL5PlayerAppearanceDAO extends PlayerAppearanceDAO
{
	private static final Logger log = Logger.getLogger(PlayerAppearanceDAO.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerAppearance load(final int playerId)
	{
		Connection con = null;
		final PlayerAppearance pa = new PlayerAppearance();
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM player_appearance WHERE player_id = ?");
			statement.setInt(1, playerId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
			{
				pa.setVoice(resultSet.getInt("voice"));
				pa.setSkinRGB(resultSet.getInt("skin_rgb"));
				pa.setHairRGB(resultSet.getInt("hair_rgb"));
				pa.setEyeRGB(resultSet.getInt("eye_rgb"));
				pa.setLipRGB(resultSet.getInt("lip_rgb"));
				pa.setFace(resultSet.getInt("face"));
				pa.setHair(resultSet.getInt("hair"));
				pa.setDecoration(resultSet.getInt("decoration"));
				pa.setTattoo(resultSet.getInt("tattoo"));
				pa.setFaceContour(resultSet.getInt("face_contour"));
				pa.setExpression(resultSet.getInt("expression"));
				pa.setJawLine(resultSet.getInt("jaw_line"));
				pa.setForehead(resultSet.getInt("forehead"));
				pa.setEyeHeight(resultSet.getInt("eye_height"));
				pa.setEyeSpace(resultSet.getInt("eye_space"));
				pa.setEyeWidth(resultSet.getInt("eye_width"));
				pa.setEyeSize(resultSet.getInt("eye_size"));
				pa.setEyeShape(resultSet.getInt("eye_shape"));
				pa.setEyeAngle(resultSet.getInt("eye_angle"));
				pa.setBrowHeight(resultSet.getInt("brow_height"));
				pa.setBrowAngle(resultSet.getInt("brow_angle"));
				pa.setBrowShape(resultSet.getInt("brow_shape"));
				pa.setNose(resultSet.getInt("nose"));
				pa.setNoseBridge(resultSet.getInt("nose_bridge"));
				pa.setNoseWidth(resultSet.getInt("nose_width"));
				pa.setNoseTip(resultSet.getInt("nose_tip"));
				pa.setCheeks(resultSet.getInt("cheeks"));
				pa.setLipHeight(resultSet.getInt("lip_height"));
				pa.setMouthSize(resultSet.getInt("mouth_size"));
				pa.setLipSize(resultSet.getInt("lip_size"));
				pa.setSmile(resultSet.getInt("smile"));
				pa.setLipShape(resultSet.getInt("lip_shape"));
				pa.setChinHeight(resultSet.getInt("chin_height"));
				pa.setCheekBones(resultSet.getInt("cheek_bones"));
				pa.setEarShape(resultSet.getInt("ear_shape"));
				pa.setHeadSize(resultSet.getInt("head_size"));
				pa.setNeck(resultSet.getInt("neck"));
				pa.setNeckLength(resultSet.getInt("neck_length"));
				pa.setShoulderSize(resultSet.getInt("shoulder_size"));
				pa.setTorso(resultSet.getInt("torso"));
				pa.setChest(resultSet.getInt("chest"));
				pa.setWaist(resultSet.getInt("waist"));
				pa.setHips(resultSet.getInt("hips"));
				pa.setArmThickness(resultSet.getInt("arm_thickness"));
				pa.setHandSize(resultSet.getInt("hand_size"));
				pa.setLegThickness(resultSet.getInt("leg_thickness"));
				pa.setFootSize(resultSet.getInt("foot_size"));
				pa.setFacialRatio(resultSet.getInt("facial_ratio"));
				pa.setArmLength(resultSet.getInt("arm_length"));
				pa.setLegLength(resultSet.getInt("leg_length"));
				pa.setShoulders(resultSet.getInt("shoulders"));
				pa.setFaceShape(resultSet.getInt("face_shape"));
				pa.setHeight(resultSet.getFloat("height"));
			}
			resultSet.close();
			statement.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not restore PlayerAppearance data for player " + playerId + " from DB: "+e.getMessage(), e);
			return null;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return pa;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean store(final int id, final PlayerAppearance pa)
	{
		Connection con = null;
		boolean success = false;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("REPLACE INTO player_appearance ("
				+ "player_id, voice, skin_rgb, hair_rgb, lip_rgb, eye_rgb, face, hair, decoration, tattoo, face_contour, expression,"
				+ "jaw_line, forehead, eye_height, eye_space, eye_width, eye_size, eye_shape, eye_angle,"
				+ "brow_height, brow_angle, brow_shape, nose, nose_bridge, nose_width, nose_tip, "
				+ "cheeks, lip_height, mouth_size, lip_size, smile, lip_shape, chin_height, cheek_bones, ear_shape,"
				+ "head_size, neck, neck_length, shoulder_size , torso, chest, waist, hips, arm_thickness, hand_size,"
				+ "leg_thickness, foot_size, facial_ratio, arm_length, leg_length, shoulders, face_shape, height)" + " VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" + ")");
			ps.setInt(1, id);
			ps.setInt(2, pa.getVoice());
			ps.setInt(3, pa.getSkinRGB());
			ps.setInt(4, pa.getHairRGB());
			ps.setInt(5, pa.getLipRGB());
			ps.setInt(6, pa.getEyeRGB());
			ps.setInt(7, pa.getFace());
			ps.setInt(8, pa.getHair());
			ps.setInt(9, pa.getDecoration());
			ps.setInt(10, pa.getTattoo());
			ps.setInt(11, pa.getFaceContour());
			ps.setInt(12, pa.getExpression());
			ps.setInt(13, pa.getJawLine());
			ps.setInt(14, pa.getForehead());
			ps.setInt(15, pa.getEyeHeight());
			ps.setInt(16, pa.getEyeSpace());
			ps.setInt(17, pa.getEyeWidth());
			ps.setInt(18, pa.getEyeSize());
			ps.setInt(19, pa.getEyeShape());
			ps.setInt(20, pa.getEyeAngle());
			ps.setInt(21, pa.getBrowHeight());
			ps.setInt(22, pa.getBrowAngle());
			ps.setInt(23, pa.getBrowShape());
			ps.setInt(24, pa.getNose());
			ps.setInt(25, pa.getNoseBridge());
			ps.setInt(26, pa.getNoseWidth());
			ps.setInt(27, pa.getNoseTip());
			ps.setInt(28, pa.getCheeks());
			ps.setInt(29, pa.getLipHeight());
			ps.setInt(30, pa.getMouthSize());
			ps.setInt(31, pa.getLipSize());
			ps.setInt(32, pa.getSmile());
			ps.setInt(33, pa.getLipShape());
			ps.setInt(34, pa.getChinHeight());
			ps.setInt(35, pa.getCheekBones());
			ps.setInt(36, pa.getEarShape());
			ps.setInt(37, pa.getHeadSize());
			ps.setInt(38, pa.getNeck());
			ps.setInt(39, pa.getNeckLength());
			ps.setInt(40, pa.getShoulderSize());
			ps.setInt(41, pa.getTorso());
			ps.setInt(42, pa.getChest());
			ps.setInt(43, pa.getWaist());
			ps.setInt(44, pa.getHips());
			ps.setInt(45, pa.getArmThickness());
			ps.setInt(46, pa.getHandSize());
			ps.setInt(47, pa.getLegThickness());
			ps.setInt(48, pa.getFootSize());
			ps.setInt(49, pa.getFacialRatio());
			ps.setInt(50, pa.getArmLength());
			ps.setInt(51, pa.getLegLength());
			ps.setInt(52, pa.getShoulders());
			ps.setInt(53, pa.getFaceShape());
			ps.setFloat(54, pa.getHeight());
			ps.execute();
			success = true;
		}
		catch(Exception e)
		{
			log.error(e);
			success = false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
