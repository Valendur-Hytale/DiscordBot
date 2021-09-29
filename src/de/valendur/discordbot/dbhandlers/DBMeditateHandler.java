package de.valendur.discordbot.dbhandlers;

import de.valendur.discordbot.handlers.LevelingHandler;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.entities.TextChannel;

public class DBMeditateHandler {
	
	
	
	public static void addMeditateTime(long id, int meditateTime, TextChannel channel) {
		JSONObject json = new JSONObject();
		json.put("userID", ""+id);
		json.put("meditateTime", meditateTime);
		
		Unirest.post("members/addExp").body(json).asJsonAsync(response -> {
			System.out.println(response.getBody().getObject().toString());
			JSONObject user = response.getBody().getObject();
			channel.sendMessage("Du hast schon " + user.getInt("meditateTime") + " minuten meditiert! [Hier Random Text einfügen]").queue();
		});
	}
	
}
