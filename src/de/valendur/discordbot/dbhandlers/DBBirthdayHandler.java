package de.valendur.discordbot.dbhandlers;

import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

public class DBBirthdayHandler {
	
	
	
	public static void setBirthdayForUser(long id, int day, int month) {
		JSONObject json = new JSONObject();
		json.put("userID", ""+id);
		json.put("day", day);
		json.put("month", month);
		
		Unirest.post("members/setBirthday").body(json).asJsonAsync();
	}
	

}
