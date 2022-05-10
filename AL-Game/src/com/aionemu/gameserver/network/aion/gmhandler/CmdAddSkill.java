package com.aionemu.gameserver.network.aion.gmhandler;

import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;

/**
 * Created by Kill3r
 */
public class CmdAddSkill extends AbstractGMHandler {

	public CmdAddSkill(Player admin,String params){
		super(admin,params);
		run();
	}

	public void run(){
		String skillName = params;
		List<SkillTemplate> skill = DataManager.SKILL_DATA.getSkillTemplates();

		if(skillName.contains("_G")){
			skillName = "SKILL_" + params;
			if(checkLevel(params) == 1){
				skillName = skillName.replaceAll("_G1", "");
			}else if(checkLevel(params) == 2){
				skillName = skillName.replaceAll("_G2", "");
			}else if(checkLevel(params) == 3){
				skillName = skillName.replaceAll("_G3", "");
			}else if(checkLevel(params) == 4){
				skillName = skillName.replaceAll("_G4", "");
			}else if(checkLevel(params) == 5){
				skillName = skillName.replaceAll("_G5", "");
			}else if(checkLevel(params) == 6){
				skillName = skillName.replaceAll("_G6", "");
			}else if(checkLevel(params) == 7){
				skillName = skillName.replaceAll("_G7", "");
			}else if(checkLevel(params) == 8){
				skillName = skillName.replaceAll("_G8", "");
			}else if(checkLevel(params) == 9){
				skillName = skillName.replaceAll("_G9", "");
			}else if(checkLevel(params) == 10){
				skillName = skillName.replaceAll("_G10", "");
			}
		}else{
			skillName = params;
		}

		for(SkillTemplate s : skill){
			if(s.getStack().equalsIgnoreCase(skillName)){
				admin.getSkillList().addSkill(admin, s.getSkillId(), checkLevel(params));
			}
		}
	}

	private int checkLevel(String string){
		if(string.endsWith("G1")){
			return 1;
		}else if(string.endsWith("G2")){
			return 2;
		}else if(string.endsWith("G3")){
			return 3;
		}else if(string.endsWith("G4")){
			return 4;
		}else if(string.endsWith("G5")){
			return 5;
		}else if(string.endsWith("G6")){
			return 6;
		}else if(string.endsWith("G7")){
			return 7;
		}else{
			return 1;
		}
	}
}
