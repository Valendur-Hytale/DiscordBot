package de.valendur.discordbot.levelling;

import java.util.concurrent.TimeUnit;

import de.valendur.discordbot.Bot2;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.LevelingConfig;
import de.valendur.discordbot.dbhandlers.DBLevelingHandler;

public class LevelingUser {
	
	private long lastReaction, lastMessage;
	private long memberId;
	
	private int messageCount;
	
	private boolean inVoice;
	private long inVoiceSince;
	
	
	public LevelingUser(long id) {
		this.memberId = id;
		this.lastMessage = 0;
		this.lastReaction = 0;
		this.messageCount = 0;
	}
	
	
	public long getId() {
		return memberId;
	}
	
	public void setVoiceState(boolean inVoice) {
		this.inVoice = inVoice;
		if (inVoice) {
			inVoiceSince = System.currentTimeMillis();
		}
	}
	
	public boolean isInVoice() {
		return inVoice;
	}
	
	public boolean addMessage(final int length) {
		final LevelingConfig config = getConfig();
		
		messageCount++;
		int xp = 0;
		
		final long currentTime = System.currentTimeMillis();
		if (lastMessage + config.MESSAGE_DELAY < currentTime) {
			lastMessage = currentTime;
			xp += config.getExpByMessageLength(length);
		}
		
		xp += config.getExpByMessageCount(messageCount);
		
		if (xp != 0) {
			backendAddExp(xp);
		}
		
		return false; // TODO Return based on backend levelup response
	}
	
	public void voiceCheck() {
		if (inVoice) {
			backendAddExp(getConfig().getExpByVoiceTime((int) TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - inVoiceSince)));
		}
	}
	
	public void addReact() {
		final LevelingConfig config = getConfig();
		
		final long currentTime = System.currentTimeMillis();
		
		if (lastReaction + config.REACTION_DELAY < currentTime) {
			lastMessage = currentTime;
			backendAddExp(config.getExpByReaction());
		}
	}
	
	
	private void backendAddExp(int exp) {
		DBLevelingHandler.addExpToUser(memberId, exp, false);
	}

	
	
	public LevelingConfig getConfig() {
		return (LevelingConfig) Bot2.configHandler.getConfig(ConfigType.LEVELING_CONFIG);
	}
}
