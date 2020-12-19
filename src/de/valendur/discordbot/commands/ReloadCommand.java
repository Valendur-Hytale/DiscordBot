package de.valendur.discordbot.commands;

import de.valendur.discordbot.Config;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ReloadCommand extends Command{

	public ReloadCommand(String commandText) {
		super(commandText);
	}
	
	@Override
	public void execute(GuildMessageReceivedEvent e, String commandParams){
		Config.parseConfig();
		e.getChannel().sendMessage("Reloaded").queue();
	}

}
