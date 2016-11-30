package com.rs.game.player.dialogues.impl;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.PlayerLook;
import com.rs.game.player.content.custom.StarterItems;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.player.actions.ShipTravelAction;
import com.rs.utils.ShopsHandler;


public class NewStarter extends Dialogue {

	int starter;
	int tutorial = 6;


	public NewStarter() {
	}

	@Override
	public void start() {
		//PlayerLook.openThessaliasMakeOver(player);
		player.getHintIconsManager().removeUnsavedHintIcon();
		if (player.getClassName() == null) {
			sendMessage("Welcome to " + Settings.SERVER_NAME + " " + player.getDisplayName() + "!",
					"I can help you change your look, tell you things about the server",
					"or even let you choose your starter set.",
					"So what would you like to do?");
			stage = 0;
		} else {
			sendMessage("Halt " + player.getDisplayName() + ", you've already chosen a reward!",
					"I'm here to give new players items based on their preferred class.",
					"However I can give you some information about " + Settings.SERVER_NAME + ", ",
					"or even change your look So what would you like to do?");
			stage = 4;
		}
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (player.getInterfaceManager().containsScreenInter())
			player.getInterfaceManager().closeScreenInterface();
		
        if (player.getClassName() == null) {
			if (stage == 0) {
				sendOptionsDialogue("Pick a Starter Type",
						"I would like a Fighter's Starter",
						"I would like an Archer's Starter",
						"I would like a Magician's Starter");
				stage = 1;
			} else if (stage == 1) {
				if (componentId == OPTION_1)
					this.starter = StarterItems.FIGHTER;
				else if (componentId == OPTION_2)
					this.starter = StarterItems.ARCHER;
				else if (componentId == OPTION_3)
					this.starter = StarterItems.MAGICIAN;
				stage = 2;
				StarterItems.giveStarter(player, starter);
			}
		} else {
			if (stage == 4) {
				sendOptionsDialogue("Choose an option",
						"I would like to change my appearance",
						"I would like to change my hairstyle",
						"I would like to understand how to play");
				stage = 5;
			} else if (stage == 5){
				if (componentId == OPTION_1) {
					player.getInterfaceManager().closeChatBoxInterface();
					PlayerLook.openMageMakeOver(player);
				} else if (componentId == OPTION_2) {
					player.getInterfaceManager().closeChatBoxInterface();
					PlayerLook.openHairdresserSalon(player);
				} // TUTORIAL HERE
				else if (componentId == OPTION_3) {
					sendMessage(
							"To make some quick cash, you could always do some skilling",
							"and sell the items you get from it (logs, ors, etc.).",
							"If you're not into skilling, train up combat and boss",
							"for some nice gear and profitable items. I'll show you around.");
					stage = 7;
				}
			} else if (stage == 7){
				player.setNextWorldTile(new WorldTile(2214, 3251, 0));
				sendMessage(
						"Here you can buy your basic skilling supplies,",
						"such as axes, logs, picks, and ore!",
						"right click and trade if you're interested!");
				stage = 8;
			} else if (stage == 8){
				player.setNextWorldTile(new WorldTile(2208,3242, 0));
				sendMessage(
						"Down here, you can change your prayer to ancient curses!");
				stage = 9;
			} else if (stage == 9){
				player.setNextWorldTile(new WorldTile(2207,3258, 0));
				sendMessage(
						"Down here, you can recharge your prayers, or change",
						"your spellbook to ancients, if desired.");
				stage = 10;
			} else if (stage == 10) {
				player.setNextWorldTile(new WorldTile(2201, 3254, 0));
				sendMessage(
						"Here is the main bank. (obviously)");
				player.getInterfaceManager().sendInterface(762);
				stage = 11;
			} else if (stage == 11) {
				player.setNextWorldTile(new WorldTile(2199, 3255, 0));
				sendMessage("To the left of the bank, there is some combat shops!",
							"Poor Lowe to the left couldn't fit inside.");
				stage = 12;
			} else if (stage == 12) {
				player.setNextWorldTile(new WorldTile(2183, 3260, 0));
				sendMessage("Hey look! Over there! The wonderful slayer master Kuradel,",
						"herself. If you tend on leveling your combat in " + Settings.SERVER_NAME + ",",
						"it is recommended that you train slayer as well to make some quick cash!");
				stage = 13;
			} else if (stage == 13) {
				player.setNextWorldTile(new WorldTile(2199, 3249, 0));
				sendMessage("Hope you haven't ran off yet, here's your typical crafting merchant,",
						"he will be here to guide you through your journey of 1-99 crafting,",
						"don't even bother trying to steal from him, you aren't able to!");
				stage = 14;
			} else if (stage == 14) {
				player.setNextWorldTile(new WorldTile(2192, 3249, 0));
				sendMessage("Jatix right here, sells all the herblore goods, he came all the way from Taverly",
						"to ensure that the heart of " + Settings.SERVER_NAME + " gets all the latest goods.",
						"He kind of reeks of herbs so be quick!");
				stage = 15;
			} else if (stage == 15) {
				player.setNextWorldTile(new WorldTile(2194, 3237, 0));
				sendMessage("Here's your lunar altar, if you ever have the need to switch spellbooks,",
							"here is your best friend.");
				stage = 16;
			} else if (stage == 16) {
				player.setNextWorldTile(new WorldTile(2183, 3244, 0));
				sendMessage("Last but definitely not least, this is Hank.",
						"sounds like your average handyman, however he is not only a handyman",
						"but a cook/fisherman. If you ever feel the need to binge on food or level",
						"your cooking, don't hesitate to speak with Hank!");
				stage = 17;
			} else if (stage == 17) {
				player.setNextWorldTile(new WorldTile(2204, 3250, 0));
				sendMessage(
						"I have set you in this fire MUAHAHAHAH.",
						"So that's it for our little tour around the home of " + Settings.SERVER_NAME + ",",
						"if you have any questions, please ask any of the staff!");
			}

		}
        
        /* else if (stage == 2) {
				if (componentId == OPTION_1) {
					player.getInterfaceManager().closeChatBoxInterface();
					PlayerLook.openMageMakeOver(player);
				} else if (componentId == OPTION_2) {
					player.getInterfaceManager().closeChatBoxInterface();
					PlayerLook.openHairdresserSalon(player);
				} else if (componentId == OPTION_3) {
					player.getInterfaceManager().closeChatBoxInterface();
					PlayerLook.openThessaliasMakeOver(player);
				}
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				ShopsHandler.openShop(player, 34);
				end();
			}
			if (componentId == OPTION_2) {
				ShopsHandler.openShop(player, 46);
				end();
			}
			if (componentId == OPTION_3) {
				ShopsHandler.openShop(player, 50);
				end();
			}
		} else if (stage == 4) {
			sendMessage(
				"To make some quick cash, you could always do some skilling", 
				"and sell the items you get from it (logs, ors, etc.).",
				"If you're not into skilling, train up combat and boss", 
				"for some nice gear and profitable items. What else can i do for ya?");
			player.getInterfaceManager().openGameTab(3);
			stage = 5;
		} else if (stage == 5) {
			sendOptionsDialogue(""+Settings.SERVER_NAME+" Manager Options",
					"I'd like to change my appearance",
					"I want to view the Point Shops",
					"I would like to go to the Trainer Zone",
					"I want to know more about the server");
			stage = 1;
			player.getInterfaceManager().openGameTab(4);
		}*/

	}
	
	public void sendMessage(String... message) {
		sendEntityDialogue(IS_NPC, Settings.SERVER_NAME , 1182, 9827, message);
	}

	private WorldTile donatorZone = new WorldTile(1807, 3211, 0);
	
	public void startDemonTeleport() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop = 0;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(17108));
					player.setNextGraphics(new Graphics(3224));
					player.setNextGraphics(new Graphics(3225));
				} else if (loop == 9) {
					player.setNextWorldTile(donatorZone);
				} else if (loop == 10) {
					player.setNextAnimation(new Animation(16386));
					player.setNextGraphics(new Graphics(3019));
				} else if (loop == 11) {
					player.setNextAnimation(new Animation(808));
					player.setNextGraphics(new Graphics(-1));
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	@Override
	public void finish() {
		player.getInterfaceManager().openGameTab(4);
	}

}
