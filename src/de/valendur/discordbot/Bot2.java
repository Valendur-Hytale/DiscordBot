package de.valendur.discordbot;

import javax.security.auth.login.LoginException;

import de.valendur.discordbot.commands.PingCommand;
import de.valendur.discordbot.commands.ReloadCommand;
import de.valendur.discordbot.handlers.CommandHandler;
import de.valendur.discordbot.security.MessageSecurity;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Bot2 extends ListenerAdapter {
	
	public static JDA jda;
	public static CommandHandler commandHandler;
	
	//public static HashMap<String, String> emoteToRole = new HashMap<String, String>();
	
	 public static void main(String[] args ) {
		 Config.parseConfig();
		 commandHandler = new CommandHandler(Config.COMMAND_PREFIX);
		 commandHandler.addCommand(new PingCommand("ping"));
		 commandHandler.addCommand(new ReloadCommand("reload"));
		 
	        try {
	        	
	            jda = JDABuilder.createDefault(Config.BOT_TOKEN)
	                    .addEventListeners(commandHandler)
	                    .addEventListeners(new MessageSecurity())
	                    .build();
	            jda.awaitReady();
	            System.out.println("Erfolgreich gestartet !");

	            
	        }
	        catch (LoginException e){

	            e.printStackTrace();
	        }
	        catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	 

}
