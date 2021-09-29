package de.valendur.discordbot.commands;

import de.valendur.discordbot.dbhandlers.DBMeditateHandler;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MeditateCommand extends GenericCommand{

	public MeditateCommand(String commandText) {
		super(commandText);
	}
	
	@Override
	public void execute(GuildMessageReceivedEvent e, String commandParams){
		int minutes = 0;
		try {
			minutes = Integer.parseInt(commandParams);
		} catch (NumberFormatException ex) {
			e.getChannel().sendMessage("Falsches Format! command [zahl]").queue();
			return;
		}	
		DBMeditateHandler.addMeditateTime(e.getMember().getIdLong(), minutes, e.getChannel());
	}

}
