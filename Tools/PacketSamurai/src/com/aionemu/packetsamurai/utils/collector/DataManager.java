/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */


package com.aionemu.packetsamurai.utils.collector;

import com.aionemu.packetsamurai.utils.collector.data.houses.CollectedHouseDataLoader;
import com.aionemu.packetsamurai.utils.collector.data.npcData.CollectedNpcDataLoader;
import com.aionemu.packetsamurai.utils.collector.data.npcMoves.NpcMoveDataSaver;
import com.aionemu.packetsamurai.utils.collector.data.npcskills.NpcSkillsTool;
import com.aionemu.packetsamurai.utils.collector.data.towns.TownSpawnsTool;

public class DataManager {

	public static void load() {
		NpcSkillsTool.load();
		CollectedNpcDataLoader.load();
		CollectedHouseDataLoader.load();
		TownSpawnsTool.load();
	}

	public static void save() {
		NpcSkillsTool.save();
		CollectedNpcDataLoader.save();
		NpcMoveDataSaver.save();
		CollectedHouseDataLoader.save();
		TownSpawnsTool.save();
	}

}
