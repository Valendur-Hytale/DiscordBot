package de.valendur.discordbot.tasks;

import de.valendur.discordbot.Bot;
import de.valendur.discordbot.Utils;
import net.dv8tion.jda.api.entities.Member;

public class VoiceCheckTask extends GenericRepeatedTask {


	public VoiceCheckTask(int seconds) {
		super(seconds);
	}

	@Override
	public void execute() {
		for (long id : Bot.levelingHandler.levelingUsers.keySet()) {
			Bot.levelingHandler.levelingUsers.get(id).voiceCheck();
		}
	}
	

}
