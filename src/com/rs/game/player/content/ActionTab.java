package com.rs.game.player.content;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

/**
 * 
 * @author Chaoz
 */
public class ActionTab {
	

	private static boolean resizableScreen;
	
	public static void sendTab(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run(){
				double kill = player.getKillCount();
				double death = player.getDeathCount();
				double kdr = kill / death;
				
				player.getPackets().sendIComponentText(930, 10, "<col=ffffff>Re-open the tab to refresh.</col>");
				player.getPackets().sendIComponentText(930, 16, 
					//"Server: <col=FFFFFF>"+Settings.SERVER_NAME+"</col><br>" +
                    //"" +
					//"Rank: <col=FFFFFF>"+player.getAccountType()+"</col><br>" +
                    //"" +
					"Online: <col=FFFFFF>"+World.getPlayers().size()+" </col><br>" +
                    "" +
					//"Owner: <col=FFFFFF>"+Settings.OWNERS[0]+"</col><br>" +
                    "" +
					"Skill Points: <col=FFFFFF>"+player.getSkillPoints()+"</col><br>" +
                    "" +
					"PvP Kills: <col=FFFFFF>"+player.getKillCount()+"</col><br>" +
                    "" +
					"PvP Deaths: <col=FFFFFF>"+player.getDeathCount()+"</col><br>" +
                    "" +
					"Loyalty Points: <col=FFFFFF>"+Utils.formatNumber(player.getLoyaltyPoints())+"</col><br>" +
                    "" +
					"Prestige Level: <col=FFFFFF>"+Utils.formatNumber(player.getPrestigeLevel())+"</col><br>" +
                    "" +
					"Prestige Points: <col=FFFFFF>"+Utils.formatNumber(player.getPrestigePoints())+"</col><br>" +
                    "" +
					"Achievements: <col=FFFFFF>"+player.getGoals().getNumberCompleted()+" / "+PlayerGoals.totalAchs+"</col>");	
			}
		}, 0, 5);
	}
	
}