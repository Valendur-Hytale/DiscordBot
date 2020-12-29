package de.valendur.discordbot;

import javax.security.auth.login.LoginException;

import de.valendur.discordbot.commands.PingCommand;
import de.valendur.discordbot.commands.ReloadCommand;
import de.valendur.discordbot.configs.BaseConfig;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.LevelingConfig;
import de.valendur.discordbot.configs.ReactionEmoteRoleConfig;
import de.valendur.discordbot.configs.SecurityConfig;
import de.valendur.discordbot.configs.TokenConfig;
import de.valendur.discordbot.handlers.CommandHandler;
import de.valendur.discordbot.handlers.ConfigHandler;
import de.valendur.discordbot.handlers.ReactionEmoteRoleHandler;
import de.valendur.discordbot.security.MessageSecurity;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Bot2 extends ListenerAdapter {
	
	public static JDA jda;
	public static CommandHandler commandHandler;
	public static ConfigHandler configHandler;
	public static ReactionEmoteRoleHandler reactionEmoteRoleHandler;
	
	//public static HashMap<String, String> emoteToRole = new HashMap<String, String>();
	
	 public static void main(String[] args ) {
		 Config.parseConfig();
		 initHandlers();
		 initConfigs();
		 
	        try {
	        	
	            jda = JDABuilder.createDefault(getTokenConfig().BOT_TOKEN)
	                    .addEventListeners(commandHandler)
	                    .addEventListeners(new MessageSecurity())
	                    .addEventListeners(reactionEmoteRoleHandler)
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
	        
	        setupHandlers();
	    }
	 
	 public static BaseConfig getBaseConfig() {
		 return (BaseConfig) configHandler.getConfig(ConfigType.BASE_CONFIG);
	 }
	 
	 public static TokenConfig getTokenConfig() {
		 return (TokenConfig) configHandler.getConfig(ConfigType.TOKEN_CONFIG);
	 }
	 
	 public static void initHandlers() {
		 initConfigs();
		 initCommands();
		 reactionEmoteRoleHandler = new ReactionEmoteRoleHandler();
	 }
	 
	 public static void initCommands() {
		 commandHandler = new CommandHandler(Config.COMMAND_PREFIX);
		 commandHandler.addCommand(new PingCommand("ping"));
		 commandHandler.addCommand(new ReloadCommand("reload"));
	 }
	 
	 public static void initConfigs() {
		 configHandler = new ConfigHandler();
		 configHandler.addConfig(new BaseConfig(ConfigType.BASE_CONFIG));
		 configHandler.addConfig(new ReactionEmoteRoleConfig(ConfigType.REACTION_ROLE_CONFIG));
		 configHandler.addConfig(new LevelingConfig(ConfigType.LEVELING_CONFIG));
		 configHandler.addConfig(new SecurityConfig(ConfigType.SECURITY_CONFIG));
		 configHandler.addConfig(new TokenConfig(ConfigType.TOKEN_CONFIG));
		 
		 configHandler.load();
	 }
	 
	 public static void setupHandlers() {
		 reactionEmoteRoleHandler.setup(jda.getGuilds().get(0));
	 }
	 
	 

}
