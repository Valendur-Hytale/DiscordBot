package de.valendur.discordbot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class GenericCommand {

	
	private String commandText;
	
	public GenericCommand(String commandText) {
		this.commandText = commandText;
	}
	
	public abstract void execute(GuildMessageReceivedEvent e, String commandParams);
	
	public String getCommandText() {
		return commandText;
	}
}
