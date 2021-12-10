package de.valendur.discordbot.commands;

import de.valendur.discordbot.dbhandlers.DBPrimeLeagueHandler;
import de.valendur.discordbot.tasks.Team;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class AddTeamCommand extends GenericCommand{

	public AddTeamCommand(String commandText) {
		super(commandText);
	}
	
	@Override
	public void execute(GuildMessageReceivedEvent e, String commandParams){
		
		DBPrimeLeagueHandler.addTeam(e.getChannel(), new Team(commandParams, e.getChannel().getId()));
		
	}
	
	
	

}
