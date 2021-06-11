package de.valendur.discordbot.tasks;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import de.valendur.discordbot.Bot;
import de.valendur.discordbot.dbhandlers.DBLevelingHandler;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public class DailyBirthdayChecker extends GenericScheduledTask {

	public DailyBirthdayChecker(int hour, int minute, int second) {
		super(hour, minute, second);
	}

	@Override
	public void execute() {
		Guild g = Bot.getGuild();
		JSONArray users = DBLevelingHandler.getAllUsers();
		Role role = g.getRoleById(Bot.getBaseConfig().BIRTHDAY_ROLE_ID);
		final DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

		// The parsed date
		
		ZonedDateTime today = ZonedDateTime.now();
		users.forEach(obj -> {
			JSONObject user = (JSONObject) obj;
			g.retrieveMemberById(user.getString("userID")).queue(member -> {
				if (member.getRoles().contains(role)) {
					g.removeRoleFromMember(member, role).queue();
				} else {
					final ZonedDateTime birthday = ZonedDateTime.parse(user.getString("birthday"), inputFormat);
					
					if (today.getMonth() == birthday.getMonth() && today.getDayOfMonth() == birthday.getDayOfMonth()) {
						g.addRoleToMember(member, role);
						g.getTextChannelById(Bot.getBaseConfig().BIRTHDAY_CHANNEL).sendMessage(Bot.getBaseConfig().BIRTHDAY_MESSAGE.replace("%user%", member.getAsMention()));
					}
				}
				
			});
		});
	}
	

}
