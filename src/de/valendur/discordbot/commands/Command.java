package de.valendur.discordbot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class Command {

	
	private String commandText;
	
	
	public Command(String commandText) {
		this.commandText = commandText;
	}
	
	
	
	public void execute(GuildMessageReceivedEvent e, String commandParams){
	}
	
	
	
	
	
	
	public String getCommandText() {
		return commandText;
	}
}
