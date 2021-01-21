package de.valendur.discordbot.configs;


import java.util.ArrayList;
import java.util.HashMap;

import de.valendur.discordbot.BotLogger;
import de.valendur.discordbot.handlers.RandomHandler;
import de.valendur.discordbot.levelling.MessageLengthExp;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class LevelingConfig extends GenericConfig {

	public String LEVELING_ANNOUNCEMENT_CHANNEL;
	
	
	public int REACTION_DELAY, REACTION_XP_MIN, REACTION_XP_MAX;
	
	public int MESSAGE_DELAY;
	public HashMap<Integer, Integer> SPECIAL_MESSAGE_QUANTITIES = new HashMap<Integer, Integer>();
	public ArrayList<MessageLengthExp> MESSAGE_EXP_BY_LENGTH = new ArrayList<MessageLengthExp>();
	
	public LevelingConfig(ConfigType type) {
		super(type);
	}

	@Override
	public void load() {
		JSONObject config = readConfig();
		
		REACTION_DELAY = config.getInt("REACTION_DELAY");
		REACTION_XP_MIN = config.getInt("REACTION_XP_MIN");
		REACTION_XP_MAX = config.getInt("REACTION_XP_MAX");
		
		MESSAGE_DELAY = config.getInt("MESSAGE_DELAY");
		LEVELING_ANNOUNCEMENT_CHANNEL = config.getString("LEVELING_ANNOUNCEMENT_CHANNEL");
		
		JSONArray specialMessageQuantities = config.getJSONArray("SPECIAL_MESSAGES_QUANTITIES");
		for (int i = 0; i < specialMessageQuantities.length(); i++) {
			JSONObject specialMessageQuantity = specialMessageQuantities.getJSONObject(i);
			SPECIAL_MESSAGE_QUANTITIES.put(specialMessageQuantity.getInt("QUANTITY"), specialMessageQuantity.getInt("EXP"));
		}
		
		JSONArray messagesExpByLength = config.getJSONArray("MESSAGE_EXP_BY_LENGTH");
		for (int i = 0; i < messagesExpByLength.length(); i++) {
			JSONObject messageExpByLength = messagesExpByLength.getJSONObject(i);
			MESSAGE_EXP_BY_LENGTH.add(new MessageLengthExp(messageExpByLength.getInt("LENGTH"), messageExpByLength.getInt("MIN_EXP"), messageExpByLength.getInt("MAX_EXP")));
		}
	}
	
	
	public int getExpByMessageLength(final int length) {
		for (MessageLengthExp messageLengthExp : MESSAGE_EXP_BY_LENGTH) {
			if (length <= messageLengthExp.getLength()) {
				return messageLengthExp.getExp();
			}
		}
		
		BotLogger.warning("Message length exp leveling config is fucked up");
		return -1;
	}
	
	public int getExpByReaction() {
		return RandomHandler.randInt(REACTION_XP_MIN, REACTION_XP_MAX);
	}
	
}
