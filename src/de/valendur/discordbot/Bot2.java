package de.valendur.discordbot;

import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import de.valendur.discordbot.commands.PingCommand;
import de.valendur.discordbot.handlers.CommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Bot2 extends ListenerAdapter {
	
	public static JDA jda;
	public static CommandHandler commandHandler;
	
	//public static HashMap<String, String> emoteToRole = new HashMap<String, String>();
	
	 public static void main(String[] args ) {
		// parseConfig();
		 commandHandler = new CommandHandler(Config.COMMAND_PREFIX);
		 commandHandler.addCommand(new PingCommand("ping"));
		 
	        try {
	        	
	            jda = new JDABuilder(Config.BOT_TOKEN)
	                    .addEventListeners(commandHandler)
	                    .build();
	            jda.awaitReady(); 

	            
	        }
	        catch (LoginException e){

	            e.printStackTrace();
	        }
	        catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	 

}
