package de.valendur.discordbot.handlers;

import java.util.ArrayList;
import java.util.List;

import de.valendur.discordbot.Bot;
import de.valendur.discordbot.commands.BirthdayCommand;
import de.valendur.discordbot.commands.GenericCommand;
import de.valendur.discordbot.configs.BaseConfig;
import de.valendur.discordbot.configs.ConfigType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command.Subcommand;

public class CommandHandler extends ListenerAdapter {

	List<GenericCommand> commands = new ArrayList<GenericCommand>();
	
	private String prefix;
	
	
	public CommandHandler(String prefix) {
		this.prefix = prefix;
	}
	
	public CommandHandler(String prefix, GenericCommand command) {
		this.prefix = prefix;
		addCommand(command);
	}
	
	public CommandHandler(String prefix, GenericCommand[] commands) {
		this.prefix = prefix;
		for (GenericCommand command : commands) {
			addCommand(command);
		}
	}
	
	
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		prefix = getConfig().COMMAND_PREFIX;
		Message message = e.getMessage();
		String content = message.getContentRaw();
		
		if (!content.startsWith(prefix)) {
			return;
		}
		String commandMessage = content.replaceFirst(prefix, "");
		String commandText = commandMessage.split(" ")[0];
		String commandParams = commandMessage.replaceFirst(commandText + " ", "");
		
		for (GenericCommand command : commands) {
			if (command.getCommandText().equalsIgnoreCase(commandText)) {
				command.execute(e, commandParams);
			}
		}
		
	}
	
	@Override
	public void onSlashCommand(SlashCommandEvent event) {

		switch (event.getName()) {
		case "geburtstag": {
			BirthdayCommand.command(event, event.getSubcommandName());
			return;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + event.getName());
		}
	}
	
	
	
	public void addCommand(GenericCommand command) {
		commands.add(command);
	}
	
	public static BaseConfig getConfig() {
		return (BaseConfig) Bot.configHandler.getConfig(ConfigType.BASE_CONFIG);
	}
}
