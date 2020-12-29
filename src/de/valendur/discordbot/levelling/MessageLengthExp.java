package de.valendur.discordbot.levelling;

import de.valendur.discordbot.handlers.RandomHandler;

public class MessageLengthExp {

	
	private int length;
	private int minExp, maxExp;
	
	public MessageLengthExp(int length, int minExp, int maxExp) {
		this.length = length;
		this.minExp = minExp;
		this.maxExp = maxExp;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getExp() {
		return RandomHandler.randInt(minExp, maxExp);
	}
	
}
