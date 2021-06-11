package de.valendur.discordbot.tasks;

import de.valendur.discordbot.Bot;
import de.valendur.discordbot.dbhandlers.DBLevelingHandler;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.entities.Guild;

public class DailyWebsiteFixer extends GenericScheduledTask {

	public DailyWebsiteFixer(int hour, int minute, int second) {
		super(hour, minute, second);
	}

	@Override
	public void execute() {
		Guild g = Bot.getGuild();
		JSONArray users = DBLevelingHandler.getAllUsers();
		users.forEach(obj -> {
			JSONObject user = (JSONObject) obj;
			g.retrieveMemberById(user.getString("userID")).queue(member -> {
				DBLevelingHandler.addExpToUser(member.getIdLong(), 0, false, member.getEffectiveName(), member.getUser().getEffectiveAvatarUrl());
			});
		});
	}
	

}
