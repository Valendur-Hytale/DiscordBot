package de.valendur.discordbot.configs;

import kong.unirest.json.JSONObject;

public class BaseConfig extends GenericConfig {

	public String COMMAND_PREFIX;
	public String BACKEND_LINK;
	public String ERROR_CHANNEL;
	
	public int SCHEDULING_LEVELING_RESET_HOUR, SCHEDULING_LEVELING_RESET_MINUTE, SCHEDULING_LEVELING_RESET_SECOND;
	
	public BaseConfig(ConfigType type) {
		super(type);
	}

	@Override
	public void load() {
		JSONObject config = readConfig();
		COMMAND_PREFIX = config.getString("COMMAND_PREFIX");
		BACKEND_LINK = config.getString("BACKEND_LINK");
		ERROR_CHANNEL = config.getString("ERROR_CHANNEL");
		
		JSONObject SCHEDULING_LEVELING_RESET = config.getJSONObject("SCHEDULER").getJSONObject("LEVELING_RESET");
		SCHEDULING_LEVELING_RESET_HOUR = SCHEDULING_LEVELING_RESET.getInt("HOUR");
		SCHEDULING_LEVELING_RESET_MINUTE = SCHEDULING_LEVELING_RESET.getInt("MINUTE");
		SCHEDULING_LEVELING_RESET_SECOND = SCHEDULING_LEVELING_RESET.getInt("SECOND");
		
		
	}
	

}
