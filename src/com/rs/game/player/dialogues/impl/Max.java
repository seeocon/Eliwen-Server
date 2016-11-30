package com.rs.game.player.dialogues.impl;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.content.PlayerLook;
import com.rs.game.player.content.custom.StarterItems;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;


public class Max extends Dialogue {

	public Max() {
	}

	@Override
	public void start() {
		player.getHintIconsManager().removeUnsavedHintIcon();
		if (player.getClassName() == null) {
			sendMessage("Congratulations on your max, " + player.getDisplayName() + "!",
					"Investigate the stand behind me to claim your cape!");
			World.sendWorldMessage(player.getDisplayName() + " has just been awarded a Max Cape!", false);
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {

	}

	public void sendMessage(String... message) {
		sendEntityDialogue(IS_NPC, Settings.SERVER_NAME , 3705, 9827, message);
	}
	
	@Override
	public void finish() {

	}

}
