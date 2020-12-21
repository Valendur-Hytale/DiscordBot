package de.valendur.discordbot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PingCommand extends GenericCommand{

	public PingCommand(String commandText) {
		super(commandText);
	}
	
	@Override
	public void execute(GuildMessageReceivedEvent e, String commandParams){
		e.getChannel().sendMessage("Pong!").queue();
		e.getChannel().sendMessage("Hilfe").queue();
	}

}
