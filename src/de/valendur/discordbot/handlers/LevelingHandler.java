package de.valendur.discordbot.handlers;

import java.util.ArrayList;
import java.util.List;

import de.valendur.discordbot.Bot2;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.LevelingConfig;
import de.valendur.discordbot.levelling.LevelingUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LevelingHandler extends ListenerAdapter {
	
	List<LevelingUser> levelingUser = new ArrayList<LevelingUser>();

	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent e) {	
		if (e.retrieveUser().complete().isBot()) {
			return;
		} 

	}
	
	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent e) {	

	}
	
	
	
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		System.out.println("MemberId: " + e.getMember().getIdLong() + " UserId: " + e.getAuthor().getIdLong());
	}

	public void setup(Guild guild) {
	}
	
	public LevelingConfig getConfig() {
		return (LevelingConfig) Bot2.configHandler.getConfig(ConfigType.LEVELING_CONFIG);
	}
	
	
	private void userSendMessage(long id) {
		for (LevelingUser user : levelingUser) {
			if (id == user.getId()) {
				user.addMessage();
			}
		}
		
		LevelingUser user = new LevelingUser();
		user.addMessage();
		
		levelingUser.add(user);
	}
}
