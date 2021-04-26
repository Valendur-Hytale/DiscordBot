package de.valendur.discordbot.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import de.valendur.discordbot.Bot2;
import de.valendur.discordbot.Utils;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.LevelingConfig;
import de.valendur.discordbot.dbhandlers.DBDataHandler;
import de.valendur.discordbot.dbhandlers.DBLevelingHandler;
import de.valendur.discordbot.levelling.LevelingUser;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DataHandler extends ListenerAdapter {

	@Override
	public void onUserActivityEnd(UserActivityEndEvent event) {
		
	}

	@Override
	public void onUserActivityStart(UserActivityStartEvent event) {
	}

	@Override
	public void onUserUpdateActivityOrder(UserUpdateActivityOrderEvent event) {
	}

	@Override
	public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
		JSONObject data = new JSONObject();
		data.put("OLD_STATUS", event.getOldOnlineStatus());
		data.put("NEW_STATUS", event.getNewOnlineStatus());
		
		DBDataHandler.addEvent(event.getUser().getId(),EventType.STATUS_UPDATE, data);
		System.out.println(event.getMember().getEffectiveName()+"change their online status to "+ event.getNewOnlineStatus());
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		JSONObject data = new JSONObject();
		data.put("CHANNEL_ID", event.getChannel().getId());
		data.put("MESSAGE_LENGTH", event.getMessage().getContentDisplay().length());
		data.put("MESSAGE_CONTENT", event.getMessage().getContentDisplay());
		
		DBDataHandler.addEvent(event.getMember().getId(),EventType.MESSAGE_RECEIVED, data);	
		
	}
	
	

	
}
