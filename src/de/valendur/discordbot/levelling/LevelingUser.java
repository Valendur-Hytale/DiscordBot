package de.valendur.discordbot.levelling;

import de.valendur.discordbot.Bot2;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.LevelingConfig;

public class LevelingUser {
	
	private long lastReaction, lastMessage;
	private long memberId;
	
	private int messageCount;
	
	
	
	public long getId() {
		return memberId;
	}
	
	public boolean addMessage(int length) {
		final LevelingConfig config = getConfig();
		
		messageCount += 1;
		final long currentTime = System.currentTimeMillis();
		
		if (lastMessage + config.MESSAGE_DELAY < currentTime) {
			lastMessage = currentTime;
			
			
			backendAddExp(config.getExpByMessageLength(length));
		}
		
		
		
		
		
		
		return false; // TODO Return based on backend levelup response
	}
	
	
	private void backendAddExp(int exp) {
		
	}

	
	
	public LevelingConfig getConfig() {
		return (LevelingConfig) Bot2.configHandler.getConfig(ConfigType.LEVELING_CONFIG);
	}
}
