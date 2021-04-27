package de.valendur.discordbot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.login.LoginException;

import de.valendur.discordbot.commands.EXPCommand;
import de.valendur.discordbot.commands.PingCommand;
import de.valendur.discordbot.commands.ReloadCommand;
import de.valendur.discordbot.commands.WaitCommand;
import de.valendur.discordbot.configs.BaseConfig;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.LevelingConfig;
import de.valendur.discordbot.configs.ReactionEmoteRoleConfig;
import de.valendur.discordbot.configs.SecurityConfig;
import de.valendur.discordbot.configs.TokenConfig;
import de.valendur.discordbot.handlers.CommandHandler;
import de.valendur.discordbot.handlers.ConfigHandler;
import de.valendur.discordbot.handlers.DataHandler;
import de.valendur.discordbot.handlers.LevelingHandler;
import de.valendur.discordbot.handlers.ReactionEmoteRoleHandler;
import de.valendur.discordbot.security.MessageSecurity;
import de.valendur.discordbot.tasks.DailyLevelingResetTask;
import de.valendur.discordbot.tasks.DailyWebsiteFixer;
import de.valendur.discordbot.tasks.VoiceCheckTask;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Bot2 extends ListenerAdapter {
	
	public static JDA jda;
	
	public static CommandHandler commandHandler;
	public static ConfigHandler configHandler;
	public static ReactionEmoteRoleHandler reactionEmoteRoleHandler;
	public static LevelingHandler levelingHandler;
	public static DataHandler dataHandler;
	
	public static HashSet<Object> taskExecutors = new HashSet<Object>();
	
	 @SuppressWarnings("deprecation")
	public static void main(String[] args ) {
		 int c = 251;
		 byte d = (byte)c;
		 System.out.println(d);
		 
		 initHandlers();
		 initConfigs();
		 initUnirest();
		 initExecutors();
		 
		 
	        try {
	        	
	            jda = JDABuilder.createDefault(getTokenConfig().BOT_TOKEN)
	                    .addEventListeners(commandHandler)
	                    .addEventListeners(dataHandler)
	                    //.addEventListeners(new MessageSecurity())
	                    .addEventListeners(reactionEmoteRoleHandler)
	                    .addEventListeners(levelingHandler)
	                    .enableIntents(GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_PRESENCES)
	                    .setMemberCachePolicy(MemberCachePolicy.ALL)
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
	 
	 public static Guild getGuild() {
		 return jda.getGuilds().get(0);
	 }
	 
	 public static TokenConfig getTokenConfig() {
		 return (TokenConfig) configHandler.getConfig(ConfigType.TOKEN_CONFIG);
	 }
	 
	 public static void initHandlers() {
		 initConfigs();
		 initCommands();
		 reactionEmoteRoleHandler = new ReactionEmoteRoleHandler();
		 levelingHandler = new LevelingHandler();
		 dataHandler = new DataHandler();
	 }
	 
	 public static void initCommands() {
		 commandHandler = new CommandHandler(Config.COMMAND_PREFIX);
		 commandHandler.addCommand(new PingCommand("ping"));
		 commandHandler.addCommand(new ReloadCommand("reload"));
		 commandHandler.addCommand(new EXPCommand("xp"));
		 commandHandler.addCommand(new WaitCommand("wait"));
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
	 
	 public static void initExecutors() {
		 final ScheduledTaskExecutor dailyReset = new ScheduledTaskExecutor(new DailyLevelingResetTask(getBaseConfig().SCHEDULING_LEVELING_RESET_HOUR, 
				 																	getBaseConfig().SCHEDULING_LEVELING_RESET_MINUTE, 
				 																	getBaseConfig().SCHEDULING_LEVELING_RESET_SECOND));
		 
		 final ScheduledTaskExecutor dailyWebsiteFix = new ScheduledTaskExecutor(new DailyWebsiteFixer(getBaseConfig().SCHEDULING_LEVELING_RESET_HOUR, 
					getBaseConfig().SCHEDULING_LEVELING_RESET_MINUTE, 
					getBaseConfig().SCHEDULING_LEVELING_RESET_SECOND));
		 
		 final RepeatedTaskExecutor voiceCheck = new RepeatedTaskExecutor(new VoiceCheckTask(getBaseConfig().SCHEDULING_VOICE_CHECK));
		 
		 
		 taskExecutors.add(dailyReset);
		 taskExecutors.add(dailyWebsiteFix);
		 taskExecutors.add(voiceCheck);
	 }
	 
	 
	 public static void setupHandlers() {
		 reactionEmoteRoleHandler.setup(getGuild());
		 levelingHandler.setup(getGuild());
	 }
	 
	 public static void initUnirest() {
		 Unirest.config()
		 .defaultBaseUrl(getBaseConfig().BACKEND_LINK)
		 .addDefaultHeader("Content-Type", "application/json")
		 .errorHandler(error -> backendErrorHandler(error));
	 }
	 
	 
	 public static void backendErrorHandler(HttpResponse<?> error) {
		 getGuild().getTextChannelById(getBaseConfig().ERROR_CHANNEL)
		 .sendMessage("Backend error: " + error.getStatus() + " " 
				 		+ error.getStatusText() + " " + error.getBody().toString()).queue();
		 System.out.println("Error when trying to do smth: " + error.getStatus() + " " + error.getStatusText());
	 }
	 

}
