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

package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.*;
import com.aionemu.gameserver.model.instance.playerreward.*;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import javolution.util.FastList;

import java.util.List;

/**
 * @author Dns, ginho1, nrg, xTz
 * @updated Eloann
 */
@SuppressWarnings("rawtypes")
public class SM_INSTANCE_SCORE extends AionServerPacket {

	private int type;
	private int mapId;
	private int instanceTime;
	private InstanceScoreType instanceScoreType;
	private InstanceReward instanceReward;
	private List<Player> players;
	private Integer object;

	public SM_INSTANCE_SCORE(int type, int instanceTime, InstanceReward instanceReward, Integer object) {
		this(instanceTime, instanceReward, null);
		this.type = type;
		this.object = object;
	}

	public SM_INSTANCE_SCORE(int instanceTime, InstanceReward instanceReward, List<Player> players) {
		this.mapId = instanceReward.getMapId();
		this.instanceTime = instanceTime;
		this.instanceReward = instanceReward;
		this.players = players;
		instanceScoreType = instanceReward.getInstanceScoreType();
	}

	public SM_INSTANCE_SCORE(InstanceReward instanceReward, InstanceScoreType instanceScoreType) {
		this.mapId = instanceReward.getMapId();
		this.instanceReward = instanceReward;
		this.instanceScoreType = instanceScoreType;
	}

