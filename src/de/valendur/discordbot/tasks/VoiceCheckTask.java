package de.valendur.discordbot.tasks;

import de.valendur.discordbot.Bot2;
import de.valendur.discordbot.Utils;
import net.dv8tion.jda.api.entities.Member;

public class VoiceCheckTask extends GenericRepeatedTask {


	public VoiceCheckTask(int seconds) {
		super(seconds);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		for (Member user : Utils.getUsersInVoiceChannel()) {
			Bot2.levelingHandler.levelingUsers.get(user.getIdLong()).voiceCheck();
		}
	}
	

}
