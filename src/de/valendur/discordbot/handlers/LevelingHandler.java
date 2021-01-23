package de.valendur.discordbot.handlers;

import java.util.ArrayList;
import java.util.HashMap;
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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LevelingHandler extends ListenerAdapter {
	
	HashMap<Long,LevelingUser> levelingUsers = new HashMap<Long,LevelingUser>();

	//List<LevelingUser> levelingUsers = new ArrayList<LevelingUser>();

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent e) {
		final User user = e.retrieveUser().complete();
		if (user.isBot()) {
			return;
		}
		userReactedToMessage(user.getIdLong());

	}

	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent e) {

	}
	
	

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		if (e.getAuthor().isBot())
			return;
		if (e.getMessage().getContentRaw().startsWith(Bot2.getBaseConfig().COMMAND_PREFIX))
			return;
		// System.out.println("MemberId: " + e.getMember().getIdLong() + " UserId: " +
		// e.getAuthor().getIdLong());
		System.out.println("Received Message by: " + e.getMember().getIdLong());
		userSendMessage(e.getMember().getIdLong(), e.getMessage().getContentDisplay().length());
	}

	public void setup(Guild guild) {
		/*
		 * levelingUsers.clear(); JSONArray json =
		 * Unirest.get(DBLevelingHandler.getConfig().BACKEND_LINK +
		 * "members/getAll").asJson().getBody().getArray();
		 * 
		 * for (JSONObject user : (List<JSONObject>) json.toList()) {
		 * levelingUsers.add(new LevelingUser()); }
		 */
		levelingUsers.clear();
	}

	public static LevelingConfig getConfig() {
		return (LevelingConfig) Bot2.configHandler.getConfig(ConfigType.LEVELING_CONFIG);
	}

	private void userSendMessage(long id, int length) {
		LevelingUser user = levelingUsers.get(id);
		if (user == null) {
			user = new LevelingUser(id);
			user.addMessage(length);
			levelingUsers.put(id, user);
			return;
		}
		user.addMessage(length);

	}

	private void userReactedToMessage(long id) {
		LevelingUser user = levelingUsers.get(id);
		if (user == null) {
			user = new LevelingUser(id);
			user.addReact();
			levelingUsers.put(id, user);
			return;
		}
		user.addReact();
	}

	public static void announcementUserLevelUp(JSONObject user) {
		Bot2.getGuild().getTextChannelById(getConfig().LEVELING_ANNOUNCEMENT_CHANNEL).sendMessage(user.toString())
				.queue();
	}
}
