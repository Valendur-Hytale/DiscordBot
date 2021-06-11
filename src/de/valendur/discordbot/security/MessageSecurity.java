package de.valendur.discordbot.security;

import de.valendur.discordbot.Bot;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.SecurityConfig;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageSecurity extends ListenerAdapter {

	@Override
	public void onGuildMessageDelete(GuildMessageDeleteEvent e) {
		TextChannel channel = (TextChannel) e.getGuild().getGuildChannelById(getConfig().SECURITY_CHANNEL);
		
		channel.sendMessage("Nachricht gel�scht.").queue();
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		if (e.getAuthor().isBot()) return;
		TextChannel channel = (TextChannel) e.getGuild().getGuildChannelById(getConfig().SECURITY_CHANNEL);
		
		channel.sendMessage("Neue Nachricht.").queue();
	}

	@Override
	public void onGuildMessageUpdate(GuildMessageUpdateEvent e) {
		TextChannel channel = (TextChannel) e.getGuild().getGuildChannelById(getConfig().SECURITY_CHANNEL);
		
		channel.sendMessage("Nachricht bearbeitet.").queue();
	}
	
	
	private SecurityConfig getConfig() {
		return (SecurityConfig) Bot.configHandler.getConfig(ConfigType.SECURITY_CONFIG);
	}
	
	

}
