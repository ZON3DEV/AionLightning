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

package com.aionemu.gameserver.model.instance.instancereward;

import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;

/**
 * @author Everlight, reworked Aioncool
 */
public class GenericInstanceReward extends InstanceReward<InstancePlayerReward> {

	private int points;
	private int npcKills;
	private int rank = 7;
	private int basicAP;
	private int itemId1;
	private int itemId2;
	private int itemId3;
	private int itemId4;
	private int itemId5;
	private int itemId6;
	private int countItem1;
	private int countItem2;
	private int countItem3;
	private int countItem4;
	private int countItem5;
	private int countItem6;
	private boolean isRewarded = false;

	public GenericInstanceReward(Integer mapId, int instanceId) {
		super(mapId, instanceId);
	}

	public void addPoints(int points) {
		this.points += points;
	}

	public int getPoints() {
		return points;
	}

	public void addNpcKill() {
		npcKills++;
	}

	public int getNpcKills() {
		return npcKills;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getRank() {
		return rank;
	}

	@Override
	public boolean isRewarded() {
		return isRewarded;
	}

	public void setRewarded() {
		isRewarded = true;
	}

	public int getBasicAP() {
		return basicAP;
	}

	public void setBasicAP(int ap) {
		this.basicAP = ap;
	}

	public int getRewardId1() {
		return itemId1;
	}

	public int getRewardId2() {
		return itemId2;
	}

	public int getRewardId3() {
		return itemId3;
	}

	public int getRewardId4() {
		return itemId4;
	}

	public int getRewardId5() {
		return itemId5;
	}

	public int getRewardId6() {
		return itemId6;
	}

	public void setRewardId1(int itemId) {
		this.itemId1 = itemId;
	}

	public void setRewardId2(int itemId) {
		this.itemId2 = itemId;
	}

	public void setRewardId3(int itemId) {
		this.itemId3 = itemId;
	}

	public void setRewardId4(int itemId) {
		this.itemId4 = itemId;
	}

	public void setRewardId5(int itemId) {
		this.itemId5 = itemId;
	}

	public void setRewardId6(int itemId) {
		this.itemId6 = itemId;
	}

	public int getCountReward1() {
		return countItem1;
	}

	public int getCountReward2() {
		return countItem2;
	}

	public int getCountReward3() {
		return countItem3;
	}

	public int getCountReward4() {
		return countItem4;
	}

	public int getCountReward5() {
		return countItem5;
	}

	public int getCountReward6() {
		return countItem6;
	}

	public void setCountReward1(int count) {
		this.countItem1 = count;
	}

	public void setCountReward2(int count) {
		this.countItem2 = count;
	}

	public void setCountReward3(int count) {
		this.countItem3 = count;
	}

	public void setCountReward4(int count) {
		this.countItem4 = count;
	}

	public void setCountReward5(int count) {
		this.countItem5 = count;
	}

	public void setCountReward6(int count) {
		this.countItem6 = count;
	}
}
