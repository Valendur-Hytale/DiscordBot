package de.valendur.discordbot.configs;

import kong.unirest.json.JSONObject;

public class TokenConfig extends GenericConfig {

	public String BOT_TOKEN;
	
	public TokenConfig(ConfigType type) {
		super(type);
	}

	@Override
	public void load() {
		JSONObject config = readConfig();
		BOT_TOKEN = config.getString("BOT_TOKEN");
		
		
	}
	
	
	

}
