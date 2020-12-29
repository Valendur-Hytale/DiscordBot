package de.valendur.discordbot.configs;


import java.util.ArrayList;
import java.util.HashMap;

import de.valendur.discordbot.levelling.MessageLengthExp;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class LevelingConfig extends GenericConfig {

	public String COMMAND_PREFIX;
	
	public int MESSAGE_DELAY;
	public HashMap<Integer, Integer> SPECIAL_MESSAGE_QUANTITIES = new HashMap<Integer, Integer>();
	public ArrayList<MessageLengthExp> MESSAGE_EXP_BY_LENGTH = new ArrayList<MessageLengthExp>();
	
	public LevelingConfig(ConfigType type) {
		super(type);
	}

	@Override
	public void load() {
		JSONObject config = readConfig();
		
		MESSAGE_DELAY = config.getInt("MESSAGE_DELAY");
		
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
	
}
