package com.rs;

import com.rs.utils.Utils;

public class PlayerMain {
	
	private static String[] latestNews = { 
		"Don't forget to ::vote for some extra cash or items!",
		"Donate to help keep the server alive since we are just starting off!",
		"You can ::prestige once you get all combat skills to 99!",
		"View your achievements by clicking Account Manager on the quest tab!",
		"Register on the ::forums and help keep the community alive!"
	};
	
	public static String getLatestNews() {
		return latestNews[Utils.random(latestNews.length - 1)];
	}
	
}
