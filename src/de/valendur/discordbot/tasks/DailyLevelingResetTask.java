package de.valendur.discordbot.tasks;

import de.valendur.discordbot.Bot2;

public class DailyLevelingResetTask extends GenericScheduledTask {

	public DailyLevelingResetTask(int hour, int minute, int second) {
		super(hour, minute, second);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		Bot2.levelingHandler.setup(Bot2.getGuild());
	}
	

}
