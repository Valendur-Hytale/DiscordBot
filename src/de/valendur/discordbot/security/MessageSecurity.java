package de.valendur.discordbot.security;

import de.valendur.discordbot.Config;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageSecurity extends ListenerAdapter {

	@Override
	public void onGuildMessageDelete(GuildMessageDeleteEvent e) {
		TextChannel channel = (TextChannel) e.getGuild().getGuildChannelById(Config.SECURITY_CHANNEL);
		
		channel.sendMessage("Nachricht gelöscht.").queue();
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		TextChannel channel = (TextChannel) e.getGuild().getGuildChannelById(Config.SECURITY_CHANNEL);
		
		channel.sendMessage("Neue Nachricht.").queue();
	}

	@Override
	public void onGuildMessageUpdate(GuildMessageUpdateEvent e) {
		TextChannel channel = (TextChannel) e.getGuild().getGuildChannelById(Config.SECURITY_CHANNEL);
		
		channel.sendMessage("Nachricht bearbeitet.").queue();
	}
	
	

}
