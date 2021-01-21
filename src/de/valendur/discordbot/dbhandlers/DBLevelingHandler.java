package de.valendur.discordbot.dbhandlers;

import de.valendur.discordbot.Bot2;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.ReactionEmoteRoleConfig;
import de.valendur.discordbot.handlers.LevelingHandler;
import kong.unirest.Unirest;
import kong.unirest.json.JSONElement;
import kong.unirest.json.JSONObject;

public class DBLevelingHandler {
	
	
	
	public static void addExpToUser(long id, int exp) {
		JSONObject json = new JSONObject();
		json.put("userID", ""+id);
		json.put("exp", exp);
		Unirest.post("members/addExp").body(json).asJsonAsync(response -> {
			System.out.println(response.getBody().getObject().toString());
			JSONObject user = response.getBody().getObject();
			if (user.getBoolean("levelUp")) {
				LevelingHandler.announcementUserLevelUp(user);
			}
		});
		
	}
	
	
	public static JSONObject getUser(long id) {
		return Unirest.get("members/" + id).asJson().getBody().getObject();
	}
	
	
}
