package de.valendur.discordbot.configs;

import kong.unirest.json.JSONObject;

public class BaseConfig extends GenericConfig {

	public String COMMAND_PREFIX;
	public String BACKEND_LINK;
	public String ERROR_CHANNEL;
	public String BIRTHDAY_CHANNEL;
	public String BIRTHDAY_MESSAGE;
	public long BIRTHDAY_ROLE_ID;
	
	public int SCHEDULING_LEVELING_RESET_HOUR, SCHEDULING_LEVELING_RESET_MINUTE, SCHEDULING_LEVELING_RESET_SECOND;
	public int SCHEDULING_VOICE_CHECK;
	
	public BaseConfig(ConfigType type) {
		super(type);
	}

	@Override
	public void load() {
		JSONObject config = readConfig();
		COMMAND_PREFIX = config.getString("COMMAND_PREFIX");
		BACKEND_LINK = config.getString("BACKEND_LINK");
		ERROR_CHANNEL = config.getString("ERROR_CHANNEL");
		
		JSONObject SCHEDULING = config.getJSONObject("SCHEDULER");
		
		JSONObject SCHEDULING_LEVELING_RESET = SCHEDULING.getJSONObject("LEVELING_RESET");
		SCHEDULING_LEVELING_RESET_HOUR = SCHEDULING_LEVELING_RESET.getInt("HOUR");
		SCHEDULING_LEVELING_RESET_MINUTE = SCHEDULING_LEVELING_RESET.getInt("MINUTE");
		SCHEDULING_LEVELING_RESET_SECOND = SCHEDULING_LEVELING_RESET.getInt("SECOND");
		
		SCHEDULING_VOICE_CHECK = SCHEDULING.getInt("VOICE_CHECK_EVERY_SECONDS");
		
	}
	

}
