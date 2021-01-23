package de.valendur.discordbot.tasks;

public abstract class GenericScheduledTask {

	private int hour, minute, second;
	
	public GenericScheduledTask(int hour, int minute, int second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	
	public abstract void execute();
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public int getSecond() {
		return second;
	}
	
}
