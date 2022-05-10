package quest.beluslan;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author gigi
 *
 */
public class _2654SpyTheLastPersuasion extends QuestHandler
{
   private final static int   questId = 2654;
   
   public _2654SpyTheLastPersuasion()
   {
      super(questId);
   }
   
   @Override
   public void register()
   {
      qe.setNpcQuestData(204775).addOnQuestStart(questId);
      qe.setNpcQuestData(204775).addOnTalkEvent(questId);
      qe.setNpcQuestData(204621).addOnTalkEvent(questId);
   }
   
   public boolean onLvlUpEvent(QuestCookie env)
   {
	   return defaultQuestOnLvlUpEvent(env, 2653);
   }
   
   @Override
   public boolean onDialogEvent(QuestCookie env)
   {
	   int targetId = 0;
	   if(env.getVisibleObject() instanceof Npc)
		   targetId = ((Npc) env.getVisibleObject()).getNpcId();
	   final QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
	   if(targetId == 204775)
	   {
		   if(qs == null || qs.getStatus() == QuestStatus.NONE)
		   {
			   if(env.getDialogId() == 26)
				   return sendQuestDialog(env, 1011);
			   else 
				   return defaultQuestStartDialog(env);
		   }
      }
      else if(targetId == 204655)
      {
          if(qs != null)
          {
             if(env.getDialogId() == 26 && qs.getStatus() == QuestStatus.START)
                return sendQuestDialog(env, 2375);
             else if(env.getDialogId() == 1009)
             {
                qs.setQuestVar(0);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return defaultQuestEndDialog(env);
             }
             else
                return defaultQuestEndDialog(env);
          }
      }
	   return false;
   }   
}