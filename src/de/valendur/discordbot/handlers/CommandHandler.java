package de.valendur.discordbot;

import java.util.ArrayList;
import java.util.List;

import de.valendur.discordbot.commands.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandHandler extends ListenerAdapter {

	List<Command> commands = new ArrayList<Command>();
	
	private String prefix;
	
	
	public CommandHandler(String prefix) {
		this.prefix = prefix;
	}
	
	public CommandHandler(String prefix, Command command) {
		this.prefix = prefix;
		addCommand(command);
	}
	
	public CommandHandler(String prefix, Command[] commands) {
		this.prefix = prefix;
		for (Command command : commands) {
			addCommand(command);
		}
	}
	
	public void onGuildMessageReceivedEvent(GuildMessageReceivedEvent e) {
		Message message = e.getMessage();
		String content = message.getContentRaw();
		
		if (!content.startsWith(prefix)) {
			return;
		}
		String commandMessage = content.replaceFirst(prefix, "");
		String commandText = commandMessage.split(" ")[0];
		String commandParams = commandMessage.replaceFirst(commandText + " ", "");
		
		for (Command command : commands) {
			if (command.getCommandText().equalsIgnoreCase(commandText)) {
				command.execute(e, commandParams);
			}
		}
		
	}
	
	
	
	public void addCommand(Command command) {
		commands.add(command);
	}
}
