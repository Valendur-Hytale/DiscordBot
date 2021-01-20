package de.valendur.discordbot.commands;

import de.valendur.discordbot.dbhandlers.DBLevelingHandler;
import kong.unirest.Unirest;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class EXPCommand extends GenericCommand{

	public EXPCommand(String commandText) {
		super(commandText);
	}
	
	@Override
	public void execute(GuildMessageReceivedEvent e, String commandParams){
		e.getChannel().sendMessage("Youre Level is: " + DBLevelingHandler.getUser(e.getMember().getIdLong()).toString()).queue();
	}

}
