package de.valendur.discordbot.configs;

import kong.unirest.json.JSONObject;

public class BaseConfig extends GenericConfig {

	public String COMMAND_PREFIX;
	
	public BaseConfig(ConfigType type) {
		super(type);
	}

	@Override
	public void load() {
		JSONObject config = readConfig();
		COMMAND_PREFIX = config.getString("COMMAND_PREFIX");
		
		
	}
	
	
	

}
