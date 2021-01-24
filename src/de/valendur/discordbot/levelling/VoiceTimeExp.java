package de.valendur.discordbot.levelling;

import de.valendur.discordbot.handlers.RandomHandler;

public class VoiceTimeExp {

	
	private int time;
	private int minExp, maxExp;
	
	public VoiceTimeExp(int time, int minExp, int maxExp) {
		this.time = time;
		this.minExp = minExp;
		this.maxExp = maxExp;
	}
	
	public int getTime() {
		return time;
	}
	
	public int getExp() {
		return RandomHandler.randInt(minExp, maxExp);
	}
	
}
