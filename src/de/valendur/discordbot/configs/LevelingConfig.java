package de.valendur.discordbot.configs;

import java.util.List;

import de.valendur.discordbot.reactionrole.ReactionEmoteRole;
import de.valendur.discordbot.reactionrole.ReactionMessage;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class LevelingConfig extends GenericConfig {

	public String COMMAND_PREFIX;
	
	public LevelingConfig(ConfigType type) {
		super(type);
	}

	@Override
	public void load() {
		JSONObject config = readConfig();
		JSONArray messages = config.getJSONArray("MESSAGES_QUANTITY");
		
		for (int i = 0; i < messages.length(); i++) {
			JSONObject message = messages.getJSONObject(i);
			
			
		}
	}
	
	
	

}
