package de.valendur.discordbot.tasks;

public abstract class GenericRepeatedTask {

	private int seconds;
	
	public GenericRepeatedTask(int seconds) {
		this.seconds = seconds;
	}
	
	public abstract void execute();
	
	public int getSeconds() {
		return seconds;
	}
	
}
