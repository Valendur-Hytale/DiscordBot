package de.valendur.discordbot.dbhandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.valendur.discordbot.Utils;
import de.valendur.discordbot.tasks.Team;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.entities.TextChannel;

public class DBPrimeLeagueHandler {
	
	
	
	public static void addTeam(TextChannel channel, Team team) {
		JSONObject json = new JSONObject();
		json.put("teamID", team.teamID);
		try {
			json.put("serializedTeam", Utils.toString(team));
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Unirest.post("prime/addTeam").body(json).asJsonAsync(response -> {
			channel.sendMessage("Du hast das Team erfolgreich hinzugefügt").queue();
		});
	}
	
	public static void updateTeam(Team team) {
		JSONObject json = new JSONObject();
		json.put("teamID", team.teamID);
		try {
			json.put("serializedTeam", Utils.toString(team));
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Unirest.post("prime/updateTeam").body(json).asJsonAsync(response -> {
			
		});
	}
	
	
	public static List<Team> getTeams() {
		List<Team> list = new ArrayList<Team>();
		JSONArray array = Unirest.get("prime/teams").asJson().getBody().getArray(); 
		for (Object o : array) {
			JSONObject teamObject = (JSONObject) o;
			try {
				list.add((Team) Utils.fromString(teamObject.getString("serializedTeam")));
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
}
