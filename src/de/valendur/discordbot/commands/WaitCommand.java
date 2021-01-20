package de.valendur.discordbot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class WaitCommand extends GenericCommand{

	public WaitCommand(String commandText) {
		super(commandText);
	}
	
	@Override
	public void execute(GuildMessageReceivedEvent e, String commandParams){
		e.getChannel().sendMessage("Going to sleep. Can u wake me up?").queue();
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
