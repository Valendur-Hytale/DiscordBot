package de.valendur.discordbot.handlers;

import java.util.ArrayList;
import java.util.List;

import de.valendur.discordbot.Bot2;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.LevelingConfig;
import de.valendur.discordbot.dbhandlers.DBLevelingHandler;
import de.valendur.discordbot.levelling.LevelingUser;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LevelingHandler extends ListenerAdapter {
	
	List<LevelingUser> levelingUsers = new ArrayList<LevelingUser>();

	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent e) {	
		if (e.retrieveUser().complete().isBot()) {
			return;
		} 

	}
	
	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent e) {	

	}
	
	
	
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		if (e.getAuthor().isBot()) return;
		if (e.getMessage().getContentRaw().startsWith(Bot2.getBaseConfig().COMMAND_PREFIX)) return;
		System.out.println("MemberId: " + e.getMember().getIdLong() + " UserId: " + e.getAuthor().getIdLong());
		userSendMessage(e.getMember().getIdLong(), e.getMessage().getContentDisplay().length());
	}

	public void setup(Guild guild) {
		/*levelingUsers.clear();
		JSONArray json = Unirest.get(DBLevelingHandler.getConfig().BACKEND_LINK + "members/getAll").asJson().getBody().getArray();
		
		for (JSONObject user : (List<JSONObject>) json.toList()) {
			levelingUsers.add(new LevelingUser());
		}*/
		
	}
	
	public static LevelingConfig getConfig() {
		return (LevelingConfig) Bot2.configHandler.getConfig(ConfigType.LEVELING_CONFIG);
	}
	
	
	private void userSendMessage(long id, int length) {
		for (LevelingUser user : levelingUsers) {
			if (id == user.getId()) {
				user.addMessage(length);
			}
		}
		
		LevelingUser user = new LevelingUser(id);
		user.addMessage(length);
		
		levelingUsers.add(user);
	}
	
	public static void announcementUserLevelUp(JSONObject user) {
		Bot2.getGuild().getTextChannelById(getConfig().LEVELING_ANNOUNCEMENT_CHANNEL).sendMessage(user.toString()).queue();
	}
}
