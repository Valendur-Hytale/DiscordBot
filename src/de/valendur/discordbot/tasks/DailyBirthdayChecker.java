package de.valendur.discordbot.tasks;

import java.time.LocalDate;
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
		System.out.println("Starting Birthday Check");
		Guild g = Bot.getGuild();
		JSONArray users = DBLevelingHandler.getAllUsers();
		try {
			Role role = g.getRoleById(Bot.getBaseConfig().BIRTHDAY_ROLE_ID);
			final DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


			// The parsed date
			
			ZonedDateTime today = ZonedDateTime.now();
			users.forEach(obj -> {
				JSONObject user = (JSONObject) obj;
				//System.out.println(user.toString());
				g.retrieveMemberById(user.getString("userID")).queue(member -> {
					if (member.getRoles().contains(role)) {
						g.removeRoleFromMember(member, role).queue();
					} else {
						final LocalDate birthday = LocalDate.parse(user.getString("birthday"), inputFormat);
						//System.out.println(today.getMonth() + "=" + birthday.getMonth() + ", " + today.getDayOfMonth() + "=" + birthday.getDayOfMonth());
						if (today.getMonth() == birthday.getMonth() && today.getDayOfMonth() == birthday.getDayOfMonth()) {
							System.out.println("Adding role");
							g.addRoleToMember(member, role).queue();
							g.getTextChannelById(Bot.getBaseConfig().BIRTHDAY_CHANNEL).sendMessage(Bot.getBaseConfig().BIRTHDAY_MESSAGE.replace("%user%", member.getAsMention()));
						}
					}
					
				});
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
