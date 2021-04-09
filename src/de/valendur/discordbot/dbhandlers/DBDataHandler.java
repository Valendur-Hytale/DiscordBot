package de.valendur.discordbot.dbhandlers;

import de.valendur.discordbot.handlers.LevelingHandler;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

public class DBDataHandler {
	
	
	public static void addEvent(String userID, String eventType, JSONObject data) {
		JSONObject json = new JSONObject();
		JSONObject dataLog = new JSONObject();
		dataLog.put("eventType", eventType);
		dataLog.put("data", data);
		
		json.put("userID", userID);
		json.put("dataLog", dataLog);
		
		Unirest.post("data/addData").body(json).asJsonAsync(response -> {
			System.out.println(response.getBody().getObject().toString());
//			JSONObject user = response.getBody().getObject();
//			if (user.getBoolean("levelUp")) {
//				LevelingHandler.announcementUserLevelUp(user);
//			}
		});
	}

}