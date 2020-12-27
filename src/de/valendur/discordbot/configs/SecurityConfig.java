package de.valendur.discordbot.configs;

import kong.unirest.json.JSONObject;

public class SecurityConfig extends GenericConfig {

	public String SECURITY_CHANNEL;
	
	public SecurityConfig(ConfigType type) {
		super(type);
	}

	@Override
	public void load() {
		JSONObject config = readConfig();
		SECURITY_CHANNEL = config.getString("SECURITY_CHANNEL");
		
		
	}
	
	
	

}
