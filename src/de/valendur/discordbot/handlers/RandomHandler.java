package de.valendur.discordbot.handlers;

import java.util.Random;

public class RandomHandler {
	
	private static Random rand = new Random();
	
	
	
	
	public static int randInt(int min, int max) {
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
}
