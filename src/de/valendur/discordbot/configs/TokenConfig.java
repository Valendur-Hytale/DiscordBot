package de.valendur.discordbot.configs;

import kong.unirest.json.JSONObject;

public class TokenConfig extends GenericConfig {

	public String BOT_TOKEN, TWITCH_CLIENT_ID, TWITCH_CLIENT_SECRET;
	
	public TokenConfig(ConfigType type) {
		super(type);
	}

	@Override
	public void load() {
		JSONObject config = readConfig();
		BOT_TOKEN = config.getString("BOT_TOKEN");
		TWITCH_CLIENT_ID = config.getString("TWITCH_CLIENT_ID");
		TWITCH_CLIENT_SECRET = config.getString("TWITCH_CLIENT_SECRET");
	}
	
	
	

}
