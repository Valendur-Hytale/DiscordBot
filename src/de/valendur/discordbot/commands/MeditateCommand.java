package de.valendur.discordbot.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import de.valendur.discordbot.dbhandlers.DBMeditateHandler;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MeditateCommand extends GenericCommand{
	
	public static List<String> motivationLines;

	public MeditateCommand(String commandText) {
		super(commandText);
		try {
			motivationLines = Files.readAllLines(new File("motivation.txt").toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
