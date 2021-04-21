package de.valendur.discordbot.dbhandlers;

import de.valendur.discordbot.handlers.LevelingHandler;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class DBLevelingHandler {
	
	
	
	public static void addExpToUser(long id, int exp, boolean message, String name, String url) {
		JSONObject json = new JSONObject();
		json.put("userID", ""+id);
		json.put("exp", exp);
		json.put("message", message);
		if (name != null) json.put("profileName", name);
		if (url != null) json.put("profileImage", url);
		
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
	
	
	public static JSONArray getAllUsers() { 
		return Unirest.get("members/").asJson().getBody().getArray(); 
	} 
	
}
