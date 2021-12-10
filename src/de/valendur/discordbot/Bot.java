package de.valendur.discordbot;

import java.util.Date;
import java.util.HashSet;
import javax.security.auth.login.LoginException;

import com.github.twitch4j.TwitchClient;

import de.valendur.discordbot.commands.AddTeamCommand;
import de.valendur.discordbot.commands.BirthdayCommand;
import de.valendur.discordbot.commands.EXPCommand;
import de.valendur.discordbot.commands.MeditateCommand;
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
import de.valendur.discordbot.handlers.LevelingHandler;
import de.valendur.discordbot.handlers.ReactionEmoteRoleHandler;
import de.valendur.discordbot.handlers.TwitchHandler;
import de.valendur.discordbot.tasks.DailyBirthdayChecker;
import de.valendur.discordbot.tasks.DailyLevelingResetTask;
import de.valendur.discordbot.tasks.DailyWebsiteFixer;
import de.valendur.discordbot.tasks.PrimeLeagueCheckTask;
import de.valendur.discordbot.tasks.VoiceCheckTask;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class Bot extends ListenerAdapter {
	
	public static JDA jda;
	
	public static CommandHandler commandHandler;
	public static ConfigHandler configHandler;
	public static ReactionEmoteRoleHandler reactionEmoteRoleHandler;
	public static LevelingHandler levelingHandler;
	public static TwitchHandler twitchHandler;
	public static HashSet<Object> taskExecutors = new HashSet<Object>();
	
	
	
	 @SuppressWarnings("deprecation")
	public static void main(String[] args ) {
		 initHandlers();
		 initConfigs();
		 initUnirest();
		 initExecutors();
		 
		 
	        try {
	        	
	            jda = JDABuilder.createDefault(getTokenConfig().BOT_TOKEN)
	                    .addEventListeners(commandHandler)
	                    //.addEventListeners(new BirthdayCommand())
	                    //.addEventListeners(new MessageSecurity())
	                    //.addEventListeners(reactionEmoteRoleHandler)
	                    //.addEventListeners(levelingHandler)
	                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
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
	        
	        
	        
	        createCommands();
//	        setupHandlers(); //might fail so nothing afterwards, TODO improve this shit lol
	       
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
		 twitchHandler = new TwitchHandler();
		 
	 }
	 
	 @Deprecated
	 public static void initCommands() {
		 commandHandler = new CommandHandler("!");
		 commandHandler.addCommand(new PingCommand("ping"));
		 commandHandler.addCommand(new ReloadCommand("reload"));
		 commandHandler.addCommand(new EXPCommand("xp"));
		 commandHandler.addCommand(new WaitCommand("wait"));
		 commandHandler.addCommand(new MeditateCommand("med"));
		 commandHandler.addCommand(new AddTeamCommand("addTeam"));
	 }
	 
	 public static void createCommands() {
		 //jda.retrieveCommands().complete().forEach(command -> command.delete().queue());
		 CommandListUpdateAction commands = getGuild().updateCommands(); //jda.updateCommands();

	        // Moderation commands with required options
	        commands.addCommands(
	            new CommandData("xp", "Zeigt dir deine Level-Karte an.")
	                .addOptions(new OptionData(USER, "user", "Gib an einen Nutzer an um dessen Level Karte zu sehen.", false) // USER type allows to include members of the server or other users by id
	                    .setRequired(false)) // This command requires a parameter
	                //.addOptions(new OptionData(INTEGER, "del_days", "Delete messages from the past days.")) // This is optional
	        );
	        
	        commands.addCommands(
		            new CommandData("kreload", "Lädt Kiras Config's neu. Das benötigt die entsprechenden Rechte.")// This command requires a parameter
		                //.addOptions(new OptionData(INTEGER, "del_days", "Delete messages from the past days.")) // This is optional
		        );
	        
	        commands.addCommands(
		            new CommandData("geburtstag", "Hiermit kannst du Geburtstage verwalten.")
		            	//.addOptions(new OptionData(SUB_COMMAND, "subcommand", "Hiermit kannst du Geburtstage verwalten."))
		            	.addSubcommands(
		            		new SubcommandData("set", "Hiermit setzt du einen neuen Geburtstag")
		            			.addOptions(new OptionData(INTEGER, "day", "Der Tag deines Geburtstags. (1-31)", true),
		            					    new OptionData(INTEGER, "month", "Der Monat deines Geburtstags. (1-12)", true)
		            					   // new OptionData(USER, "user", "Um den Geburtstag eines bestimmten Nutzers zu bearbeiten", false)
		            			)
		            		/*new SubcommandData("delete", "Hiermit löscht du einen Geburtstag")
		            			//.addOption(USER, "user", "Lösche den Geburtstag eines Nutzers. Benötigt entsprechende Rechte", false)
		            			*/
		            		)
		                //.addOptions(new OptionData(INTEGER, "del_days", "Delete messages from the past days.")) // This is optional
		        );
	        // Simple reply commands
	        /*commands.addCommands(
	            new CommandData("say", "Makes the bot say what you tell it to")
	                .addOptions(new OptionData(STRING, "content", "What the bot should say")
	                    .setRequired(true))
	        );

	        // Commands without any inputs
	        commands.addCommands(
	            new CommandData("leave", "Make the bot leave the server")
	        );

	        commands.addCommands(
	            new CommandData("prune", "Prune messages from this channel")
	                .addOptions(new OptionData(INTEGER, "amount", "How many messages to prune (Default 100)"))
	        );*/

	        // Send the new set of commands to discord, this will override any existing global commands with the new set provided here
	        commands.queue();
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
		 
		 final ScheduledTaskExecutor dailyBirthdayCheck = new ScheduledTaskExecutor(new DailyBirthdayChecker(0, 0, 0));
		 
		 final RepeatedTaskExecutor voiceCheck = new RepeatedTaskExecutor(new VoiceCheckTask(getBaseConfig().SCHEDULING_VOICE_CHECK));
		 final RepeatedTaskExecutor primeCheck = new RepeatedTaskExecutor(new PrimeLeagueCheckTask(60));
		 
		 
//		 taskExecutors.add(dailyReset);
//		 taskExecutors.add(dailyWebsiteFix);
//		 taskExecutors.add(voiceCheck);
//		 taskExecutors.add(dailyBirthdayCheck);
//		 taskExecutors.add(primeCheck);
	 }
	 
	 
	 public static void setupHandlers() {
		 levelingHandler.setup(getGuild());
		 twitchHandler.setup(getGuild());
		 
		 
		 reactionEmoteRoleHandler.setup(getGuild());
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
