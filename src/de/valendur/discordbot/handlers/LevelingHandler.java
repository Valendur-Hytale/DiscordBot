package de.valendur.discordbot.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import de.valendur.discordbot.Bot;
import de.valendur.discordbot.Utils;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.LevelRoleConfig;
import de.valendur.discordbot.configs.LevelingConfig;
import de.valendur.discordbot.dbhandlers.DBLevelingHandler;
import de.valendur.discordbot.levelling.LevelingUser;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LevelingHandler extends ListenerAdapter {

	public HashMap<Long, LevelingUser> levelingUsers = new HashMap<Long, LevelingUser>();

	// List<LevelingUser> levelingUsers = new ArrayList<LevelingUser>();

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent e) {
		DBLevelingHandler.delUser(e.getMember().getIdLong());
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent e) {
		e.retrieveMember().queue(member -> {
			final User user = member.getUser();
			if (user.isBot()) {
				return;
			}
			userReactedToMessage(user.getIdLong(), member.getEffectiveName(), user.getEffectiveAvatarUrl());
		});

	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		if (e.getAuthor().isBot())
			return;
		if (e.getMessage().getContentRaw().startsWith(Bot.getBaseConfig().COMMAND_PREFIX))
			return;
		// System.out.println("MemberId: " + e.getMember().getIdLong() + " UserId: " +
		// e.getAuthor().getIdLong());
		System.out.println("Received Message by: " + e.getMember().getIdLong());
		userSendMessage(e.getMember().getIdLong(), e.getMessage().getContentDisplay().length(),
				e.getMember().getEffectiveName(), e.getAuthor().getAvatarUrl());
	}

	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
		if (e.getMember().getUser().isBot())
			return;
		userChangedVoiceState(e.getMember().getIdLong(), true);
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
		if (e.getMember().getUser().isBot())
			return;
		userChangedVoiceState(e.getMember().getIdLong(), false);
	}

	public void setup(Guild guild) {
		levelingUsers.clear();

		for (Member member : Utils.getUsersInVoiceChannel()) {
			userChangedVoiceState(member.getIdLong(), true);
		}
	}

	public static LevelingConfig getConfig() {
		return (LevelingConfig) Bot.configHandler.getConfig(ConfigType.LEVELING_CONFIG);
	}

	private void userSendMessage(long id, int length, final String name, final String url) {
		LevelingUser user = levelingUsers.get(id);
		if (user == null) {
			user = new LevelingUser(id);
			user.addMessage(length, name, url);
			levelingUsers.put(id, user);
			return;
		}
		user.addMessage(length, name, url);

	}

	private void userReactedToMessage(long id, final String name, final String url) {
		LevelingUser user = levelingUsers.get(id);
		if (user == null) {
			user = new LevelingUser(id);
			user.addReact(name, url);
			levelingUsers.put(id, user);
			return;
		}
		user.addReact(name, url);
	}

	private void userChangedVoiceState(long id, boolean inVoice) {
		LevelingUser user = levelingUsers.get(id);
		if (user == null) {
			user = new LevelingUser(id);
			user.setVoiceState(inVoice);
			levelingUsers.put(id, user);
			return;
		}
		user.setVoiceState(inVoice);
	}

	/**
	 * Temporarily Stores the Roles in here gets outsourced to a Config
	 * @param user
	 */
	public static void announcementUserLevelUp(JSONObject user) {
		LevelRoleConfig config = Bot.getLevelRoleConfig();
		Guild guild =  Bot.getGuild();
		Member lvlupMember = guild.retrieveMemberById(user.getInt("userID")).complete();
		int currentLvl = user.getInt("currentLevel");

		Role levelfive = config.getAsRole("5");
		Role levelten = config.getAsRole("10");
		Role levelfifteen = config.getAsRole("15");
		Role leveltwentyfive = config.getAsRole("25");
		Role levelthirtyfive = config.getAsRole("35");
		Role levelfifty = config.getAsRole("50");
		Role levelseventyfive = config.getAsRole("75");
		Role levelhundred = config.getAsRole("100");
		Role levelhundredtwentyfive = config.getAsRole("125");
		Role levelhundredfifty = config.getAsRole("150");
		Role levelhundredseventyfive = config.getAsRole("175");
		Role leveltwohundred = config.getAsRole("200");

		if(currentLvl == 5){
			Role games = config.getAsRole("GAMES");
			Role aboutme = config.getAsRole("ABOUT_ME");
			Role hobbies = config.getAsRole("HOBBIES");
			lvlupMember.getRoles().add(games);
			lvlupMember.getRoles().add(aboutme);
			lvlupMember.getRoles().add(hobbies);
			lvlupMember.getRoles().add(levelfive);
		} else if(currentLvl == 10){
			lvlupMember.getRoles().remove(levelfive);
			lvlupMember.getRoles().add(levelten);
		} else if(currentLvl == 15){
			lvlupMember.getRoles().remove(levelten);
			lvlupMember.getRoles().add(levelfifteen);
		} else if(currentLvl == 25){
			lvlupMember.getRoles().remove(levelfifteen);
			lvlupMember.getRoles().add(leveltwentyfive);
		} else if(currentLvl == 35){
			lvlupMember.getRoles().remove(leveltwentyfive);
			lvlupMember.getRoles().add(levelthirtyfive);
		} else if(currentLvl == 50){
			lvlupMember.getRoles().remove(levelthirtyfive);
			lvlupMember.getRoles().add(levelfifty);
		} else if(currentLvl == 75){
			lvlupMember.getRoles().remove(levelfifty);
			lvlupMember.getRoles().add(levelseventyfive);
		} else if(currentLvl == 100){
			lvlupMember.getRoles().remove(levelseventyfive);
			lvlupMember.getRoles().add(levelhundred);
		} else if(currentLvl == 125){
			lvlupMember.getRoles().remove(levelhundred);
			lvlupMember.getRoles().add(levelhundredtwentyfive);
		} else if(currentLvl == 150){
			lvlupMember.getRoles().remove(levelhundredtwentyfive);
			lvlupMember.getRoles().add(levelhundredfifty);
		} else if(currentLvl == 175){
			lvlupMember.getRoles().remove(levelhundredfifty);
			lvlupMember.getRoles().add(levelhundredseventyfive);
		} else if(currentLvl == 200){
			lvlupMember.getRoles().remove(levelhundredseventyfive);
			lvlupMember.getRoles().add(leveltwohundred);
		}

	}
}