	public SM_INSTANCE_SCORE(InstanceReward instanceReward) {
		this.mapId = instanceReward.getMapId();
		this.instanceReward = instanceReward;
		this.instanceScoreType = instanceReward.getInstanceScoreType();
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void writeImpl(AionConnection con) {
		int playerCount = 0;
		Player owner = con.getActivePlayer();
		Integer ownerObject = owner.getObjectId();
		writeD(mapId);
		writeD(instanceTime);
		writeD(instanceScoreType.getId());
		switch (mapId) {
			case 300450000: // Arena Of Harmony
			case 300570000: // Harmony Training Grounds
			case 301100000: // Unity Training Grounds
				HarmonyArenaReward harmonyArena = (HarmonyArenaReward) instanceReward;
				if (object == null) {
					object = ownerObject;
				}
				HarmonyGroupReward harmonyGroupReward = harmonyArena.getHarmonyGroupReward(object);
				writeC(type);
				switch (type) {
					case 2:
						writeD(0);
						writeD(harmonyArena.getRound());
						break;
					case 3:
						writeD(harmonyGroupReward.getOwner() - 1);
						writeS(harmonyGroupReward.getAGPlayer(object).getName(), 52);
						writeD(harmonyGroupReward.getOwner());
						writeD(object);
						break;
					case 4:
						writeD(harmonyArena.getPlayerReward(object).getRemaningTime());
						writeD(0);
						writeD(0);
						writeD(object);
						break;
					case 5:
						writeD(harmonyGroupReward.getBasicAP());
				    	writeD(harmonyGroupReward.getBasicGP());
						writeD(harmonyGroupReward.getScoreAP());
				    	writeD(harmonyGroupReward.getScoreGP());
						writeD(harmonyGroupReward.getRankingAP());
				    	writeD(harmonyGroupReward.getRankingGP());
						writeD(186000137);
						writeD(harmonyGroupReward.getBasicCourage());
						writeD(harmonyGroupReward.getScoreCourage());
						writeD(harmonyGroupReward.getRankingCourage());
						if (harmonyGroupReward.getGloryTicket() != 0) {
							writeD(186000185);
							writeD(harmonyGroupReward.getGloryTicket());
						} else {
							writeD(0);
							writeD(0);
						}
						writeD(0);
						writeD(0);
						writeD(0);
						writeD(0);
						writeD(0);
						writeD(0);
						writeD((int) harmonyGroupReward.getParticipation() * 100);
						writeD(harmonyGroupReward.getPoints());
						break;
					case 6:
						writeD(3);
						writeD(harmonyArena.getCapPoints());
						writeD(3);
						writeD(1);
						writeD(harmonyArena.getBuffId());
						writeD(2);
						writeD(0);
						writeD(harmonyArena.getRound());
						FastList<HarmonyGroupReward> groups = harmonyArena.getHarmonyGroupInside();
						writeC(groups.size());
						for (HarmonyGroupReward group : groups) {
							writeC(harmonyArena.getRank(group.getPoints()));
							writeD(group.getPvPKills());
							writeD(group.getPoints());
							writeD(group.getOwner());
							FastList<Player> members = harmonyArena.getPlayersInside(group);
							writeC(members.size());
							int i = 0;
							for (Player p : members) {
								PvPArenaPlayerReward rewardedPlayer = harmonyArena.getPlayerReward(p.getObjectId());
								writeD(0);
								writeD(rewardedPlayer.getRemaningTime());
								writeD(0);
								writeC(group.getOwner() - 1);
								writeC(i);
								writeH(0);
								writeS(p.getName(), 52);
								writeD(p.getObjectId());
								i++;
							}
						}
						break;
					case 10:
						writeC(harmonyArena.getRank(harmonyGroupReward.getPoints()));
						writeD(harmonyGroupReward.getPvPKills());
						writeD(harmonyGroupReward.getPoints());
						writeD(harmonyGroupReward.getOwner());
						break;
				}
				break;
			case 300110000: // Baranath Dredgion.
			case 300210000: // Chantra Dredgion.
			case 300440000: // Terath Dredgion.
				fillTableWithGroup(Race.ELYOS);
				fillTableWithGroup(Race.ASMODIANS);
				DredgionReward dredgionReward = (DredgionReward) instanceReward;
				int elyosScore = dredgionReward.getPointsByRace(Race.ELYOS).intValue();
				int asmosScore = dredgionReward.getPointsByRace(Race.ASMODIANS).intValue();
				writeD(instanceScoreType.isEndProgress() ? (asmosScore > elyosScore ? 1 : 0) : 255);
				writeD(elyosScore);
				writeD(asmosScore);
				writeH(0);
				for (DredgionReward.DredgionRooms dredgionRoom : dredgionReward.getDredgionRooms()) {
					writeC(dredgionRoom.getState());
				}
				break;
			case 301120000: // Kamar Battlefield
				// TODO
				break;
			case 301210000: // Ophidan Bridge War
				// TODO
				break;
			case 301220000: // The Eternal Bastion War
				// TODO
				break;
			case 300300000: // Empyrean Crucible
			case 300320000: // Empyrean Crucible Challenge
				for (CruciblePlayerReward playerReward : (FastList<CruciblePlayerReward>) instanceReward.getInstanceRewards()) {
					writeD(playerReward.getOwner());
					writeD(playerReward.getPoints());
					writeD(instanceScoreType.isEndProgress() ? 3 : 1);
					writeD(playerReward.getInsignia());
					playerCount++;
				}
				if (playerCount < 6) {
					writeB(new byte[16 * (6 - playerCount)]);
				}
				break;
			case 300040000: // Dark Poeta.
				DarkPoetaReward dpr = (DarkPoetaReward) instanceReward;
				writeD(dpr.getPoints());
				writeD(dpr.getNpcKills());
				writeD(dpr.getGatherCollections());
				writeD(dpr.getRank());
				break;
			case 300480000:
			case 301190000: // legion
				DanuarMysticariumReward hok = (DanuarMysticariumReward) instanceReward;
				writeD(hok.getPoints());
				writeD(hok.getNpcKills());
				writeD(0);
				writeD(hok.getRank());
				writeD(hok.getBasicAP());
				if (hok.getPoints() >= 30481) { // S
					writeD(186000239); // Sillus Crest
					writeD(hok.getSillusCrest());
					writeD(186000242); // Ceramium Medal
					writeD(hok.getCeramiumMedal());
					writeD(188052543); // Favorable Reward Bundle
				} else if (hok.getPoints() >= 22252) { // A
					writeD(186000239);
					writeD(hok.getSillusCrest());
					writeD(152012578); // Ceramium Fragments
					writeD(hok.getCeramiumFragments());
					writeD(188052547); // Valor Reward Bundle
				} else if (hok.getPoints() >= 15394) { // B
					writeD(186000239);
					writeD(hok.getSillusCrest());
					writeD(186000147); // Mithril Medal
					writeD(hok.getMithrilMedal());
					writeD(0);
				} else if (hok.getPoints() >= 9908) { // C
					writeD(186000239);
					writeD(hok.getSillusCrest());
					writeD(0);
					writeD(0);
					writeD(0);
				} else if (hok.getPoints() >= 7165) { // D
					writeD(186000239);
					writeD(hok.getSillusCrest());
					writeD(0);
					writeD(0);
					writeD(0);
				} else { // F
					writeD(0);
					writeD(0);
					writeD(0);
					writeD(0);
					writeD(0);
				}
				if (hok.getPoints() >= 30481) { // S
					writeD(hok.getFavorableBundle());
				} else if (hok.getPoints() >= 22252) { // A
					writeD(hok.getValorBundle());
				} else {
					writeD(0);
				}
				writeD(0);
				writeD(0);
				break;
			case 300530000: // Idgel Research Center
			case 301170000: // Idgel Research Center (Legion)
				IdgelResearchCenterReward ircr = (IdgelResearchCenterReward) instanceReward;
				writeD(ircr.getPoints());
				writeD(ircr.getNpcKills());
				writeD(0);
				writeD(ircr.getRank());
				writeD(ircr.getScoreAP());
				if (ircr.getPoints() >= 2700) {
					writeD(186000239); // Sillus Crest.
					writeD(ircr.getSillus());
					writeD(186000242); // Ceramium Medal.
					writeD(ircr.getCeramium());
					writeD(188052543); // Favorable Reward Bundle.
				} else {
					writeD(0);
					writeD(0);
					writeD(0);
					writeD(0);
					writeD(0);
				}
				if (ircr.getPoints() >= 5000) {
					writeD(ircr.getFavorable());
				} else {
					writeD(0);
				}
				writeD(0);
				writeD(0);
				break;
            case 300540000: //Eternal Bastion
                EternalBastionReward ebr = (EternalBastionReward) instanceReward;
                writeD(ebr.getPoints());
                writeD(ebr.getNpcKills());
                writeD(0);
                writeD(ebr.getRank());
                writeD(ebr.getBasicAP());
                if (ebr.getPoints() >= 92000) { //S
                    writeD(186000242);
                    writeD(ebr.getCeramiumMedal());
                    writeD(188052596);
                    writeD(ebr.getPowerfulBundleWater());
                    writeD(188052594);
                } else if (ebr.getPoints() >= 84000) { //A
                    writeD(186000242);
                    writeD(ebr.getCeramiumMedal());
                    writeD(188052594);
                    writeD(ebr.getPowerfulBundleEssence());
                    writeD(188052597);
                } else if (ebr.getPoints() >= 76000) { //B
                    writeD(186000242);
                    writeD(ebr.getCeramiumMedal());
                    writeD(188052595);
                    writeD(ebr.getLargeBundleEssence());
                    writeD(188052598);
                } else if (ebr.getPoints() >= 50000) { //C
                    writeD(188052598);
                    writeD(ebr.getSmallBundleWater());
                    writeD(0);
                    writeD(0);
                    writeD(0);
                } else if (ebr.getPoints() >= 10000) { //D
                    writeD(0);
                    writeD(0);
                    writeD(0);
                    writeD(0);
                    writeD(0);
                } else { //F
                    writeD(0);
                    writeD(0);
                    writeD(0);
                    writeD(0);
                    writeD(0);
                }
                writeD(0);
                writeD(0);
                break;
            case 300580000: //Cube Void
            case 301180000: //Cube Void (Legion)
                VoidCubeReward vcr = (VoidCubeReward) instanceReward;
                writeD(vcr.getPoints());
                writeD(vcr.getNpcKills());
                writeD(0);
                writeD(vcr.getRank());
                writeD(vcr.getScoreAP());
                if (vcr.getPoints() >= 5700) {
                    writeD(186000239); //Sillus Crest.
                    writeD(vcr.getSillus());
                    writeD(186000242); //Ceramium Medal.
                    writeD(vcr.getCeramium());
                    writeD(188052543); //Favorable Reward Bundle.
                } else {
                    writeD(0);
                    writeD(0);
                    writeD(0);
                    writeD(0);
                    writeD(0);
                }
                if (vcr.getPoints() >= 10000) {
                    writeD(vcr.getFavorable());
                } else {
                    writeD(0);
                }
                writeD(0);
                writeD(0);
                break;
			case 300350000: // Arena Of Chaos.
			case 300360000: // Arena Of Discipline.
			case 300420000: // Chaos Training Grounds.
			case 300430000: // Discipline Training Grounds.
			case 300550000: // Arena Of Glory.
				PvPArenaReward arenaReward = (PvPArenaReward) instanceReward;
				PvPArenaPlayerReward rewardedPlayer = arenaReward.getPlayerReward(ownerObject);
				int rank,
				points;
				boolean isRewarded = arenaReward.isRewarded();
				for (Player player : players) {
					InstancePlayerReward reward = arenaReward.getPlayerReward(player.getObjectId());
					PvPArenaPlayerReward playerReward = (PvPArenaPlayerReward) reward;
					points = playerReward.getPoints();
					rank = arenaReward.getRank(playerReward.getScorePoints());
					writeD(playerReward.getOwner());
					writeD(playerReward.getPvPKills());
					writeD(isRewarded ? points + playerReward.getTimeBonus() : points);
					writeD(0);
					writeC(0);
					writeC(player.getPlayerClass().getClassId());
					writeC(1);
					writeC(rank);
					writeD(playerReward.getRemaningTime());
					writeD(isRewarded ? playerReward.getTimeBonus() : 0);
					writeD(0);
					writeD(0);
					writeH(isRewarded ? (short) (playerReward.getParticipation() * 100) : 0);
					writeS(player.getName(), 54);
					playerCount++;
				}
				if (playerCount < 12) {
					writeB(new byte[92 * (12 - playerCount)]);
				}
				if (isRewarded && arenaReward.canRewarded() && rewardedPlayer != null) {
					writeD(rewardedPlayer.getBasicAP());
					writeD(rewardedPlayer.getRankingAP());
					writeD(rewardedPlayer.getScoreAP());
					if (mapId == 300550000) { // Arena Of Glory.
						writeD(rewardedPlayer.getBasicGP());
						writeD(rewardedPlayer.getRankingGP());
						writeD(rewardedPlayer.getScoreGP());
						writeB(new byte[32]);
						if (rewardedPlayer.getMithrilMedal() != 0) {
							writeD(186000147); // Mithril Medal.
							writeD(rewardedPlayer.getMithrilMedal());
						} else if (rewardedPlayer.getPlatinumMedal() != 0) {
							writeD(186000096); // Platinum Medal.
							writeD(rewardedPlayer.getPlatinumMedal());
						} else if (rewardedPlayer.getLifeSerum() != 0) {
							writeD(162000077); // Fine Life Serum.
							writeD(rewardedPlayer.getLifeSerum());
						} else {
							writeD(0);
							writeD(0);
						}
						if (rewardedPlayer.getGloriousInsignia() != 0) {
							writeD(182213259); // Glorious Insignia.
							writeD(rewardedPlayer.getGloriousInsignia());
						} else {
							writeD(0);
							writeD(0);
						}
					} else {
						writeD(186000130); // Crucible Insignia.
						writeD(rewardedPlayer.getBasicCrucible());
						writeD(rewardedPlayer.getScoreCrucible());
						writeD(rewardedPlayer.getRankingCrucible());
						writeD(186000137); // Courage Insignia.
						writeD(rewardedPlayer.getBasicCourage());
						writeD(rewardedPlayer.getScoreCourage());
						writeD(rewardedPlayer.getRankingCourage());
						if (rewardedPlayer.getOpportunity() != 0) {
							writeD(186000165); // Opportunity Token.
							writeD(rewardedPlayer.getOpportunity());
						} else if (rewardedPlayer.getGloryTicket() != 0) {
							writeD(186000185); // Arena Of Glory Ticket.
							writeD(rewardedPlayer.getGloryTicket());
						} else {
							writeD(0);
							writeD(0);
						}
						writeD(0);
						writeD(0);
					}
				} else {
					writeB(new byte[60]);
				}
				writeD(arenaReward.getBuffId());
				writeD(0);
				writeD(arenaReward.getRound());
				writeD(arenaReward.getCapPoints());
				writeD(3);
				writeD(0);
				break;
		}
	}

	private void fillTableWithGroup(Race race) {
		int count = 0;
		DredgionReward dredgionReward = (DredgionReward) instanceReward;
		for (Player player : players) {
			if (!race.equals(player.getRace())) {
				continue;
			}
			InstancePlayerReward playerReward = dredgionReward.getPlayerReward(player.getObjectId());
			DredgionPlayerReward dpr = (DredgionPlayerReward) playerReward;
			writeD(playerReward.getOwner());
			writeD(player.getAbyssRank().getRank().getId());
			writeD(dpr.getPvPKills());
			writeD(dpr.getMonsterKills());
			writeD(dpr.getZoneCaptured());
			writeD(dpr.getPoints());
			if (instanceScoreType.isEndProgress()) {
				boolean winner = race.equals(dredgionReward.getWinningRace());
				writeD((winner ? dredgionReward.getWinnerPoints() : dredgionReward.getLooserPoints()) + (int) (dpr.getPoints() * 1.6f));
				writeD((winner ? dredgionReward.getWinnerPoints() : dredgionReward.getLooserPoints()));
			} else {
				writeB(new byte[8]);
			}
			writeC(player.getPlayerClass().getClassId());
			writeC(0);
			writeS(player.getName(), 54);
			count++;
		}
		if (count < 6) {
			writeB(new byte[88 * (6 - count)]);
		}
	}
}
